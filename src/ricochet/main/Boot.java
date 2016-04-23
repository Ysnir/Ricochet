package ricochet.main;

import java.util.ArrayList;

import javax.swing.JFrame;

import ricochet.algorithme.Algorithme;
import ricochet.algorithme.IDDFS;
import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;
import ricochet.vue.Vue;

public class Boot {

	public static void main(String[] args) {

		
		//config test 1
		int[][] posRobot = new int[4][2];
		posRobot[0][0] = 2;
		posRobot[0][1] = 2;
		posRobot[1][0] = 1;
		posRobot[1][1] = 0;
		posRobot[2][0] = 1;
		posRobot[2][1] = 1;
		posRobot[3][0] = 2;
		posRobot[3][1] = 1;

		int[] posObj = new int[2];
		posObj[0] = 0;
		posObj[1] = 0;
		Case[][] murs = new Case[3][3];
		murs[0][0] = new Case(false, false, true, false, false);
		murs[0][1] = new Case(false, true, true, false, false);
		murs[0][2] = new Case(true, false, false, false, false);
		murs[1][0] = new Case(false, true, true, true, true);
		murs[1][1] = new Case(true, true, true, true, true);
		murs[1][2] = new Case(true, false, true, false, false);
		murs[2][0] = new Case(false, true, false, true, false);
		murs[2][1] = new Case(true, false, false, true, true);
		murs[2][2] = new Case(false, false, false, true, true);
		
		Algorithme algo = new Algorithme(Modele.getInstance());
		algo.genererConfig(3, 3, posRobot, posObj, murs);
		Vue v = new Vue(algo);
		

		//Affichage Heuristique des couloirs
		/**
		System.out.println(Modele.getInstance().getConfigInitiale());
		algo.setMethodeResolution(new AStar());
		int[][] coutHeuristique = ((AStar)algo.getMethodeResolution()).couloirsHeuristique(Modele.getInstance().getConfigInitiale());
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				System.out.print(coutHeuristique[j][i] + " ");
			}
			System.out.println();
		}*/

		//Affichage parcours
		algo.setMethodeResolution(new IDDFS());
		ArrayList<Configuration> chemin = new ArrayList<Configuration>();
		algo.resoudre();
		
		v.dessineParcours();
		
		v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		v.setVisible(true);
		v.pack();
		v.setTitle("Ricochet Robot");
		
		
	}

}
