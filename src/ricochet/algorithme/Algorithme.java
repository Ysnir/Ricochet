package ricochet.algorithme;

import java.util.ArrayList;
import java.util.Random;

import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;

public class Algorithme {
	
	private Modele modele;
	private Resolution methodeResolution;
	
	/**
	 * Constructeur de la classe.
	 * 
	 * @param m instance du modele
	 */
	public Algorithme(Modele m) {
		this.modele = m;
	}
	
	/**
	 * Accesseur de la methode de resolution courante
	 * 
	 * @return instance de la methode de résolution
	 */
	public Resolution getMethodeResolution() {
		return methodeResolution;
	}

	/**
	 * mutateur permettant de changer la methode de resolution utilisee
	 * 
	 * @param methodeResolution nouvelle methode de resolution
	 */
	public void setMethodeResolution(Resolution methodeResolution) {
		this.methodeResolution = methodeResolution;
	}

	/**
	 * genere la configuration initiale du plateau avant resolution.
	 * Transmet les infos de la vue au modele en appelant la methode setConfigInitiale.
	 * 
	 * @see Modele#setConfigInitiale(Configuration)
	 * @param x largeur du plateau
	 * @param y longeur du plateau
	 * @param nbRobot nombre de robot
	 * @param posRobot tableau de coordonnee des robots
	 * @param posObj coordonnee de l'objectif
	 * @param murs tableau contenant les "murs" du plateau
	 */
	public void genererConfig(int x, int y, int[][] posRobot, int[] posObj, Case[][] murs) {
		modele.setConfigInitiale(new Configuration(x, y, posRobot, posObj, murs, 0));
	}
	
