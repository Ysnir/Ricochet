package ricochet.algorithme;

import java.util.ArrayList;
import java.util.Arrays;

import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;
import ricochet.utilitaire.Direction;

public class AStar implements Resolution {
	
	private int[][] coutHeuristique = new int[getInit().getxPlateau()][getInit().getyPlateau()];

	public ArrayList<Configuration> lancer() {
		ArrayList<int[]> dejaVu = new ArrayList<int[]>();
		ArrayList<int[]> aExplorer = new ArrayList<int[]>();
		aExplorer.add(Arrays.copyOf(Modele.getInstance().getConfigInitiale().getPositionObjectif(), 2));
		ArrayList<Configuration> chemin = new ArrayList<Configuration>();
		
		//Le cout pour atteindre un noeud depuis le debut ainsi que celui pour atteindre l'objectif en passant par ce noeud
		int[][] coutAtteindre = new int[getInit().getxPlateau()][getInit().getyPlateau()];
		int[][] coutTraverser = new int[getInit().getxPlateau()][getInit().getyPlateau()];
		
		for (int[] row: coutAtteindre) {
		    Arrays.fill(row, -1);
		}
		//le cout pour aller du depart au depart vaut 0
		coutAtteindre[getInit().getPositionRobots()[0][0]][getInit().getPositionRobots()[0][1]] = 0;
		
		for (int[] row: coutTraverser) {
		    Arrays.fill(row, -1);
		}
		//On recupere la valeur approximee pour atteindre l'objectif depuis le depart
		coutTraverser[getInit().getPositionRobots()[0][0]][getInit().getPositionRobots()[0][1]] = coutHeuristique[getInit().getPositionRobots()[0][0]][getInit().getPositionRobots()[0][1]];
		
		while(!aExplorer.isEmpty()){
			int[] courant = Arrays.copyOf(this.minArray(aExplorer, coutTraverser), 2);
			
			if(Arrays.equals(courant, getInit().getPositionObjectif())) {
				return chemin;
			}
			
			aExplorer.remove(courant);
			dejaVu.add(courant);
			
			//On parcourt chaque voisin de courant
			for(int[] voisin : calculNoeudVoisins(courant)) {
				if(dejaVu.contains(voisin)) {
					continue;
				}
				int raccourcis = coutAtteindre[courant[0]][courant[1]] + 1;
				
				if(!aExplorer.contains(voisin)) {
					aExplorer.add(voisin);
				} else if(raccourcis >= coutAtteindre[voisin[0]][voisin[1]]) {//Le nouveau chemin est pire que l'ancien
					continue;
				}
				
				chemin.add(configVierge(courant));
				coutAtteindre[voisin[0]][voisin[1]] = raccourcis;
				coutTraverser[voisin[0]][voisin[1]] = coutAtteindre[voisin[0]][voisin[1]] + coutHeuristique[voisin[0]][voisin[1]];
			}
		}
		
		return null;
	}
	
	public void couloirsHeuristique() {
		
		//On rempli le cout avec -1 pour signifier -infini
		for (int[] row: this.coutHeuristique) {
		    Arrays.fill(row, -1);
		}

		int[] coordObjectif = Arrays.copyOf(Modele.getInstance().getConfigInitiale().getPositionObjectif(), 2);
		for(Direction dir : Direction.values()) {
			if(Modele.getInstance().getConfigInitiale().getPlateau()[coordObjectif[0]][coordObjectif[1]].isDirection(dir)) {
				propage(coordObjectif, dir, 1);
			}
		}
	}

	private void propage(int[] coord, Direction mouvement, int generation) {
		Case[][] plateauInitial = Modele.getInstance().getConfigInitiale().getPlateau();
		
		//Lorsque la generation dépasse les dimensions du plateau on arrete
		if(generation < (Modele.getInstance().getConfigInitiale().getxPlateau() * Modele.getInstance().getConfigInitiale().getyPlateau())){
			//Pour chaque case accessible
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
					if(((mouvement == Direction.EST) || (mouvement == Direction.OUEST)) && (mouvement == dir)) {
						if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isNord() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isSud()) {
							int nouvelleGeneration = generation + 1;
							propage(voisinCoord, Direction.NORD, nouvelleGeneration);
						}
						if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isSud() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isNord()) {
							int nouvelleGeneration = generation + 1;
							propage(voisinCoord, Direction.SUD, nouvelleGeneration);
						}
					}
					
