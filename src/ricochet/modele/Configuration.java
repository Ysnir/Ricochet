package ricochet.modele;

import java.util.Arrays;
import java.util.List;

import ricochet.utilitaire.Direction;

public class Configuration {
	//Dimensions du plateau de jeu : x = nombre de colonnes et y = nombre de lignes
	private int xPlateau;
	private int yPlateau;
	
	//Position des robots pour accès direct du "robot 0" au "robot n" donne le numéro de la case ou il est
	private int[][] positionRobots;
	//Position de l'objectif
	private int[] positionObjectif;
	
	//Liste de case représentant le plateau avec ses murs, robots et l'objectif
	private Case[][] plateau;
	
	//Profondeur de recherche pour les différents parcours
	private int profondeurRecherche;
	
	//Pere de la configuration lors d'un parcours
	private Configuration pere;
	
	/**
	 * Constructeur d'une configuration de jeu utilisée pour le plateau initial et pour les configurations du parcours
	 * 
	 * @param x nombre de colonnes du plateau
	 * @param y nombre de lignes du plateau
	 * @param nbRob nombre de robot
	 * @param posRob position des robots
	 * @param posObj position de l'objectif
	 */
	public Configuration(int x, int y, int[][] posRob, int[] posObj, Case[][] murs) {
		this.xPlateau = x;
		this.yPlateau = y;
		this.positionRobots = posRob;
		this.positionObjectif = posObj;
		this.plateau = murs;
		this.profondeurRecherche = 0;
		this.pere = null;
		
	}
	
	/**
	 * Constructeur par recopie
	 * 
	 * @param c la configuration de plateau à cloner
	 */
	public Configuration(Configuration c) {
		this.xPlateau = c.getxPlateau();
		this.yPlateau = c.getyPlateau();
		
		this.positionRobots = new int[c.getPositionRobots().length][2];
		for(int k=0; k<c.getPositionRobots().length; k++) {
			this.positionRobots[k][0] = c.getPositionRobots()[k][0];
			this.positionRobots[k][1] = c.getPositionRobots()[k][1];
		}

		this.positionObjectif = Arrays.copyOf(c.getPositionObjectif(), c.getPositionObjectif().length);
		
		this.plateau = new Case[xPlateau][yPlateau];
		for(int i=0; i<c.getPlateau().length; i++) {
			for(int j=0; j<c.getPlateau()[i].length; j++) {
				this.plateau[i][j] = new Case(c.getPlateau()[i][j]);
			}
		}
		
		this.profondeurRecherche = c.getProfondeurRecherche();
		this.pere = c.getPere();
	}
	
	/**
	 * Methode qui determine quelle sera la position finale d'un robot de coordonnees de depart donnees si on l'envoi dans une direction donnee
	 * La fonction teste s'il y a des obstacle en condition d'arret et s'appelle recursivement sur les case suivante que le robot parcours
	 * 
	 * @param coordRobot coordonnees de depart du robot
	 * @param sens direction vers laquelle le robot se deplace
	 * @return la coordonnee finale a laquelle le robot va s'arreter
	 */
	public int[] bougeJusqueObstacle(int coordRobot[], Direction sens) {
		int xCase = coordRobot[0];
		int yCase = coordRobot[1];
		int[] nouvellePos = new int[2];

		Case caseCour = this.getPlateau()[xCase][yCase];
		
		if(!caseCour.isDirection(sens)) {
			nouvellePos[0] = xCase;
			nouvellePos[1] = yCase;
			return nouvellePos;
			
		} else {
			Case caseSuiv = this.getPlateau()[coordRobot[0] + sens.getMouvement()[0]][coordRobot[1] + sens.getMouvement()[1]];

			if(caseSuiv.isRobot()) {
				nouvellePos[0] = xCase;
				nouvellePos[1] = yCase;
				return nouvellePos;
			}
			
			nouvellePos[0] = coordRobot[0] + sens.getMouvement()[0];
			nouvellePos[1] = coordRobot[1] + sens.getMouvement()[1];
			
			return bougeJusqueObstacle(nouvellePos, sens);
		}
	}
	
	public boolean isFini() {
		return ((this.positionRobots[0][0] == this.positionObjectif[0]) && (this.positionRobots[0][1] == this.positionObjectif[1]));
	}

	public String toString() {
		String strPosRob = new String();
		String strPlateau = new String();
		for(int i=0; i<positionRobots.length; i++) {
			strPosRob = strPosRob + Arrays.toString(positionRobots[i]);
		}
		
		for(int k=0; k<yPlateau; k++) {
			for(int j=0; j<xPlateau; j++) {
				strPlateau = strPlateau + plateau[j][k].toString();
			}
			strPlateau = strPlateau + '\n';
		}
		
		return "Configuration {xPlateau=" + xPlateau + ", yPlateau=" + yPlateau + ", positionRobots="
				+ strPosRob + ", positionObjectif=" + Arrays.toString(positionObjectif)+ ", profondeurRecherche=" + profondeurRecherche
				+ ", plateau=\n" + strPlateau  + "}";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(plateau);
		result = prime * result + Arrays.deepHashCode(positionRobots);

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (pere == null) {
			if (other.pere != null)
				return false;
		} else if (!pere.equals(other.pere))
			return false;
		if (!Arrays.deepEquals(plateau, other.plateau))
			return false;
		if (!Arrays.equals(positionObjectif, other.positionObjectif))
			return false;
		if (!Arrays.deepEquals(positionRobots, other.positionRobots))
			return false;
		if (profondeurRecherche != other.profondeurRecherche)
			return false;
		if (xPlateau != other.xPlateau)
			return false;
		if (yPlateau != other.yPlateau)
			return false;
		return true;
	}

	public int getxPlateau() {
		return xPlateau;
	}

	public int getyPlateau() {
		return yPlateau;
	}

	public int[][] getPositionRobots() {
		return positionRobots;
	}

	public int[] getPositionObjectif() {
		return positionObjectif;
	}

	public Case[][] getPlateau() {
		return plateau;
	}

	public int getProfondeurRecherche() {
		return profondeurRecherche;
	}

	public void setxPlateau(int xPlateau) {
		this.xPlateau = xPlateau;
	}

	public void setyPlateau(int yPlateau) {
		this.yPlateau = yPlateau;
	}

	public void setPositionRobots(int[][] positionRobots) {
		this.positionRobots = positionRobots;
	}

	public void setPositionObjectif(int[] positionObjectif) {
		this.positionObjectif = positionObjectif;
	}

	public void setPlateau(Case[][] plateau) {
		this.plateau = plateau;
	}

	public void setProfondeurRecherche(int profondeurRecherche) {
		this.profondeurRecherche = profondeurRecherche;
	}

	public Configuration getPere() {
		return pere;
	}

	public void setPere(Configuration pere) {
		this.pere = pere;
	}

}
