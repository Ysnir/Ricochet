package ricochet.vue;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ricochet.algorithme.Algorithme;
import ricochet.modele.Case;
import ricochet.modele.Configuration;
import ricochet.modele.Modele;

@SuppressWarnings("serial")
public class Vue extends JFrame implements Observateur{
	
	protected Algorithme algo;
	protected JLabel[][] labels;
	
	public Vue(Algorithme algo) {
		this.algo = algo;
		this.labels = new JLabel[getInit().getxPlateau()][getInit().getyPlateau()];
		
	}
	
	public JPanel dessineConfiguration(Configuration c) {
		JPanel p = new JPanel();
		setResizable(false);
		p.setLayout(new GridLayout(c.getxPlateau(),c.getyPlateau(), 0, 0));
		p.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		for(int i=0; i<c.getxPlateau(); i++) {
			for(int j=0; j<c.getyPlateau(); j++) {
				BufferedImage buff;
				BufferedImage robot;
				try {
					
					String path = new String("images/");
					if(c.getPlateau()[j][i].isNord()) {
						path = path + "N";
					}
					if(c.getPlateau()[j][i].isSud()) {
						path = path + "S";
					}
					if(c.getPlateau()[j][i].isEst()) {
						path = path + "E";
					}
					if(c.getPlateau()[j][i].isOuest()) {
						path = path + "O";
					}
					path = path +".png";
	
					buff = ImageIO.read(new File(path));
					for(int k=0; k<c.getPositionRobots().length; k++) {
						if(c.getPositionRobots()[k][0] == j && c.getPositionRobots()[k][1] == i) {
							robot = ImageIO.read(new File("images/"+k+".png"));
							Graphics g = buff.getGraphics();
							g.drawImage(robot, 20, 20, null);
						}
					}
					
					ImageIcon image = new ImageIcon(buff);
					labels[j][i] = new JLabel(image);
					p.add(labels[j][i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			
		}
		return p;
	}
	
	public void dessineParcours() {
		JPanel p;
		setResizable(false);
		setLayout(new FlowLayout());
		for(int i=Modele.getInstance().getParcours().size()-1;i>=0; i--) {
			p = this.dessineConfiguration(Modele.getInstance().getParcours().get(i));
			add(p);
		}
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
		
		algo.genererConfig(x, y, posRobot, posObj, murs);
	}
	
	public Configuration getInit() {
		return Modele.getInstance().getConfigInitiale();
	}
	
	public void actualise() {

	}

}
