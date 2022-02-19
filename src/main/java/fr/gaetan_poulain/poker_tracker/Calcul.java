package fr.gaetan_poulain.poker_tracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Calcul {

	public static void methodePrincipale(File repertoire) throws IOException, FileNotFoundException {

		Collection<File> fichiersMain = Arrays.stream(repertoire.listFiles())
				.filter(f -> f.getName().contains("real_holdem_no-limit.txt")).collect(Collectors.toList());

		Collection<File> fichiersSummary = Arrays.stream(repertoire.listFiles())
				.filter(f -> f.getName().contains("real_holdem_no-limit_summary.txt")).collect(Collectors.toList());

		// Structure de données
		Map<String, PreStatsJoueur> statsJoueur = new TreeMap<String, PreStatsJoueur>();

		// Parcours des fichiers
		for (File file : fichiersMain) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String ligneCourante;
				boolean affichageSiegeJoueurs = false;
				boolean actionPreflop = false;
				boolean actionFlop = false;
				Set<String> joueursAyantDejaParlePreFlop = null;
				Set<String> joueursAyantDejaFoldeOuMiseAuFlop = null;

				// Parcours des lignes du fichier en cours de lecture
				while ((ligneCourante = br.readLine()) != null) {
					// Repère dans quel étape de la main nous sommes : Pré-flop, Flop, Turn, River,
					// Showdown, Summary
					if (ligneCourante.startsWith("Winamax Poker -")) {
						joueursAyantDejaParlePreFlop = new HashSet<String>();
						joueursAyantDejaFoldeOuMiseAuFlop = new HashSet<String>();
					} else if (ligneCourante.matches("Seat (\\d|10): .*")) {
						Integer numeroPlace = Integer.valueOf(ligneCourante.substring(0, 7).replace(":", "").replace("Seat ", ""));
						String nomJoueur = ligneCourante.replaceFirst("Seat (\\d|10): ", "").replaceFirst(" \\(\\d+\\) ", "");
					} else if (ligneCourante.startsWith("*** PRE-FLOP ***")) {
						actionPreflop = true;
						actionFlop = false;
					} else if (ligneCourante.startsWith("*** FLOP ***")) {
						actionPreflop = false;
						actionFlop = true;
					} else if (ligneCourante.startsWith("***")) {
						actionPreflop = false;
						actionFlop = false;
					} else {
						if (ligneCourante.matches(".*( folds| bets| raises| calls| checks).*")) {
							String nomJoueur = ligneCourante.split("( folds| bets| raises| calls| checks)")[0];
							String actionJoueur = ligneCourante.replace(nomJoueur, "").split(" ")[1];

							// Ajout du joueur dans la structure de données s'il n'existe pas
							statsJoueur.putIfAbsent(nomJoueur, new PreStatsJoueur(nomJoueur));

							// maj des stats
							if (actionPreflop) {
								majPreFlopPreStat(statsJoueur, joueursAyantDejaParlePreFlop, nomJoueur, actionJoueur);
							} else if (actionFlop) {
								majFlopPreStat(statsJoueur, joueursAyantDejaFoldeOuMiseAuFlop, nomJoueur, actionJoueur);
							}
						}
					}
				}
			}
		}

		// Récupération des joueurs de chaque table en cours
		Map<String, List<String>> joueursTablesCourantes = getJoueursTableEnCours(fichiersMain, fichiersSummary);
		
		System.out.print("****************************************************************************************************");

		// Affichage des stats de chaque joueur en cours
		for (Entry<String, List<String>> joueurTableCourante : joueursTablesCourantes.entrySet()) {
			System.out.println();
			System.out.println(joueurTableCourante.getKey());
			for (String joueurAAfficher : joueurTableCourante.getValue()) {
				afficheStatsJoueur(statsJoueur.get(joueurAAfficher),
						calculeStatsJoueur(statsJoueur.get(joueurAAfficher)));
			}
		}
	}

	/**
	 * Récupère les joueurs de chaque table en cours
	 */
	private static Map<String, List<String>> getJoueursTableEnCours(Collection<File> fichiersMain,
			Collection<File> fichiersSummary) throws IOException, FileNotFoundException {
		// Récupération des tables en cours. Suppose que les tables terminées ont un
		// fichier avec le même nom mais finissant par _summary
		Set<File> fichiersEnCours = getFichierTournoisEnCours(fichiersMain, fichiersSummary);

		Map<String, List<String>> tablesCourantes = new HashMap<String, List<String>>();
		String ligneCourante;
		boolean listeJoueursMain = false;
		List<String> listeJoueursDerniereMain = new ArrayList<String>();
		for (File tableEnCours : fichiersEnCours) {
			try (BufferedReader br = new BufferedReader(new FileReader(tableEnCours))) {
				while ((ligneCourante = br.readLine()) != null) {
					if (ligneCourante.startsWith("*** PRE-FLOP ***")) {
						listeJoueursMain = true;
						listeJoueursDerniereMain = new ArrayList<String>();
					} else if (ligneCourante.startsWith("*** ")) {
						listeJoueursMain = false;
					} else if (listeJoueursMain) {
						if (ligneCourante.matches(".*( folds| bets| raises| calls| checks).*")) {
							listeJoueursDerniereMain
									.add(ligneCourante.split("( folds| bets| raises| calls| checks)")[0]);
						}
					}
				}
				tablesCourantes.put(tableEnCours.getName(), listeJoueursDerniereMain);
			}
		}
		return tablesCourantes;
	}

	/**
	 * Récupère les fichiers des tournois en cours
	 */
	private static Set<File> getFichierTournoisEnCours(Collection<File> fichiersMain,
			Collection<File> fichiersSummary) {
		Set<File> fichiersEnCours = new HashSet<File>();
		for (File fichierMainEnCours : fichiersMain) {
			boolean tableEnCours = true;
			for (File fileSummary : fichiersSummary) {
				if (fileSummary.getName().replace("_summary", "").equals(fichierMainEnCours.getName())) {
					tableEnCours = false;
				}
			}
			if (tableEnCours) {
				fichiersEnCours.add(fichierMainEnCours);
			}
		}
		return fichiersEnCours;
	}

	/**
	 * Calcule et affiche les stats du joueur
	 */
	private static StatsJoueur calculeStatsJoueur(PreStatsJoueur statJoueur) {
		StatsJoueur statsJoueur = new StatsJoueur();

		// Calcul des stats intéressantes
		statsJoueur.setVpip(Math.round(100 * ((float) (statJoueur.getPreflopRaise() + statJoueur.getPreflopCall())
				/ statJoueur.getNbMainPreflop())));
		statsJoueur.setPfr(Math.round(100 * ((float) (statJoueur.getPreflopRaise()) / statJoueur.getNbMainPreflop())));

		statsJoueur.setCbet(Math.round(100
				* (((float) statJoueur.getFlopBet() / ((float) statJoueur.getFlopBet() + statJoueur.getFlopCheck())))));
		statsJoueur.setNbOccasionCBet(statJoueur.getFlopBet() + statJoueur.getFlopCheck());

		statsJoueur.setfCBet(Math.round(100 * ((float) (statJoueur.getFlopFold()))
				/ (statJoueur.getFlopFold() + statJoueur.getFlopCall() + statJoueur.getFlopRaise())));
		statsJoueur.setNbOccasionFCBet(statJoueur.getFlopFold() + statJoueur.getFlopCall() + statJoueur.getFlopRaise());

		return statsJoueur;
	}

	/**
	 * Affiche les stats du joueur
	 */
	private static void afficheStatsJoueur(PreStatsJoueur preStatsJoueur, StatsJoueur statsJoueur) {
		System.out.print(preStatsJoueur.getNom() + " : " + statsJoueur.getVpip() + "/" + statsJoueur.getPfr() + " ("
				+ preStatsJoueur.getNbMainPreflop() + ")");
		System.out.println("; cbet : " + statsJoueur.getCbet() + " (" + statsJoueur.getNbOccasionCBet() + ")"
				+ ". fcbet : " + statsJoueur.getfCBet() + " (" + statsJoueur.getNbOccasionFCBet() + ")");
	}

	/**
	 * Maj les pré stats du flop
	 */
	private static void majFlopPreStat(Map<String, PreStatsJoueur> mapStatsJoueur,
			Set<String> joueursAyantDejaFoldeOuMiseAuFlop, String nomJoueur, String actionJoueur) {
		if (!joueursAyantDejaFoldeOuMiseAuFlop.contains(nomJoueur)) {
			mapStatsJoueur.get(nomJoueur).setNbFlop(mapStatsJoueur.get(nomJoueur).getNbFlop() + 1);

			if (actionJoueur.equals("bets")) {
				mapStatsJoueur.get(nomJoueur).setFlopBet(mapStatsJoueur.get(nomJoueur).getFlopBet() + 1);
			} else if (actionJoueur.equals("checks")) {
				mapStatsJoueur.get(nomJoueur).setFlopCheck(mapStatsJoueur.get(nomJoueur).getFlopCheck() + 1);
			} else if (actionJoueur.equals("calls")) {
				mapStatsJoueur.get(nomJoueur).setFlopCall(mapStatsJoueur.get(nomJoueur).getFlopCall() + 1);
			} else if (actionJoueur.equals("raises")) {
				mapStatsJoueur.get(nomJoueur).setFlopRaise(mapStatsJoueur.get(nomJoueur).getFlopRaise() + 1);
			} else if (actionJoueur.equals("folds")) {
				mapStatsJoueur.get(nomJoueur).setFlopFold(mapStatsJoueur.get(nomJoueur).getFlopFold() + 1);
			}

			if (!actionJoueur.equals("checks")) {
				joueursAyantDejaFoldeOuMiseAuFlop.add(nomJoueur);
			}
		}
	}

	/**
	 * Maj les pré stats préflop
	 */
	private static void majPreFlopPreStat(Map<String, PreStatsJoueur> mapStatsJoueur,
			Set<String> joueursAyantDejaParleAuFlop, final String nomJoueur, final String actionJoueur) {
		if (!joueursAyantDejaParleAuFlop.contains(nomJoueur)) {
			mapStatsJoueur.get(nomJoueur).setNbMainPreflop(mapStatsJoueur.get(nomJoueur).getNbMainPreflop() + 1);

			if (actionJoueur.equals("folds") || actionJoueur.equals("checks")) {
				mapStatsJoueur.get(nomJoueur)
						.setPreflopFoldCheck(mapStatsJoueur.get(nomJoueur).getPreflopFoldCheck() + 1);
			} else if (actionJoueur.equals("raises")) {
				mapStatsJoueur.get(nomJoueur).setPreflopRaise(mapStatsJoueur.get(nomJoueur).getPreflopRaise() + 1);
			} else if (actionJoueur.equals("calls")) {
				mapStatsJoueur.get(nomJoueur).setPreflopCall(mapStatsJoueur.get(nomJoueur).getPreflopCall() + 1);
			}

			joueursAyantDejaParleAuFlop.add(nomJoueur);
		}
	}

}
