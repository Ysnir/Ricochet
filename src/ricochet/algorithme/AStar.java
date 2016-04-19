package ricochet.algorithme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;
import ricochet.utilitaire.Direction;

public class AStar implements Resolution {
	
	public ArrayList<Configuration> lancer() {
		List<int[]> dejaVu = new ArrayList<int[]>();
		List<int[]> aExplorer = new ArrayList<int[]>();
		aExplorer.add(getInit().getPositionRobots()[0]);
		HashMap<Configuration, Configuration> vientDe = new HashMap();
		
		int[][] coutHeuristique = couloirsHeuristique(getInit());
		int[] robotCible = getInit().getPositionRobots()[0];
		
		while(coutHeuristique[robotCible[0]][robotCible[1]] == Integer.MAX_VALUE) {
			Configuration configMin = new Configuration(getInit());
			int[][] coutMin = new int[getInit().getxPlateau()][getInit().getyPlateau()];
			for (int[] row: coutMin) {
			    Arrays.fill(row, Integer.MAX_VALUE);
			}
			int[] nouvellePosition, anciennePosition = new int[2];
			int[][] coutCourant = new int[getInit().getxPlateau()][getInit().getyPlateau()];
			
			for(int i=1; i<getInit().getPositionRobots().length; i++) {
				for(Direction dir : Direction.values()) {
					Configuration config = new Configuration(getInit());
					
					nouvellePosition = config.bougeJusqueObstacle(config.getPositionRobots()[i], dir);
					anciennePosition = config.getPositionRobots()[i];
					if(!Arrays.equals(nouvellePosition, anciennePosition)) {
						config.getPositionRobots()[i] = nouvellePosition;
						
						
							config.getPlateau()[nouvellePosition[0]][nouvellePosition[1]].setRobot(true);				
							config.getPlateau()[anciennePosition[0]][anciennePosition[1]].setRobot(false);
						
						
						coutCourant = couloirsHeuristique(config);
						
						if(coutTotal(coutMin) > coutTotal(coutCourant) || coutMin[robotCible[0]][robotCible[1]] > coutCourant[robotCible[0]][robotCible[1]]) {
							coutMin = coutCourant;
							configMin = config;
						}	
					}
				}
			}
			coutHeuristique = coutMin;
			Configuration ancienneConfig = new Configuration(getInit());
			getInit().setPositionRobots(configMin.getPositionRobots());
			getInit().setPlateau(configMin.getPlateau());

			vientDe.put(getInit(), ancienneConfig);
		}
		
		//Le cout pour atteindre un noeud depuis le debut ainsi que celui pour atteindre l'objectif en passant par ce noeud
		int[][] coutAtteindre = new int[getInit().getxPlateau()][getInit().getyPlateau()];
		int[][] coutTraverser = new int[getInit().getxPlateau()][getInit().getyPlateau()];
		
		for (int[] row: coutAtteindre) {
		    Arrays.fill(row, Integer.MAX_VALUE);
		}
		//le cout pour aller du depart au depart vaut 0
		coutAtteindre[getInit().getPositionRobots()[0][0]][getInit().getPositionRobots()[0][1]] = 0;
		
		for (int[] row: coutTraverser) {
		    Arrays.fill(row, Integer.MAX_VALUE);
		}
		//On recupere la valeur approximee pour atteindre l'objectif depuis le depart
		coutTraverser[getInit().getPositionRobots()[0][0]][getInit().getPositionRobots()[0][1]] = coutHeuristique[getInit().getPositionRobots()[0][0]][getInit().getPositionRobots()[0][1]];

		while(!aExplorer.isEmpty()){			
			int[] courant = minArray(aExplorer, coutTraverser);
			
			if(Arrays.equals(courant, getInit().getPositionObjectif())) {
				/*for(Map.Entry<Configuration, Configuration> entry : vientDe.entrySet()) {
					System.out.println("clef : "+entry.getKey()+"\nvaleur : "+entry.getValue());
				}*/
				return reconstruireChemin(vientDe, configVierge(0, courant));
			}
			
			removeArray(aExplorer, courant);
			dejaVu.add(courant);
			
			//On parcourt chaque voisin de courant
			for(int[] voisin : calculNoeudVoisins(courant)) {
				if(containsArray(dejaVu, voisin)) {
					continue;
				}
				int raccourcis = coutAtteindre[courant[0]][courant[1]] + 1;
				
				if(!containsArray(aExplorer, voisin)) {
					aExplorer.add(voisin);
				} else if(raccourcis >= coutAtteindre[voisin[0]][voisin[1]]) {//Le nouveau chemin est pire que l'ancien
					continue;
				}
				/*System.out.println("cour : "+configVierge(0,courant));
				System.out.println("vois : "+configVierge(0,voisin));*/
				vientDe.put(configVierge(0,voisin), configVierge(0, courant));
				coutAtteindre[voisin[0]][voisin[1]] = raccourcis;
				coutTraverser[voisin[0]][voisin[1]] = coutAtteindre[voisin[0]][voisin[1]] + coutHeuristique[voisin[0]][voisin[1]];
			}
		}

		return null;
	}
	
