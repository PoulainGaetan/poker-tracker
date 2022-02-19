package fr.gaetan_poulain.poker_tracker;

public class PreStatsJoueur {
	private String nom = null;
	
	private int nbMainPreflop = 0;
	
	private int preflopFoldCheck = 0;

	private int preflopRaise = 0;

	private int preflopCall = 0;

	private int nbFlop = 0;
	
	private int flopBet = 0;
	
	private int flopCall = 0; // Peut signifier qu'il a checké avant
	
	private int flopRaise = 0;
	
	private int flopCheck = 0;
	
	private int flopFold = 0;

	public PreStatsJoueur() {
		
	}

	
	
	public PreStatsJoueur(String nom) {
		this.nom = nom;
	}



	public int getFlopFold() {
		return flopFold;
	}

	public void setFlopFold(int flopFold) {
		this.flopFold = flopFold;
	}

	public int getFlopCheck() {
		return flopCheck;
	}

	public void setFlopCheck(int flopCheck) {
		this.flopCheck = flopCheck;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNbMainPreflop() {
		return nbMainPreflop;
	}

	public void setNbMainPreflop(int nbMainPreflop) {
		this.nbMainPreflop = nbMainPreflop;
	}

	public int getNbFlop() {
		return nbFlop;
	}

	public void setNbFlop(int nbFlop) {
		this.nbFlop = nbFlop;
	}

	public int getPreflopFoldCheck() {
		return preflopFoldCheck;
	}

	public void setPreflopFoldCheck(int preflopFoldCheck) {
		this.preflopFoldCheck = preflopFoldCheck;
	}

	public int getPreflopRaise() {
		return preflopRaise;
	}

	public void setPreflopRaise(int preflopRaise) {
		this.preflopRaise = preflopRaise;
	}

	public int getPreflopCall() {
		return preflopCall;
	}

	public void setPreflopCall(int preflopCall) {
		this.preflopCall = preflopCall;
	}

	public int getFlopBet() {
		return flopBet;
	}

	public void setFlopBet(int flopBet) {
		this.flopBet = flopBet;
	}

	public int getFlopCall() {
		return flopCall;
	}

	public void setFlopCall(int flopCall) {
		this.flopCall = flopCall;
	}

	public int getFlopRaise() {
		return flopRaise;
	}

	public void setFlopRaise(int flopRaise) {
		this.flopRaise = flopRaise;
	}

	
}
