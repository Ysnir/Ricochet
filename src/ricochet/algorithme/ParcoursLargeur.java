package ricochet.algorithme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;
import ricochet.utilitaire.Direction;

public class ParcoursLargeur implements Resolution{

	/**
	 * Methode de resolution du parcours en largeur des etats.
	 * Realise un parcours en largeur en sauvegardant les etats deja visite pour ne pas les revisiter a une profondeur de recherche plus importante.
	 * Appel mouvementRobot pour connaitre chaque possibilite de deplacement des robots pour une configuration.
	 * 
	 * @return une liste de configuration de la configuration initiale a la configuration finale
	 * @see ParcoursLargeur#mouvementRobot(Configuration)
	 */
	public ArrayList<Configuration> run() {

		ArrayList<Configuration> visite = new ArrayList<Configuration>();
		Queue<Configuration> fileConfig = new LinkedList<Configuration>();
		visite.add(Modele.getInstance().getConfigInitiale());
		fileConfig.add(Modele.getInstance().getConfigInitiale());
		boolean fini = false;
		ArrayList<Configuration> chemin = new ArrayList<Configuration>();
		
		while(!fini && !fileConfig.isEmpty()) {//TODO puzzle insolvable condition de sortie de boucle
			Configuration suivant = new Configuration(fileConfig.poll());
			ArrayList<Configuration> aTraiter = new ArrayList<Configuration>(mouvementRobot(suivant));
			for(Configuration e : aTraiter) {
				if(!visite.contains(e)) {
					visite.add(e);
					fileConfig.add(e);
				}
			}
			fini = suivant.isFini();

			if(fini){
				for(int i=suivant.getProfondeurRecherche(); i>0; i--) {
					chemin.add(suivant);
					suivant = suivant.getPere();
				}
			}
		}
		
		return chemin;
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
