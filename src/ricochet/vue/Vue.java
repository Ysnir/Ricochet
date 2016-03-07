package ricochet.vue;

import java.util.Scanner;

import ricochet.algorithme.Algorithme;
import ricochet.modele.Case;

public class Vue implements Observateur{
	
	protected Algorithme algo;
	
	public Vue(Algorithme algo) {
		this.algo = algo;
	}
	
	public void scanConfig() {
		int x, y, nbRobot;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Nombre de ligne : ");
		x = sc.nextInt();
		System.out.println("Nombre de colonne : ");
		y = sc.nextInt();
		System.out.println("Nombre de robots : ");
		nbRobot = sc.nextInt();
		
		int[][] posRobot = new int[nbRobot][2];
		for(int i=0; i<nbRobot; i++) {
			System.out.println("position X du robot numero " + i + " : ");
			posRobot[i][0] = sc.nextInt();
			System.out.println("position Y du robot numero " + i + " : ");
			posRobot[i][1] = sc.nextInt();
		}
		
		int[] posObj = new int[2];
		System.out.println("Position X de l'objectif : ");
		posObj[0] = sc.nextInt();
		System.out.println("Position Y de l'objectif : ");
		posObj[1] = sc.nextInt();
		
		sc.nextLine();
		
		String str;
		Case[][] murs = new Case[x][y];
		for(int j=0; j<y; j++ ) {
			for(int k=0; k<x; k++) {
				System.out.println("Acces depuis la case de coordonnee "+k+","+j+" (N=Nord, S=Sud, E=Est, O=Ouest, R=Presence d'un robot) : ");
				str = sc.nextLine();
				Case c = new Case(str.contains("N"), str.contains("S"), str.contains("E"), str.contains("O"), str.contains("R"));
				murs[k][j] = c;
			}
		}
		
		algo.genererConfig(x, y, nbRobot, posRobot, posObj, murs);
	}

	public void actualise() {

	}

}
