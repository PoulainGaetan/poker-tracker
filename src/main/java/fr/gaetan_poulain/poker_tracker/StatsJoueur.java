package fr.gaetan_poulain.poker_tracker;

public class StatsJoueur {
	private int vpip = 0;

	private int pfr = 0;

	private int cbet = 0;

	private int nbOccasionCBet = 0;

	private int fCBet = 0;

	private int nbOccasionFCBet = 0;
	
	public int getVpip() {
		return vpip;
	}

	public void setVpip(int vpip) {
		this.vpip = vpip;
	}

	public int getPfr() {
		return pfr;
	}

	public void setPfr(int pfr) {
		this.pfr = pfr;
	}

	public int getCbet() {
		return cbet;
	}

	public void setCbet(int cbet) {
		this.cbet = cbet;
	}

	public int getNbOccasionCBet() {
		return nbOccasionCBet;
	}

	public void setNbOccasionCBet(int nbOccasionCBet) {
		this.nbOccasionCBet = nbOccasionCBet;
	}

	public int getfCBet() {
		return fCBet;
	}

	public void setfCBet(int fCBet) {
		this.fCBet = fCBet;
	}

	public int getNbOccasionFCBet() {
		return nbOccasionFCBet;
	}

	public void setNbOccasionFCBet(int nbOccasionFCBet) {
		this.nbOccasionFCBet = nbOccasionFCBet;
	}

}
