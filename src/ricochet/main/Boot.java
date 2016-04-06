package ricochet.main;

import java.util.ArrayList;

import ricochet.algorithme.AStar;
import ricochet.algorithme.Algorithme;
import ricochet.algorithme.ParcoursLargeur;
import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;
import ricochet.vue.Vue;

public class Boot {

	public static void main(String[] args) {

		
		//config test 1
		int[][] posRobot = new int[1][2];
		posRobot[0][0] = 2;
		posRobot[0][1] = 2;
		int[] posObj = new int[2];
		posObj[0] = 0;
		posObj[1] = 0;
		Case[][] murs = new Case[3][3];
		murs[0][0] = new Case(false, false, true, false, false);
		murs[0][1] = new Case(false, true, true, false, false);
		murs[0][2] = new Case(true, false, false, false, false);
		murs[1][0] = new Case(false, true, true, true, false);
		murs[1][1] = new Case(true, true, true, true, false);
		murs[1][2] = new Case(true, false, true, false, false);
		murs[2][0] = new Case(false, true, false, true, false);
		murs[2][1] = new Case(true, false, false, true, false);
		murs[2][2] = new Case(false, false, false, true, true);
		
		Algorithme algo = new Algorithme(Modele.getInstance());
		Vue v = new Vue(algo);
		algo.genererConfig(3, 3, 1, posRobot, posObj, murs);
		
		//Affichage AStar
		/**
		algo.setMethodeResolution(new AStar());
		
		((AStar)algo.getMethodeResolution()).couloirsHeuristique();
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				System.out.print(((AStar)algo.getMethodeResolution()).getCost()[j][i]);
			}
			System.out.println();
		}*/
		
		//Affichage Parcours Largeur
		
		algo.setMethodeResolution(new ParcoursLargeur());
		System.out.println(Modele.getInstance().getConfigInitiale().toString());
		ArrayList<Configuration> chemin = new ArrayList<Configuration>();
		algo.resoudre();
		chemin = Modele.getInstance().getParcours();
		
		for(int i=chemin.size()-1; i>=0; i--) {
			System.out.println(chemin.get(i).toString());
		}
		
	}

}
