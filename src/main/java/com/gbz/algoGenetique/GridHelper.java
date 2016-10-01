package com.gbz.algoGenetique;

import java.util.ArrayList;
import java.util.List;

import com.gbz.algoGenetique.recupArtefact.*;

public class GridHelper {

	public static int DEATH_PLAYER = -500;
	public static int OTHER_PLAYER_KILL = 100;
	public static int POINT_FOR_A_BOX = 1;
	public static int POINT_FOR_PICKING_UP_ITEM = 10;
	public static int TIMER_BOMB = 8;

	public static int simulateGridForMultipleMouvement(Grid grid, PossibleMouvement[] moves, Entities entities,
			int myId) {
		int resultatJoueur = 0;
		for (int i = 0; i < moves.length; i++) {
			resultatJoueur += simulateGrid(grid, moves[i], entities, myId);
		}

		resultatJoueur += resolveAllPendingBomb(grid, entities, myId);
		//entities.bombs = removeExplodedBomb(entities.bombs);
		return resultatJoueur;
	}

	private static int resolveAllPendingBomb(Grid grid, Entities entities, int myId) {
		for (Bomb bomb : entities.bombs) {
			bomb.leftRoundToExplode = 0;
			grid.grid[bomb.pos.x][bomb.pos.y].hasBomb = true;
		}
		return explodeBombs(grid, entities, myId, true);
	}

	public static int simulateGrid(Grid grid, PossibleMouvement move, Entities entities, int myId) {
		int resultatTourJoueur = 0;

		distributateItemOnGrid(grid, entities.items);
		descreaseBombTimer(grid, entities.bombs);
		resultatTourJoueur += explodeBombs(grid, entities, myId, false);
		entities.bombs = removeExplodedBomb(entities.bombs);
		resultatTourJoueur += movePlayer(grid, move, entities, myId);

		return resultatTourJoueur;
	}

	private static List<Bomb> removeExplodedBomb(List<Bomb> bombs) {
		List<Bomb> res = new ArrayList<Bomb>();
		for (Bomb b : bombs) {
			if (!b.haveExplode) {
				res.add(b);
			}
		}

		return res;

	}

	private static void distributateItemOnGrid(Grid grid, List<Item> items) {
		if (items != null) {
			for (Item item : items) {
				grid.grid[item.pos.x][item.pos.y].hasItem = true;
			}
		}

	}

	private static void descreaseBombTimer(Grid grid, List<Bomb> bombs) {
		for (Bomb bomb : bombs) {
			bomb.leftRoundToExplode--;
			grid.grid[bomb.pos.x][bomb.pos.y].hasBomb = true;
		}

	}

	private static int explodeBombs(Grid grid, Entities entities, int myId, boolean simulateLastTurnForBombNoKilling) {
		int resultatPlayer = 0;
		for (Bomb bomb : entities.bombs) {
			if (bomb.leftRoundToExplode == 0 && !bomb.haveExplode) {

				resultatPlayer += explodeBomb(grid, bomb, entities, myId, simulateLastTurnForBombNoKilling);
			}
		}
		return resultatPlayer;
	}

	private static int explodeBomb(Grid grid, Bomb bomb, Entities entities, int myId, boolean simulateLastTurnForBombNoKilling) {
		int resultatPlayer = 0;
		bomb.haveExplode = true;
		grid.grid[bomb.pos.x][bomb.pos.y].hasBomb = false;
		resultatPlayer += deflagration(grid, bomb, 1, 0, entities, myId, simulateLastTurnForBombNoKilling);
		resultatPlayer += deflagration(grid, bomb, -1, 0, entities, myId, simulateLastTurnForBombNoKilling);
		resultatPlayer += deflagration(grid, bomb, 0, 1, entities, myId, simulateLastTurnForBombNoKilling);
		resultatPlayer += deflagration(grid, bomb, 0, -1, entities, myId, simulateLastTurnForBombNoKilling);
		return resultatPlayer;
	}

