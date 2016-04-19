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
				System.err.println("Direction inexistante");
				return false;
		} 
	}
	
	public boolean isAccessible(Direction dir) {
		switch (dir) {
			case NORD :
				return this.isNord() && !this.isRobot();
			case SUD :
				return this.isSud() && !this.isRobot();
			case EST :
				return this.isEst() && !this.isRobot();
			case OUEST :
				return this.isOuest() && !this.isRobot();
			default :
				System.err.println("Direction inexistante");
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (est ? 1231 : 1237);
		result = prime * result + (nord ? 1231 : 1237);
		result = prime * result + (ouest ? 1231 : 1237);
		result = prime * result + (robot ? 1231 : 1237);
		result = prime * result + (sud ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Case other = (Case) obj;
		if (est != other.est)
			return false;
		if (nord != other.nord)
			return false;
		if (ouest != other.ouest)
			return false;
		if (robot != other.robot)
			return false;
		if (sud != other.sud)
			return false;
		return true;
	}
	
	
	
}