					if(((mouvement == Direction.NORD) || (mouvement == Direction.SUD)) && (mouvement == dir)) {
						if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isOuest() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isEst()) {
							int nouvelleGeneration = generation + 1;
							propage(voisinCoord, Direction.OUEST, nouvelleGeneration);
						}
						if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isEst() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isOuest() ) {
							int nouvelleGeneration = generation + 1;
							propage(voisinCoord, Direction.EST, nouvelleGeneration);
						}
					}
				}
			}
			
			//On continue la propagation dans la direction donnee
			coutHeuristique[coord[0]][coord[1]] = generation;
			if(plateauInitial[coord[0]][coord[1]].isDirection(mouvement)){
				int[] suivantCoord = new int[2];
				suivantCoord[0] = coord[0] + mouvement.getMouvement()[0];
				suivantCoord[1] = coord[1] + mouvement.getMouvement()[1];
				propage(suivantCoord, mouvement, generation);
				//on incremente pas generation car dans ce cas le robot n'a pas a changer de direction
			}
		}
	}
	
	/**
	 * Methode pour simplifier l'acces a la configuration initiale
	 * 
	 * @return la configuration initiale
	 * @see Modele#getInstance()
	 */
	public Configuration getInit() {
		return Modele.getInstance().getConfigInitiale();
	}
	
	/**
	 * Methode permettant de recuperer l'item d'une liste avec le cout minimal
	 * 
	 * @param list La liste dans laquelle on cherche l'indice min
	 * @param couts tableau de cout qui permet de determiner la valeur minimale
	 * @return le couple de valeur avec le cout minimal
	 */
	public int[] minArray(ArrayList<int[]> list, int[][] couts) {
		int coutMin = couts[list.get(0)[0]][list.get(0)[1]];
		int[] noeudMin = Arrays.copyOf(list.get(0), 2);
		for(int[] noeud : list) {
			if(couts[noeud[0]][noeud[1]] < coutMin) {
				noeudMin = Arrays.copyOf(noeud, 2);
				coutMin = couts[noeud[0]][noeud[1]];
			}
		}
		
		return noeudMin;
	}
	
	/**
	 * Methode qui retourne une liste de voisin que l'on peux atteindre avec le robot 0 a partir d'une coordonnee donnee
	 * 
	 * @param noeud coordonnee du robot 0
	 * @return toutes les coordonnees que l'on peut atteindre
	 */
	public ArrayList<int[]> calculNoeudVoisins(int noeud[]) {
		ArrayList<int[]> suivants = new ArrayList<int[]>();
		Configuration noeudConfig = configVierge(noeud);
			
		for (Direction dir : Direction.values()) {
				
				int[] nouvellePosition = new int[2];
				nouvellePosition = Arrays.copyOf(noeudConfig.bougeJusqueObstacle(noeud, dir), 2);
				
				if(!Arrays.equals(nouvellePosition, noeud)){		
					suivants.add(nouvellePosition);
				}
		}
		return suivants;
	}
	
	/**
	 * Methode pour creer facilement une configuration a partir de la configuration de depart
	 * en ne changant que les coordonnees du robot cible
	 * 
	 * @param noeud nouvelle coordonnees du robot cible
	 * @return la configuration vierge
	 */
	public Configuration configVierge(int noeud[]) {
		Configuration nouvelleConfig = new Configuration(getInit());
		nouvelleConfig.getPlateau()[getInit().getPositionRobots()[0][0]][getInit().getPositionRobots()[0][0]].setRobot(false);
		nouvelleConfig.getPlateau()[noeud[0]][noeud[1]].setRobot(true);
		nouvelleConfig.getPositionRobots()[0] = noeud;
		
		return nouvelleConfig;
	}

	public int[][] getCost() {
		return coutHeuristique;
	}

	public void setCost(int[][] cost) {
		this.coutHeuristique = cost;
	}	
	
}
