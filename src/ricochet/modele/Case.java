package ricochet.modele;

import ricochet.utilitaire.Direction;

public class Case {
	private boolean nord;
	private boolean sud;
	private boolean est;
	private boolean ouest;
	private boolean robot;
	
	public Case() {
		nord = sud = est = ouest = robot = false;
	}

	public Case(boolean n, boolean s, boolean e, boolean o, boolean r) {
		nord = n;
		sud = s;
		est = e;
		ouest = o;
		robot = r;
	}
	
	public Case(Case c) {
		nord = c.isNord();
		sud = c.isSud();
		est = c.isEst();
		ouest = c.isOuest();
		robot = c.isRobot();
	}

	public boolean isNord() {
		return nord;
	}

	public boolean isSud() {
		return sud;
	}

	public boolean isEst() {
		return est;
	}

	public boolean isOuest() {
		return ouest;
	}
	
	public boolean isRobot() {
		return robot;
	}
	
	public boolean isDirection(Direction dir) {
		switch (dir) {
			case NORD :
				return this.isNord();
			case SUD :
				return this.isSud();
			case EST :
				return this.isEst();
			case OUEST :
				return this.isOuest();
			default :
				//TODO exception
				return false;
		} 
	}

	public void setMurs(boolean n, boolean s, boolean e, boolean o) {
		nord = n;
		sud = s;
		est = e;
		ouest = o;
	}

	public void setRobot(boolean robot) {
		this.robot = robot;
	}

	public String toString() {
		String res = new String("[");
		if(nord) res = res +"N";
		if(sud) res = res +"S";
		if(est) res = res +"E";
		if(ouest) res = res +"O";
		if(robot) res = res +"R";
		
		return res + "]";
	}
	
	
	
}
