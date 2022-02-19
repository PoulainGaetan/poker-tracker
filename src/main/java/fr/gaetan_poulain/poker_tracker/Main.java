package fr.gaetan_poulain.poker_tracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		File repertoire = new File("C:\\Users\\Gaetan Poulain\\Documents\\Winamax Poker\\accounts\\poulinkow\\history");

		// Exécute le programme toutes les 10 secondes si un des fichiers a été modifié
		Long previousModifiedDate = 0l;
		while (true) {
			Thread.sleep(10000);
			Long lastModifiedDate = Arrays.stream(repertoire.listFiles()).mapToLong(f -> f.lastModified()).max().getAsLong();
			if (!previousModifiedDate.equals(lastModifiedDate)) {
				previousModifiedDate = lastModifiedDate;
				Calcul.methodePrincipale(repertoire);
			}
		}
	}

}