	/**
	 * Methode permettant de retrouver le chemin que parcours le robot a l'aide d'un tableau associatif conservant l'antecedance d'un plateau
	 * 
	 * @param vientDe tableau associatif qui a pour clef une coordonnee et pour valeur sa configuration mere
	 * @param origine debut du parcours
	 * @return
	 */
	private ArrayList<Configuration> reconstruireChemin(HashMap<Configuration, Configuration> vientDe, Configuration origine) {
		ArrayList<Configuration> chemin = new ArrayList<Configuration>();
		chemin.add(origine);
		Configuration courant = origine;
		
		while(vientDe.containsKey(courant)) {
			courant = vientDe.get(courant);
			chemin.add(courant);
		}
		return chemin;
	}

	/**
	 * Methode pour creer facilement une configuration a partir de la configuration de depart
	 * en ne changant que les coordonnees d'un seul robot
	 * 
	 * @param noeud nouvelle coordonnees du robot choisit
	 * @param robot le robot que l'on veut deplacer
	 * @return la nouvelle configuration
	 */
	public Configuration configVierge(int robot, int noeud[]) {
		Configuration nouvelleConfig = new Configuration(getInit());
		nouvelleConfig.getPlateau()[getInit().getPositionRobots()[robot][0]][getInit().getPositionRobots()[robot][1]].setRobot(false);
		nouvelleConfig.getPlateau()[noeud[0]][noeud[1]].setRobot(true);
		nouvelleConfig.getPositionRobots()[robot] = noeud;
		return nouvelleConfig;
	}
	
	/**
	 * Methode qui retourne une liste de voisin que l'on peux atteindre avec le robot 0 a partir d'une coordonnee donnee
	 * 
	 * @param noeud coordonnee du robot 0
	 * @return toutes les coordonnees que l'on peut atteindre
	 */
	public ArrayList<int[]> calculNoeudVoisins(int noeud[]) {
		ArrayList<int[]> suivants = new ArrayList<int[]>();
		Configuration noeudConfig = configVierge(0, noeud);
			
		for (Direction dir : Direction.values()) {
				
				int[] nouvellePosition = new int[2];
				nouvellePosition = noeudConfig.bougeJusqueObstacle(noeud, dir);
				
				if(!Arrays.equals(nouvellePosition, noeud)){		
					suivants.add(nouvellePosition);
				}
		}
		return suivants;
	}
	
	public int[][] couloirsHeuristique(Configuration c) {
		int[][] cout = new int[getInit().getxPlateau()][getInit().getxPlateau()];
		//On rempli le cout avec -1 pour signifier -infini
		for (int[] row: cout) {
		    Arrays.fill(row, Integer.MAX_VALUE);
		}

		int[] coordObjectif = Arrays.copyOf(c.getPositionObjectif(), 2);
		for(Direction dir : Direction.values()) {
			if(c.getPlateau()[coordObjectif[0]][coordObjectif[1]].isAccessible(dir)) {
				propage(c, cout, coordObjectif, dir, 1);
			}
		}
		return cout;
	}

