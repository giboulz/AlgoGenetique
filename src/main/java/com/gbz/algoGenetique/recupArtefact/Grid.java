package com.gbz.algoGenetique.recupArtefact;

import java.util.ArrayList;
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

enum Mouvement {
	MOVE("MOVE"), BOMB("BOMB");

	private String name = "";

	Mouvement(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
class Intention {
	public Mouvement move;
	public int x;
	public int y;

	public Intention() {

	}

	public Intention(Mouvement move, int x, int y) {
		this.move = move;
		this.x = x;
		this.y = y;
	}

	public String toString() {
		String s = move.toString() + " " + x + " " + y;
		return s;
	}

}



class Entities {
	List<Entity> list;
	List<Joueur> joueurs;
	List<Bomb> bombs;
	List<Item> items; 
	

	public Entities() {
		list = new ArrayList<Entity>();
		joueurs = new ArrayList<Joueur>();
		bombs = new ArrayList<Bomb>();
		items = new ArrayList<Item>();
	}

	public void addEntity(Entity e) {
		list.add(e);
		if (e instanceof Joueur) {
			joueurs.add((Joueur) e);
		}
		if (e instanceof Bomb) {
			bombs.add((Bomb) e);
		}
		if(e instanceof Item){
			items.add((Item) e); 
		}

	}

	public Joueur getMyPlayer() {
		Joueur me = null;
		for (Joueur joueur : joueurs) {
			if (joueur.owner == Configuration.myId) {
				me = joueur;
			}
		}
		return me;
	}

}

class Entity {
	public Position pos;
	public int owner;

	public Entity(int x, int y, int owner) {
		pos = new Position(x, y);
		this.owner = owner;
	}

}



class ItemRange extends Item{

	public ItemRange(int x, int y) {
		super(x, y);
	}
	
}

class ItemBomb extends Item{

	public ItemBomb(int x, int y) {
		super(x, y);
	}
	
}












