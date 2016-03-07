package ricochet.main;

import java.util.ArrayList;

import ricochet.algorithme.Algorithme;
import ricochet.algorithme.ParcoursLargeur;
import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;
import ricochet.vue.Vue;

public class Boot {

	public static void main(String[] args) {
		Algorithme algo = new Algorithme(Modele.getInstance());
		algo.setMethodeResolution(new ParcoursLargeur());
		Vue v = new Vue(algo);
		
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
		algo.genererConfig(3, 3, 1, posRobot, posObj, murs);
		
		System.out.println(Modele.getInstance().getConfigInitiale().toString());
		ArrayList<Configuration> path = new ArrayList<Configuration>();
		path = algo.resoudre();

		for(int i=path.size()-1; i>=0; i--) {
			System.out.println(path.get(i).toString());
		}
		
	}

}