	private static int deflagration(Grid grid, Bomb bomb, int offsetX, int offsetY, Entities entities, int myId, boolean simulateLastTurnForBombNoKilling) {
		int resultatPlayer = 0;
		boolean isDelfagrationStopped = false;
		int i = bomb.pos.x;
		int j = bomb.pos.y;
		for (int z = 0; z < bomb.explodingRange; z++) {
			if (posIsWithinBoard(i, j) && !isDelfagrationStopped) {
				if (grid.grid[i][j].isStopDeflagration()) {
					if (grid.grid[i][j].isBox()) {
						isDelfagrationStopped = true;
						grid.grid[i][j] = new Sol();
						resultatPlayer += POINT_FOR_A_BOX;
					}
					isDelfagrationStopped = isDelfagrationStopped || removeIfItemOnFloor(grid, entities.items, i, j);
				}
				if(!simulateLastTurnForBombNoKilling){
					resultatPlayer = killPlayerOnExplostion(entities.joueurs, myId, resultatPlayer, i, j);
				}

				// TODO ya un problème si une nouvelle bomb redefonce une tuile
				// déjà défoncé, ca va faire comme si elle était pas la.
				scanBombForPropagation(grid, entities, myId, i, j, simulateLastTurnForBombNoKilling);

			}
			i += offsetX;
			j += offsetY;

		}
		return resultatPlayer;

	}

	private static void scanBombForPropagation(Grid grid, Entities entities, int myId, int i, int j, boolean simulateLastTurnForBombNoKilling) {
		for (Bomb b : entities.bombs) {
			if (b.pos.x == i && b.pos.y == j && !b.haveExplode) {
				explodeBomb(grid, b, entities, myId, simulateLastTurnForBombNoKilling);
			}
		}
	}

	private static int killPlayerOnExplostion(List<Joueur> joueurs, int myId, int resultatPlayer, int i, int j) {
		for (Joueur joueur : joueurs) {
			if (joueur.pos.x == i && joueur.pos.y == j) {
				joueur.isDead = true;
				if (joueur.owner == myId) {
					resultatPlayer += GridHelper.DEATH_PLAYER;
				} else {
					resultatPlayer += GridHelper.OTHER_PLAYER_KILL;
				}
			}

		}
		return resultatPlayer;
	}

	private static boolean removeIfItemOnFloor(Grid grid, List<Item> items, int i, int j) {
		boolean isItemHasBeenRemoved = false;
		if (grid.grid[i][j].hasItem) {
			grid.grid[i][j].hasItem = false;
			isItemHasBeenRemoved = true;
			removeItemFromItems(items, i, j);
		}
		return isItemHasBeenRemoved;
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

	private static boolean posIsWithinBoard(int i, int j) {

		if (i >= 0 && j >= 0 && i < Configuration.width && j < Configuration.height) {
			return true;
		}
		return false;
	}

	private static int movePlayer(Grid g, PossibleMouvement pm, Entities entities, int playerId) {
		int resultatPlayer = 0;
		Joueur myPlayer = null;

		for (Joueur j : entities.joueurs) {
			if (j.owner == playerId) {
				myPlayer = j;
			}
		}
		if (myPlayer == null) {
			return 0;
		}

		if (pm == null) {
			pm = PossibleMouvement.IDLE;
		}

		switch (pm) {
		case UP:
			resultatPlayer += move(g, myPlayer, 0, -1);
			break;
		case DOWN:
			resultatPlayer += move(g, myPlayer, 0, 1);
			break;
		case LEFT:
			resultatPlayer += move(g, myPlayer, -1, 0);
			break;
		case RIGHT:
			resultatPlayer += move(g, myPlayer, 1, 0);
			break;
		case BOMB:
			if (myPlayer.nbLeftBomb > 0) {
				myPlayer.nbLeftBomb--;
				entities.bombs
						.add(new Bomb(myPlayer.pos.x, myPlayer.pos.y, playerId, TIMER_BOMB, myPlayer.explodingRange));
			}
			break;
		case IDLE:
			break;
		default:
			break;
		}

		return resultatPlayer;
	}

	private static int move(Grid g, Joueur player, int offsetX, int offsetY) {
		int restultatPlayer = 0;
		int x = player.pos.x + offsetX;
		int y = player.pos.y + offsetY;

		if (posIsWithinBoard(x, y) && g.grid[x][y].isWalkable() && !g.grid[x][y].hasBomb) {

			player.pos.x = x;
			player.pos.y = y;
			if (g.grid[x][y].hasItem) {
				restultatPlayer += POINT_FOR_PICKING_UP_ITEM;
				g.grid[x][y].hasItem = false;
			}
		}
		return restultatPlayer;

	}

}
