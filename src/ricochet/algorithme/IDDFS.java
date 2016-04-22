package ricochet.algorithme;

import java.util.ArrayList;
import java.util.Arrays;

import ricochet.modele.Configuration;
import ricochet.modele.Modele;
import ricochet.utilitaire.Direction;

public class IDDFS implements Resolution{
	Configuration configCourante;
	ArrayList<Configuration> chemin = new ArrayList<Configuration>();
	
	public ArrayList<Configuration> lancer() {
		for(int profondeur = 0; profondeur < 600; profondeur++) { //600 ?
			configCourante = parcoursProfondeurRecursif(Modele.getInstance().getConfigInitiale(), profondeur);
			if(configCourante != null) {
				for(int i=configCourante.getProfondeurRecherche(); i>=0; i--) {
					chemin.add(configCourante);
					configCourante = configCourante.getPere();
				}
				return chemin;
			}
		}
		return null;
	}
	
	public Configuration parcoursProfondeurRecursif(Configuration c, int profondeur) {
		Configuration but;
		if(profondeur == 0 && c.isFini()) {
			return c;
		} else if(profondeur > 0) {	
			ArrayList<Configuration> filles = new ArrayList<Configuration>(mouvementRobot(c));
			for(Configuration fille : filles) {
				but = parcoursProfondeurRecursif(fille, profondeur - 1);
				if(but != null) {
					return but;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Genere une liste de configurations fille a partir d'une configuration en deplacant chaque robot dans toute les directions non-bouchee
	 * Appel {@link ricochet.modele.Configuration#bougeJusqueObstacle(int[], Direction)} qui retourne la coordonnee d'un robot apres l'avoir deplace dans une direction
	 * 
	 * @param c configuration mere
	 * @return liste de configuration filles
	 */
	public ArrayList<Configuration> mouvementRobot(Configuration c) {
		ArrayList<Configuration> suivants = new ArrayList<Configuration>();
		
		for(int i=0; i<c.getPositionRobots().length; i++) {
			
			for (Direction dir : Direction.values()) {
				
				int[] nouvellePosition = new int[2];
				nouvellePosition = Arrays.copyOf(c.bougeJusqueObstacle(c.getPositionRobots()[i], dir), 2);
				
				if(!Arrays.equals(nouvellePosition, c.getPositionRobots()[i])){
					Configuration nouvelleConfig = new Configuration(c);

					nouvelleConfig.getPositionRobots()[i] = nouvellePosition;
					
					nouvelleConfig.getPlateau()[nouvellePosition[0]][nouvellePosition[1]].setRobot(true);
					
					nouvelleConfig.getPlateau()[c.getPositionRobots()[i][0]][c.getPositionRobots()[i][1]].setRobot(false);
					
					nouvelleConfig.setProfondeurRecherche(c.getProfondeurRecherche()+1);
					
					nouvelleConfig.setPere(c);
					
					suivants.add(nouvelleConfig);
				}
			}
			
		}
		
		return suivants;
	}

}
