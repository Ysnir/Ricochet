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
	 * Accesseur de la methode de resolution courante
	 * 
	 * @return instance de la methode de résolution
	 */
	public Resolution getMethodeResolution() {
		return methodeResolution;
	}

	/**
	 * mutateur permettant de changer la methode de resolution utilisee
	 * 
	 * @param methodeResolution nouvelle methode de resolution
	 */
	public void setMethodeResolution(Resolution methodeResolution) {
		this.methodeResolution = methodeResolution;
	}

	/**
	 * genere la configuration initiale du plateau avant resolution.
	 * Transmet les infos de la vue au modele en appelant la methode setConfigInitiale.
	 * 
	 * @see Modele#setConfigInitiale(Configuration)
	 * @param x largeur du plateau
	 * @param y longeur du plateau
	 * @param nbRobot nombre de robot
	 * @param posRobot tableau de coordonnee des robots
	 * @param posObj coordonnee de l'objectif
	 * @param murs tableau contenant les "murs" du plateau
	 */
	public void genererConfig(int x, int y, int nbRobot, int[][] posRobot, int[] posObj, Case[][] murs) {
		modele.setConfigInitiale(new Configuration(x, y, nbRobot, posRobot, posObj, murs));
	}
	
	/**
	 * Resout le la configuration initiale avec la methode de resolution courante. 
	 * Renvoie la liste des etat par lequel le plateau passe pour etre resolut.
	 * Appel la methode run de la methode de resolution courante (specifique a chaque maniere de resoudre le probleme).
	 * 
	 * @see Resolution#run()
	 * @return une liste de configuration reprenant le "chemin" des configuration jusqu'a l'arrive du robot 0 a l'objectif
	 */
	public void resoudre() {
		this.modele.setParcours(methodeResolution.lancer());
		
	}
	
}