	public void genererConfigAleatoire25() {
		int[][] posRobotAleat = new int[4][2];
		int[] posObjAleat = new int[2];
		Case[][] mursAleat = new Case[5][5];
		Random rand = new Random();
		
		//On genere des position aleatoires distinctes pour les differents robots
		do {
			posRobotAleat[0][0] = rand.nextInt(5);
			posRobotAleat[0][1] = rand.nextInt(5);
			
			posRobotAleat[1][0] = rand.nextInt(5);
			posRobotAleat[1][1] = rand.nextInt(5);
			
			posRobotAleat[2][0] = rand.nextInt(5);
			posRobotAleat[2][1] = rand.nextInt(5);
			
			posRobotAleat[3][0] = rand.nextInt(5);
			posRobotAleat[3][1] = rand.nextInt(5);
		} while	(		(posRobotAleat[0][0] == posRobotAleat[1][0] && posRobotAleat[0][1] == posRobotAleat[1][1]) ||
						(posRobotAleat[0][0] == posRobotAleat[2][0] && posRobotAleat[0][1] == posRobotAleat[2][1]) ||
						(posRobotAleat[0][0] == posRobotAleat[3][0] && posRobotAleat[0][1] == posRobotAleat[3][1]) ||
						
						(posRobotAleat[1][0] == posRobotAleat[2][0] && posRobotAleat[1][1] == posRobotAleat[2][1]) ||
						(posRobotAleat[1][0] == posRobotAleat[3][0] && posRobotAleat[1][1] == posRobotAleat[3][1]) ||
						
						(posRobotAleat[2][0] == posRobotAleat[3][0] && posRobotAleat[2][1] == posRobotAleat[3][1])
				);
		
		
		//On genere des position distinctes pour les 6 murs perpendiculaires au bordures du plateau
		int murBordureNord;
		int murBordureSud;
		int[] murBordureEst = new int[2];
		int[] murBordureOuest = new int[2];
		
		do {
			murBordureNord = rand.nextInt(2) + 1;
			
			murBordureSud = rand.nextInt(2) + 1;
			
			murBordureEst[0] = rand.nextInt(3) + 1;
			murBordureEst[1] = rand.nextInt(3) + 1;
			
			murBordureOuest[0] = rand.nextInt(3) + 1;
			murBordureOuest[1] = rand.nextInt(3) + 1;
		} while	(
						(Math.abs(murBordureEst[0] - murBordureEst[1]) < 2) ||
						(Math.abs(murBordureOuest[0] - murBordureOuest[1]) < 2)
				);
		
		
		//On genere une position distincte de celle des robots pour l'objectif dans une des case de la bordure
		do {
			int cote = rand.nextInt(7);
			switch(cote) {
				case 0:
					posObjAleat[0] = murBordureNord;
					posObjAleat[1] = 0; 
					break;
				case 1:
					posObjAleat[0] = murBordureSud;
					posObjAleat[1] = 4; 
					break;
				case 2:
					posObjAleat[0] = 4;
					posObjAleat[1] = murBordureEst[0]; 
					break;
				case 3:
					posObjAleat[0] = 4;
					posObjAleat[1] = murBordureEst[1]; 
					break;
				case 4:
					posObjAleat[0] = 0;
					posObjAleat[1] = murBordureOuest[0]; 
					break;
				case 5:
					posObjAleat[0] = 0;
					posObjAleat[1] = murBordureOuest[1]; 
					break;
				case 6:
					posObjAleat[0] = 2;
					posObjAleat[1] = 2;
			}
		} while	(		(posObjAleat[0] == posRobotAleat[0][0] && posObjAleat[1] == posRobotAleat[0][1]) ||
						(posObjAleat[0] == posRobotAleat[1][0] && posObjAleat[1] == posRobotAleat[1][1]) ||
						(posObjAleat[0] == posRobotAleat[2][0] && posObjAleat[1] == posRobotAleat[2][1]) ||
						(posObjAleat[0] == posRobotAleat[3][0] && posObjAleat[1] == posRobotAleat[3][1])
				);
		
		
		//On construit le plateau
		for(int i=0; i<5; i ++) {
			for(int j=0; j<5; j ++) {
				mursAleat[j][i] = new Case();
				mursAleat[j][i].setMurs(true, true, true, true);
			}
		}
		
		//On ajoute les elements genere au prealable
		for(int i=0; i<5; i ++) {
			for(int j=0; j<5; j ++) {
				
				//On place les bordures
				if(j == 0) {
					mursAleat[j][i].setOuest(false);
					if(i == murBordureOuest[0] || i == murBordureOuest[1]){
						mursAleat[j][i].setSud(false);
						mursAleat[j][i+1].setNord(false);
					}
				}
				if(j == 4) {
					mursAleat[j][i].setEst(false);
					if(i == murBordureEst[0] || i == murBordureEst[1]){
						mursAleat[j][i].setSud(false);
						mursAleat[j][i+1].setNord(false);
					}
				}
				if(i == 0) {
					mursAleat[j][i].setNord(false);
					if(j == murBordureNord){
						mursAleat[j][i].setEst(false);
						mursAleat[j+1][i].setOuest(false);
					}
				}
				if(i == 4) {
					mursAleat[j][i].setSud(false);
					if(j == murBordureSud){
						mursAleat[j][i].setEst(false);
						mursAleat[j+1][i].setOuest(false);
					}
				}
				//Le coin central
				if(i == 2 && j == 2) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}
				
				//On place les robots
				if(j == posRobotAleat[0][0] && i == posRobotAleat[0][1]) {
					mursAleat[j][i].setRobot(true);
				}
				if(j == posRobotAleat[1][0] && i == posRobotAleat[1][1]) {
					mursAleat[j][i].setRobot(true);
				}
				if(j == posRobotAleat[2][0] && i == posRobotAleat[2][1]) {
					mursAleat[j][i].setRobot(true);
				}
				if(j == posRobotAleat[3][0] && i == posRobotAleat[3][1]) {
					mursAleat[j][i].setRobot(true);
				}
			}
		}
		
