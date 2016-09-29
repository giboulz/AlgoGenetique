package com.gbz.algoGenetique.recupArtefact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {
	public Tuile[][] grid;

	public Map<Integer, boolean[][]> playerMobility;

	public Grid() {
		grid = new Tuile[Configuration.width][Configuration.height];

		for (int i = 0; i < Configuration.width; i++) {
			for (int y = 0; y < Configuration.height; y++) {
				grid[i][y] = new Tuile();
			}
		}

		playerMobility = new HashMap<Integer, boolean[][]>();

	}

	public void reduceBombingSpotToWalkablePath(int playerId, int[][] res) {
		boolean[][] walkablePathForPlayer = playerMobility.get(playerId);
		if (walkablePathForPlayer != null) {
			for (int i = 0; i < Configuration.width; i++) {
				for (int y = 0; y < Configuration.height; y++) {
					if (walkablePathForPlayer[i][y] == false) {
						res[i][y] = 0;
					}
				}
			}
		}

	}

	public void calculateAccessibleTuileForPlayer(Joueur myPlayer) {
		boolean[][] res = new boolean[Configuration.width][Configuration.height];
		for (int i = 0; i < Configuration.width; i++) {
			for (int y = 0; y < Configuration.height; y++) {
				res[i][y] = false;
			}
		}
		calculateMovingPath(myPlayer.pos, res);

		playerMobility.put(myPlayer.owner, res);

	}

	private void calculateMovingPath(Position pos, boolean[][] res) {
		if (!outOfBound(pos)) {
			int x = pos.x;
			int y = pos.y;
			if (grid[x][y].isWalkable() && res[x][y] == false) {
				res[x][y] = true;

				calculateMovingPath(new Position(x + 1, y), res);
				calculateMovingPath(new Position(x - 1, y), res);
				calculateMovingPath(new Position(x, y + 1), res);
				calculateMovingPath(new Position(x, y + 1), res);
			}
		}

	}

	public void resolvePendingExplosition(List<Bomb> bombs) {
		for (Bomb b : bombs) {
			resolveBomb(b, 0, 1);
			resolveBomb(b, 0, -1);
			resolveBomb(b, 1, 0);
			resolveBomb(b, -1, 0);
		}

	}

	private void resolveBomb(Bomb b, int xOffset, int yOffset) {
		boolean res = false;
		int i = b.pos.x;
		int j = b.pos.y;
		for (int z = 0; z < b.explodingRange; z++) {
			if (!outOfBound(i, j) && !res) {
				if (grid[i][j].isBox()) {
					res = true;
					grid[i][j] = new Sol();
				}
				i += xOffset;
				j += yOffset;

			}

		}
	}

	public static Position getBestPositionWithinGivenMouvements(int[][] tabOfBombPlacement, int nbMouvement,
			Position actual) {
		Position res = new Position(0, 0);
		res.value = 0;
		int nbBox = 0;

		for (int i = actual.x - nbMouvement; i < actual.x + nbMouvement; i++) {
			for (int j = actual.y - nbMouvement; j < actual.y + nbMouvement; j++) {
				if (!outOfBound(i, j)) {
					if (tabOfBombPlacement[i][j] > nbBox) {
						res = new Position(i, j);

						nbBox = tabOfBombPlacement[i][j];
						res.value = nbBox;
					}
				}
			}
		}
		return res;
	}

	public int[][] getNumberHitBoxForBomb(int explodingRange) {
		int[][] res = new int[Configuration.width][Configuration.height];

		for (int i = 0; i < Configuration.width; i++) {
			for (int j = 0; j < Configuration.height; j++) {
				int boxUp = 0;
				int boxRight = 0;
				int boxDown = 0;
				int boxLeft = 0;
				if (!isBoxIsOnTuile(i, j)) {
					boxUp = calculateIfBoxIsInExplodingRange(i, j, explodingRange, 0, -1);
					boxRight = calculateIfBoxIsInExplodingRange(i, j, explodingRange, 1, 0);
					boxDown = calculateIfBoxIsInExplodingRange(i, j, explodingRange, 0, 1);
					boxLeft = calculateIfBoxIsInExplodingRange(i, j, explodingRange, -1, 0);
				}
				res[i][j] = boxUp + boxRight + boxDown + boxLeft;

			}
		}

		return res;
	}

	private int calculateIfBoxIsInExplodingRange(int i, int j, int explodingRange, int xOffset, int yOffset) {
		boolean res = false;
		for (int z = 0; z < explodingRange; z++) {
			if (!outOfBound(i, j)) {
				if (grid[i][j].isBox()) {
					res = true;
				}
				i += xOffset;
				j += yOffset;

			}

		}
		if (res)
			return 1;
		else
			return 0;

	}

	private static boolean outOfBound(Position pos) {
		return outOfBound(pos.x, pos.y);
	}

	private static boolean outOfBound(int i, int j) {

		if (i < 0 || j < 0 || i > Configuration.width - 1 || j > Configuration.height - 1) {
			return true;
		}
		return false;
	}

	public boolean isBoxIsOnTuile(int x, int y) {
		return grid[x][y].isBox();
	}

	public String toString() {
		String s = "";

		for (int y = 0; y < Configuration.height; y++) {
			for (int i = 0; i < Configuration.width; i++) {
				s += grid[i][y].toString();
			}
			s += "\n";
		}
		return s;
	}
}

class Configuration {
	public static final int ENTITY_JOUEUR = 0;
	public static final int ENTITY_BOMB = 1;

	public static int myId;
	public static int height;
	public static int width;
}

class Entity {
	public Position pos;
	public int owner;

	public Entity(int x, int y, int owner) {
		pos = new Position(x, y);
		this.owner = owner;
	}

}

class Tuile {

	public boolean box;
	public boolean walkable;

	public Tuile() {

	}

	public boolean isBox() {
		return false;
	}

	public boolean isWalkable() {
		return false;
	}
}

class Caisse extends Tuile {
	public String toString() {
		return "0";
	}

	public boolean isBox() {
		return true;
	}
}

class Sol extends Tuile {
	public String toString() {
		return ".";
	}

	public boolean isWalkable() {
		return true;
	}
}

class Wall extends Tuile {
	public String toString() {
		return "X";
	}

	public boolean isBox() {
		return false;
	}
	
	public boolean isWalkable() {
		return false;
	}
}


class Joueur extends Entity {
	public int nbLeftBomb;
	public int explodingRange;

	public Joueur(int x, int y, int owner, int nbLeftBomb, int explodingRange) {
		super(x, y, owner);
		this.nbLeftBomb = nbLeftBomb;
		this.explodingRange = explodingRange;
	}
}

class Bomb extends Entity {
	public int leftRoundToExplode;
	public int explodingRange;

	public Bomb(int x, int y, int owner, int leftRoundToExplode, int explodingRange) {
		super(x, y, owner);
		this.leftRoundToExplode = leftRoundToExplode;
		this.explodingRange = explodingRange;
	}
}

class Position {
	public int x;
	public int y;
	public int value;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		return x == other.x && y == other.y;
	}

}

