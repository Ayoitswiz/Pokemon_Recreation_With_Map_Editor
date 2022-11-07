package adventuremode.sprites;

public enum Direction {
	L, R, U, D;


public Direction turnAround() {

	return switch (this) {
		case L -> R;
		case R -> L;
		case U -> D;
		case D -> U;
	};
}
}
