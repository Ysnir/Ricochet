package ricochet.algorithme;

import java.util.ArrayList;
import java.util.Arrays;

import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;
import ricochet.utilitaire.Direction;

public class AStar implements Resolution {
	
	private int[][] cost = new int[Modele.getInstance().getConfigInitiale().getxPlateau()][Modele.getInstance().getConfigInitiale().getyPlateau()];

	public ArrayList<Configuration> run() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void couloirsHeuristique() {
		int[] coordObjectif = Arrays.copyOf(Modele.getInstance().getConfigInitiale().getPositionObjectif(), 2);
		for(Direction dir : Direction.values()) {
			if(Modele.getInstance().getConfigInitiale().getPlateau()[coordObjectif[0]][coordObjectif[1]].isDirection(dir)) {
				propage(coordObjectif, dir, 1);
			}
		}
	}

	private void propage(int[] coord, Direction mouvement, int generation) {
		Case[][] plateauInitial = Modele.getInstance().getConfigInitiale().getPlateau();
		
		for(Direction dir : Direction.values()) {
			if(plateauInitial[coord[0]][coord[1]].isDirection(dir)) {
				
				/*On cree une nouvelle coordonne pour la case vers laquelle on se dirige. 
				 * Pour cela on recupere les coordonnees de la case courante que l'on modifie
				 * on fonction de chaque directions vers lesquelles on se dirige.*/
				int[] voisinCoord = new int[2];
				voisinCoord[0] = coord[0] + dir.getMouvement()[0];
				voisinCoord[1] = coord[1] + dir.getMouvement()[1];
				
				/*Si l'on peut s'arreter sur une case accessible depuis la case courante on propage
				 *  vers la direction d'ou l'on proviendrait s'il on s'etait arrete sur cette case.
				 *  (Si l'on longe une case avec un mur pendant la propagation on propage depuis cette case)
				 */
				if((mouvement == Direction.NORD) || (mouvement == Direction.SUD)) {
					if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isNord() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isSud()) {
						propage(voisinCoord, Direction.NORD, generation++);
					}
					if(!plateauInitial[voisinCoord[0]][voisinCoord[1]].isNord() && plateauInitial[voisinCoord[0]][voisinCoord[1]].isSud()) {
						propage(voisinCoord, Direction.SUD, generation++);
					}
				}
				
				//!\ Premieres conditions inversees ?
				if((mouvement == Direction.EST) || (mouvement == Direction.OUEST)) {
					if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isOuest() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isEst()) {
						propage(voisinCoord, Direction.OUEST, generation++);
					}
					if(!plateauInitial[voisinCoord[0]][voisinCoord[1]].isOuest() && plateauInitial[voisinCoord[0]][voisinCoord[1]].isEst()) {
						propage(voisinCoord, Direction.EST, generation++);
					}
				}
			}
		}
		
		//On continue la propagation dans la direction donnee
		cost[coord[0]][coord[1]] = generation;
		if(plateauInitial[coord[0]][coord[1]].isDirection(mouvement)){
			int[] suivantCoord = new int[2];
			suivantCoord[0] = coord[0] + mouvement.getMouvement()[0];
			suivantCoord[1] = coord[1] + mouvement.getMouvement()[1];
			propage(suivantCoord, mouvement, generation);
			//on incremente pas generation car dans ce cas le robot n'a pas a changer de direction
		}
	}
	
}