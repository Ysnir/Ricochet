package ricochet.utilitaire;

public enum Direction {
	NORD(0, -1),
	SUD(0, 1),
	EST(1, 0),
	OUEST(-1, 0);
	
	private final int[] mouvement = new int[2];
	
	Direction(int x, int y) {
		this.mouvement[0] = x;
		this.mouvement[1] = y;
	}
	
	public int[] getMouvement(){
		return this.mouvement;
	}
}
