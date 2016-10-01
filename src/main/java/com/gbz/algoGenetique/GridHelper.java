package com.gbz.algoGenetique;

import java.util.List;

import com.gbz.algoGenetique.recupArtefact.*;

public class GridHelper {

	public static int DEATH_PLAYER = -500;
	public static int OTHER_PLAYER_KILL = 100;

	public static int simulateGrid(Grid grid, PossibleMouvement move, List<Bomb> bombs, List<Item> items,
			List<Joueur> joueurs, int myId) {
		int resultatTourJoueur = 0;

		distruateItemOnGrid(grid, items);
		descreaseBombTimer(bombs);
		resultatTourJoueur += explodeBomb(grid, bombs, items, joueurs, myId);
		propagateExplodingBomb();
		exterminateWithBombRange();
		resultatTourJoueur += countPointOfBoxDestroyed();
		resultatTourJoueur += movePlayer();
		popItem();
		popBomb();

		return resultatTourJoueur;
	}

	private static void distruateItemOnGrid(Grid grid, List<Item> items) {
		for (Item item : items) {
			grid.grid[item.pos.x][item.pos.y].hasItem = true;
		}

	}

	private static void descreaseBombTimer(List<Bomb> bombs) {
		for (Bomb bomb : bombs) {
			bomb.leftRoundToExplode--;
		}

	}

	private static int explodeBomb(Grid grid, List<Bomb> bombs, List<Item> items, List<Joueur> joueurs, int myId) {
		int resultatPlayer = 0;
		for (Bomb bomb : bombs) {
			if (bomb.leftRoundToExplode == 0) {
				resultatPlayer += explodeBomb(grid, bomb, items, joueurs, myId);
			}
		}
		return resultatPlayer;
	}

	private static int explodeBomb(Grid grid, Bomb bomb, List<Item> items, List<Joueur> joueurs, int myId) {
		int resultatPlayer = 0;
		resultatPlayer += deflagration(grid, bomb, 1, 0, items, joueurs, myId);
		resultatPlayer += deflagration(grid, bomb, -1, 0, items, joueurs, myId);
		resultatPlayer += deflagration(grid, bomb, 0, 1, items, joueurs, myId);
		resultatPlayer += deflagration(grid, bomb, 0, -1, items, joueurs, myId);
		return resultatPlayer;
	}

	private static int deflagration(Grid grid, Bomb bomb, int offsetX, int offsetY, List<Item> items,
			List<Joueur> joueurs, int myId) {
		int resultatPlayer = 0;
		boolean isDelfagrationStopped = false;
		int i = bomb.pos.x;
		int j = bomb.pos.y;
		for (int z = 0; z < bomb.explodingRange; z++) {
			if (!outOfBound(i, j) && !isDelfagrationStopped) {
				if (grid.grid[i][j].isStopDeflagration()) {
					if (grid.grid[i][j].isBox()) {
						isDelfagrationStopped = true;
						grid.grid[i][j] = new Sol();
						resultatPlayer++;
					}
					removeIfItemOnFloor(grid, items, i, j);
				}
				resultatPlayer = killPlayerOnExplostion(joueurs, myId, resultatPlayer, i, j);

				i += offsetX;
				j += offsetY;

			}

		}
		return resultatPlayer;

	}

	private static int killPlayerOnExplostion(List<Joueur> joueurs, int myId, int resultatPlayer, int i, int j) {
		for (Joueur joueur : joueurs) {
			if (joueur.pos.x == i && joueur.pos.y == j) {
				if (joueur.owner == myId) {
					resultatPlayer += GridHelper.DEATH_PLAYER;
				} else {
					resultatPlayer += GridHelper.OTHER_PLAYER_KILL;
				}
			}

		}
		return resultatPlayer;
	}

	private static void removeIfItemOnFloor(Grid grid, List<Item> items, int i, int j) {
		if (grid.grid[i][j].hasItem) {
			grid.grid[i][j].hasItem = false;

			removeItemFromItems(items, i, j);
		}
	}

	private static void removeItemFromItems(List<Item> items, int i, int j) {
		int posItemToRemove = 0;
		for (int ind = 0; ind < items.size(); ind++) {
			if (items.get(ind).pos.x == i && items.get(ind).pos.y == j) {
				posItemToRemove = ind;
			}
		}
		items.remove(posItemToRemove);
	}

	private static boolean outOfBound(int i, int j) {

		if (i < 0 || j < 0 || i > Configuration.width - 1 || j > Configuration.height - 1) {
			return true;
		}
		return false;
	}

	private static void propagateExplodingBomb() {
		// TODO Auto-generated method stub

	}

	private static void exterminateWithBombRange() {
		// TODO Auto-generated method stub

	}

	private static void countPointOfBoxDestroyed() {
		// TODO Auto-generated method stub

	}

	private static void movePlayer() {
		// TODO Auto-generated method stub

	}

	private static void popItem() {
		// TODO Auto-generated method stub

	}

	private static void popBomb() {
		// TODO Auto-generated method stub

	}

}