	private void propage(Configuration c, int[][] cout, int[] coord, Direction mouvement, int generation) {
		Case[][] plateauInitial = c.getPlateau();
		
		//Lorsque la generation dépasse les dimensions du plateau on arrete
		if(generation < (c.getxPlateau() * c.getyPlateau())){
			//Pour chaque case accessible
			for(Direction dir : Direction.values()) {
				if(plateauInitial[coord[0]][coord[1]].isAccessible(dir)) {
					
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
							propage(c, cout, voisinCoord, Direction.NORD, nouvelleGeneration);
						}
						if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isSud() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isNord()) {
							int nouvelleGeneration = generation + 1;
							propage(c, cout, voisinCoord, Direction.SUD, nouvelleGeneration);
						}
					}
					
					if(((mouvement == Direction.NORD) || (mouvement == Direction.SUD)) && (mouvement == dir)) {
						if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isOuest() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isEst()) {
							int nouvelleGeneration = generation + 1;
							propage(c, cout, voisinCoord, Direction.OUEST, nouvelleGeneration);
						}
						if(plateauInitial[voisinCoord[0]][voisinCoord[1]].isEst() && !plateauInitial[voisinCoord[0]][voisinCoord[1]].isOuest() ) {
							int nouvelleGeneration = generation + 1;
							propage(c, cout, voisinCoord, Direction.EST, nouvelleGeneration);
						}
					}
				}
			}
			
			//On continue la propagation dans la direction donnee
			cout[coord[0]][coord[1]] = generation;
			if(plateauInitial[coord[0]][coord[1]].isAccessible(mouvement)){
				int[] suivantCoord = new int[2];
				suivantCoord[0] = coord[0] + mouvement.getMouvement()[0];
				suivantCoord[1] = coord[1] + mouvement.getMouvement()[1];
				propage(c, cout, suivantCoord, mouvement, generation);
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
	 * Methode permettant de recuperer la coordonnee d'une liste avec le cout minimal
	 * 
	 * @param liste La liste dans laquelle on cherche l'element de cout minimal
	 * @param couts tableau de cout.
	 * @return le couple de valeur avec le cout minimal
	 */
	public int[] minArray(List<int[]> liste, int[][] couts) {
		int[] noeudMin = liste.get(0);
		int coutMin = couts[noeudMin[0]][noeudMin[1]];
		for(int[] noeud : liste) {
			if(couts[noeud[0]][noeud[1]] < coutMin) {
				noeudMin = noeud;
				coutMin = couts[noeud[0]][noeud[1]];
			}
		}
		
		return noeudMin;
	}
	
	/**
	 * Methode qui permet de determiner si une coordonnee est presente dans une liste
	 * 
	 * @param liste liste a inspecter
	 * @param array coordonnee que l'on recherche
	 * @return un booleen
	 */
	public boolean containsArray(List<int[]> liste, int[] array){
		boolean resultat = false;
		for(int[] item : liste){
			if((item[0] == array[0]) && (item[1] == array[1])) {
				resultat = true;
			}
		}
		
		return resultat;
	}
	
	/**
	 * Methode qui permet de supprimer toute les occurence d'une coordonnee
	 * 
	 * @param liste liste 
	 * @param array coordonnee à supprimer
	 */
	public void removeArray(List<int[]> liste, int[] array) {
		List<int[]> copie = new ArrayList<int[]>(liste);
		
		for(int[] item : copie){
			if((item[0] == array[0]) && (item[1] == array[1])) {
				liste.remove(item);
			}
		}
	}
	
	public long coutTotal(int[][] tableau) {
		long resultat = 0;
		for(int[] ligne : tableau) {
			for(int cellule : ligne) {
				resultat = resultat + cellule;
			}
		}
		return resultat;
	}
	
}