		genererConfig(5, 5, posRobotAleat, posObjAleat, mursAleat);
	}
	
	/**
	 * Genere une configuration aleatoire correspondant a un plateau classique : 16x16
	 */
	public void genererConfigAleatoire256() {
		int[][] posRobotAleat = new int[4][2];
		int[] posObjAleat = new int[2];
		Case[][] mursAleat = new Case[16][16];
		Random rand = new Random();
		
		//On genere des position aleatoires distinctes pour les differents robots
		do {
			posRobotAleat[0][0] = rand.nextInt(16);
			posRobotAleat[0][1] = rand.nextInt(16);
			
			posRobotAleat[1][0] = rand.nextInt(16);
			posRobotAleat[1][1] = rand.nextInt(16);
			
			posRobotAleat[2][0] = rand.nextInt(16);
			posRobotAleat[2][1] = rand.nextInt(16);
			
			posRobotAleat[3][0] = rand.nextInt(16);
			posRobotAleat[3][1] = rand.nextInt(16);
		} while	(		(posRobotAleat[0][0] == posRobotAleat[1][0] && posRobotAleat[0][1] == posRobotAleat[1][1]) ||
						(posRobotAleat[0][0] == posRobotAleat[2][0] && posRobotAleat[0][1] == posRobotAleat[2][1]) ||
						(posRobotAleat[0][0] == posRobotAleat[3][0] && posRobotAleat[0][1] == posRobotAleat[3][1]) ||
						
						(posRobotAleat[1][0] == posRobotAleat[2][0] && posRobotAleat[1][1] == posRobotAleat[2][1]) ||
						(posRobotAleat[1][0] == posRobotAleat[3][0] && posRobotAleat[1][1] == posRobotAleat[3][1]) ||
						
						(posRobotAleat[2][0] == posRobotAleat[3][0] && posRobotAleat[2][1] == posRobotAleat[3][1])
				);
		
		
		//On genere des position distinctes pour les 8 murs perpendiculaires au bordures du plateau
		int[] murBordureNord = new int[2];
		int[] murBordureSud = new int[2];
		int[] murBordureEst = new int[2];
		int[] murBordureOuest = new int[2];
		
		do {
			murBordureNord[0] = rand.nextInt(13) + 1;
			murBordureNord[1] = rand.nextInt(13) + 1;
			
			murBordureSud[0] = rand.nextInt(13) + 1;
			murBordureSud[1] = rand.nextInt(13) + 1;
			
			murBordureEst[0] = rand.nextInt(13) + 1;
			murBordureEst[1] = rand.nextInt(13) + 1;
			
			murBordureOuest[0] = rand.nextInt(13) + 1;
			murBordureOuest[1] = rand.nextInt(13) + 1;
		} while	(		(Math.abs(murBordureNord[0] - murBordureNord[1]) < 2) ||
						(Math.abs(murBordureSud[0] - murBordureSud[1]) < 2) ||
						(Math.abs(murBordureEst[0] - murBordureEst[1]) < 2) ||
						(Math.abs(murBordureOuest[0] - murBordureOuest[1]) < 2)
				);
		
		//On genere une position distincte de celle des robots pour l'objectif dans une des case de la bordure
		do {
			int cote = rand.nextInt(7);
			switch(cote) {
				case 0:
					posObjAleat[0] = murBordureNord[0];
					posObjAleat[1] = 0; 
					break;
				case 1:
					posObjAleat[0] = murBordureNord[1];
					posObjAleat[1] = 0; 
					break;
				case 2:
					posObjAleat[0] = murBordureSud[0];
					posObjAleat[1] = 15; 
					break;
				case 3:
					posObjAleat[0] = murBordureSud[1];
					posObjAleat[1] = 15; 
					break;
				case 4:
					posObjAleat[0] = 15;
					posObjAleat[1] = murBordureEst[0]; 
					break;
				case 5:
					posObjAleat[0] = 15;
					posObjAleat[1] = murBordureEst[1]; 
					break;
				case 6:
					posObjAleat[0] = 0;
					posObjAleat[1] = murBordureOuest[0]; 
					break;
				case 7:
					posObjAleat[0] = 0;
					posObjAleat[1] = murBordureOuest[1]; 
					break;
			}
		} while	(		(posObjAleat[0] == posRobotAleat[0][0] && posObjAleat[1] == posRobotAleat[0][1]) ||
						(posObjAleat[0] == posRobotAleat[1][0] && posObjAleat[1] == posRobotAleat[1][1]) ||
						(posObjAleat[0] == posRobotAleat[2][0] && posObjAleat[1] == posRobotAleat[2][1]) ||
						(posObjAleat[0] == posRobotAleat[3][0] && posObjAleat[1] == posRobotAleat[3][1])
				);
		
		//On va generer des coordonnees aleatoire pour les 16 "coins" de mur du centre du plateau:
		int[][] quartNordOuest = new int[4][2];
		int[][] quartNordEst = new int[4][2];
		int[][] quartSudOuest = new int[4][2];
		int[][] quartSudEst = new int[4][2];
		
		//Le quart Nord-Ouest, ...
		do {
			quartNordOuest[0][0] = rand.nextInt(6) + 1;
			quartNordOuest[0][1] = rand.nextInt(6) + 1;
			
			quartNordOuest[1][0] = rand.nextInt(6) + 1;
			quartNordOuest[1][1] = rand.nextInt(6) + 1;
			
			quartNordOuest[2][0] = rand.nextInt(6) + 1;
			quartNordOuest[2][1] = rand.nextInt(6) + 1;
			
			quartNordOuest[3][0] = rand.nextInt(6) + 1;
			quartNordOuest[3][1] = rand.nextInt(6) + 1;
		} while	(		(Math.abs(quartNordOuest[0][0] - quartNordOuest[1][0]) < 1) || (Math.abs(quartNordOuest[0][1] - quartNordOuest[1][1]) < 1) ||
						(Math.abs(quartNordOuest[0][0] - quartNordOuest[2][0]) < 1) || (Math.abs(quartNordOuest[0][1] - quartNordOuest[2][1]) < 1) ||
						(Math.abs(quartNordOuest[0][0] - quartNordOuest[3][0]) < 1) || (Math.abs(quartNordOuest[0][1] - quartNordOuest[3][1]) < 1) ||
				
						(Math.abs(quartNordOuest[1][0] - quartNordOuest[2][0]) < 1) || (Math.abs(quartNordOuest[1][1] - quartNordOuest[2][1]) < 1) ||
						(Math.abs(quartNordOuest[1][0] - quartNordOuest[3][0]) < 1) || (Math.abs(quartNordOuest[1][1] - quartNordOuest[3][1]) < 1) ||
				
						(Math.abs(quartNordOuest[2][0] - quartNordOuest[3][0]) < 1) || (Math.abs(quartNordOuest[2][1] - quartNordOuest[3][1]) < 1)
				);
		//...Nord-Est, ...
		do {
			quartNordEst[0][0] = rand.nextInt(7) + 8;
			quartNordEst[0][1] = rand.nextInt(6) + 1;
			
			quartNordEst[1][0] = rand.nextInt(7) + 8;
			quartNordEst[1][1] = rand.nextInt(6) + 1;
			
			quartNordEst[2][0] = rand.nextInt(7) + 8;
			quartNordEst[2][1] = rand.nextInt(6) + 1;
			
			quartNordEst[3][0] = rand.nextInt(7) + 8;
			quartNordEst[3][1] = rand.nextInt(6) + 1;
		} while	(		(Math.abs(quartNordEst[0][0] - quartNordEst[1][0]) < 1) || (Math.abs(quartNordEst[0][1] - quartNordEst[1][1]) < 1) ||
						(Math.abs(quartNordEst[0][0] - quartNordEst[2][0]) < 1) || (Math.abs(quartNordEst[0][1] - quartNordEst[2][1]) < 1) ||
						(Math.abs(quartNordEst[0][0] - quartNordEst[3][0]) < 1) || (Math.abs(quartNordEst[0][1] - quartNordEst[3][1]) < 1) ||
		
						(Math.abs(quartNordEst[1][0] - quartNordEst[2][0]) < 1) || (Math.abs(quartNordEst[1][1] - quartNordEst[2][1]) < 1) ||
						(Math.abs(quartNordEst[1][0] - quartNordEst[3][0]) < 1) || (Math.abs(quartNordEst[1][1] - quartNordEst[3][1]) < 1) ||
		
						(Math.abs(quartNordEst[2][0] - quartNordEst[3][0]) < 1) || (Math.abs(quartNordEst[2][1] - quartNordEst[3][1]) < 1)

				);
		
		//...Sud-Ouest et...
		do {
			quartSudOuest[0][0] = rand.nextInt(6) + 1;
			quartSudOuest[0][1] = rand.nextInt(7) + 8;
			
			quartSudOuest[1][0] = rand.nextInt(6) + 1;
			quartSudOuest[1][1] = rand.nextInt(7) + 8;
			
			quartSudOuest[2][0] = rand.nextInt(6) + 1;
			quartSudOuest[2][1] = rand.nextInt(7) + 8;
			
			quartSudOuest[3][0] = rand.nextInt(6) + 1;
			quartSudOuest[3][1] = rand.nextInt(7) + 8;
		} while	(		(Math.abs(quartSudOuest[0][0] - quartSudOuest[1][0]) < 1) || (Math.abs(quartSudOuest[0][1] - quartSudOuest[1][1]) < 1) ||
						(Math.abs(quartSudOuest[0][0] - quartSudOuest[2][0]) < 1) || (Math.abs(quartSudOuest[0][1] - quartSudOuest[2][1]) < 1) ||
						(Math.abs(quartSudOuest[0][0] - quartSudOuest[3][0]) < 1) || (Math.abs(quartSudOuest[0][1] - quartSudOuest[3][1]) < 1) ||
		
						(Math.abs(quartSudOuest[1][0] - quartSudOuest[2][0]) < 1) || (Math.abs(quartSudOuest[1][1] - quartSudOuest[2][1]) < 1) ||
						(Math.abs(quartSudOuest[1][0] - quartSudOuest[3][0]) < 1) || (Math.abs(quartSudOuest[1][1] - quartSudOuest[3][1]) < 1) ||
		
						(Math.abs(quartSudOuest[2][0] - quartSudOuest[3][0]) < 1) || (Math.abs(quartSudOuest[2][1] - quartSudOuest[3][1]) < 1)

				);
		//...Sud-Est.
		do {
			quartSudEst[0][0] = rand.nextInt(7) + 8;
			quartSudEst[0][1] = rand.nextInt(7) + 8;
			
			quartSudEst[1][0] = rand.nextInt(7) + 8;
			quartSudEst[1][1] = rand.nextInt(7) + 8;
			
			quartSudEst[2][0] = rand.nextInt(7) + 8;
			quartSudEst[2][1] = rand.nextInt(7) + 8;
			
			quartSudEst[3][0] = rand.nextInt(7) + 8;
			quartSudEst[3][1] = rand.nextInt(7) + 8;
		} while	(		(Math.abs(quartSudEst[0][0] - quartSudEst[1][0]) < 1) || (Math.abs(quartSudEst[0][1] - quartSudEst[1][1]) < 1) ||
						(Math.abs(quartSudEst[0][0] - quartSudEst[2][0]) < 1) || (Math.abs(quartSudEst[0][1] - quartSudEst[2][1]) < 1) ||
						(Math.abs(quartSudEst[0][0] - quartSudEst[3][0]) < 1) || (Math.abs(quartSudEst[0][1] - quartSudEst[3][1]) < 1) ||
		
						(Math.abs(quartSudEst[1][0] - quartSudEst[2][0]) < 1) || (Math.abs(quartSudEst[1][1] - quartSudEst[2][1]) < 1) ||
						(Math.abs(quartSudEst[1][0] - quartSudEst[3][0]) < 1) || (Math.abs(quartSudEst[1][1] - quartSudEst[3][1]) < 1) ||
		
						(Math.abs(quartSudEst[2][0] - quartSudEst[3][0]) < 1) || (Math.abs(quartSudEst[2][1] - quartSudEst[3][1]) < 1)

				);
		
		//On construit le plateau
		for(int i=0; i<16; i ++) {
			for(int j=0; j<16; j ++) {
				mursAleat[j][i] = new Case();
				mursAleat[j][i].setMurs(true, true, true, true);
			}
		}
		
		//On ajoute les elements genere au prealable
		for(int i=0; i<16; i ++) {
			for(int j=0; j<16; j ++) {
				
				//On place les bordures
				if(j == 0) {
					mursAleat[j][i].setOuest(false);
					if(i == murBordureOuest[0] || i == murBordureOuest[1]){
						mursAleat[j][i].setSud(false);
						mursAleat[j][i+1].setNord(false);
					}
				}
				if(j == 15) {
					mursAleat[j][i].setEst(false);
					if(i == murBordureEst[0] || i == murBordureEst[1]){
						mursAleat[j][i].setSud(false);
						mursAleat[j][i+1].setNord(false);
					}
				}
				if(i == 0) {
					mursAleat[j][i].setNord(false);
					if(j == murBordureNord[0] || j == murBordureNord[1]){
						mursAleat[j][i].setEst(false);
						mursAleat[j+1][i].setOuest(false);
					}
				}
				if(i == 15) {
					mursAleat[j][i].setSud(false);
					if(j == murBordureSud[0] || j == murBordureSud[1]){
						mursAleat[j][i].setEst(false);
						mursAleat[j+1][i].setOuest(false);
					}
				}
				
				//On place les coins
				//Nord-Ouest :
				if(j == quartNordOuest[0][0] && i == quartNordOuest[0][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
						case 0:
							mursAleat[j][i].setMurs(false, true, true, false);
							mursAleat[j][i-1].setSud(false);
							mursAleat[j-1][i].setEst(false);
							break;
						case 1:
							mursAleat[j][i].setMurs(false, true, false, true);
							mursAleat[j][i-1].setSud(false);
							mursAleat[j+1][i].setOuest(false);
							break;
						case 2:
							mursAleat[j][i].setMurs(true, false, true, false);
							mursAleat[j][i+1].setNord(false);
							mursAleat[j-1][i].setEst(false);
							break;
						case 3:
							mursAleat[j][i].setMurs(true, false, false, true);
							mursAleat[j][i+1].setNord(false);
							mursAleat[j+1][i].setOuest(false);
							break;
					}
				}
				if(j == quartNordOuest[1][0] && i == quartNordOuest[1][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}	
				if(j == quartNordOuest[2][0] && i == quartNordOuest[2][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}				
				if(j == quartNordOuest[3][0] && i == quartNordOuest[3][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}
				
				//Nord-Est :
				if(j == quartNordEst[0][0] && i == quartNordEst[0][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}
				if(j == quartNordEst[1][0] && i == quartNordEst[1][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}	
				if(j == quartNordEst[2][0] && i == quartNordEst[2][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}				
				if(j == quartNordEst[3][0] && i == quartNordEst[3][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}
				
				//Sud-Ouest :
				if(j == quartSudOuest[0][0] && i == quartSudOuest[0][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}
				if(j == quartSudOuest[1][0] && i == quartSudOuest[1][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}	
				if(j == quartSudOuest[2][0] && i == quartSudOuest[2][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}				
				if(j == quartSudOuest[3][0] && i == quartSudOuest[3][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}
				
				//Sud-Est
				if(j == quartSudEst[0][0] && i == quartSudEst[0][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}
				if(j == quartSudEst[1][0] && i == quartSudEst[1][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}	
				if(j == quartSudEst[2][0] && i == quartSudEst[2][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}				
				if(j == quartSudEst[3][0] && i == quartSudEst[3][1]) {
					int coin;
					coin  = rand.nextInt(4);
					switch(coin) {
					case 0:
						mursAleat[j][i].setMurs(false, true, true, false);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 1:
						mursAleat[j][i].setMurs(false, true, false, true);
						mursAleat[j][i-1].setSud(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					case 2:
						mursAleat[j][i].setMurs(true, false, true, false);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j-1][i].setEst(false);
						break;
					case 3:
						mursAleat[j][i].setMurs(true, false, false, true);
						mursAleat[j][i+1].setNord(false);
						mursAleat[j+1][i].setOuest(false);
						break;
					}
				}
				
				//On place les robots
				if(j == posRobotAleat[0][0] && i == posRobotAleat[0][1]) {
					mursAleat[j][i].setRobot(true);
				}
				if(j == posRobotAleat[1][0] && i == posRobotAleat[1][1]) {
					mursAleat[j][i].setRobot(true);
				}
				if(j == posRobotAleat[2][0] && i == posRobotAleat[2][1]) {
					mursAleat[j][i].setRobot(true);
				}
				if(j == posRobotAleat[3][0] && i == posRobotAleat[3][1]) {
					mursAleat[j][i].setRobot(true);
				}
			}
		}
		
		genererConfig(16, 16, posRobotAleat, posObjAleat, mursAleat);
	}
	
	/**
	 * Resout le la configuration initiale avec la methode de resolution courante. 
	 * Renvoie la liste des etat par lequel le plateau passe pour etre resolut.
	 * Appel la methode run de la methode de resolution courante (specifique a chaque maniere de resoudre le probleme).
	 * 
	 * @see Resolution#run()
	 * @return une liste de configuration reprenant le "chemin" des configuration jusqu'a l'arrive du robot 0 a l'objectif
	 */
	public void resoudre() {
		double debutNano = System.nanoTime();
		modele.setParcours(methodeResolution.lancer());
		double tempsEcouleNano = System.nanoTime() - debutNano;
		
		modele.setTempsExec(tempsEcouleNano / 1000000000.0);
	}
	
}
