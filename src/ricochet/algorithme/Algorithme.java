package ricochet.algorithme;

import ricochet.modele.Configuration;

import java.util.ArrayList;

import ricochet.modele.Case;
import ricochet.modele.Modele;

public class Algorithme {
	
	private Modele modele;
	private Resolution methodeResolution;
	
	/**
	 * Constructeur de la classe.
	 * 
	 * @param m instance du modele
	 */
	public Algorithme(Modele m) {
		this.modele = m;
	}
	
	/**
	 * Accesseur permetant d'obtenir l'instance du modele.
	 * 
	 * @return instance du modele
	 */
	public Modele getModele () {
		return this.modele;
	}
	
	public Resolution getMethodeResolution() {
		return methodeResolution;
	}

	public void setMethodeResolution(Resolution methodeResolution) {
		this.methodeResolution = methodeResolution;
	}

	public void genererConfig(int x, int y, int nbRobot, int[][] posRobot, int[] posObj, Case[][] murs) {
		modele.setConfigInitiale(new Configuration(x, y, nbRobot, posRobot, posObj, murs));
	}
	
	public ArrayList<Configuration> resoudre() {
		return methodeResolution.run();
	}
	
}
