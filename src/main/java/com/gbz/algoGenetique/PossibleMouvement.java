package com.gbz.algoGenetique;

public enum PossibleMouvement {

	UP, RIGHT, DOWN, LEFT, BOMB, IDLE;

	@Override
	public String toString() {
		String res = "";
		switch (this) {
		case UP:
			res = "UP";
			break;
		case RIGHT:
			res = "RIGHT";
			break;
		case DOWN:
			res = "DOWN";
			break;
		case LEFT:
			res = "LEFT";
			break;
		case BOMB:
			res = "BOMB";
			break;
		case IDLE:
			res = "IDLE";
			break;
		default:
			break;
		}

		return res; 
	}

}
