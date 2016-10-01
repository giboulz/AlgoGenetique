package com.gbz.algoGenetique;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gbz.algoGenetique.recupArtefact.Bomb;
import com.gbz.algoGenetique.recupArtefact.Caisse;
import com.gbz.algoGenetique.recupArtefact.Configuration;
import com.gbz.algoGenetique.recupArtefact.Entities;
import com.gbz.algoGenetique.recupArtefact.Grid;
import com.gbz.algoGenetique.recupArtefact.ItemBomb;
import com.gbz.algoGenetique.recupArtefact.Joueur;
import com.gbz.algoGenetique.recupArtefact.Position;
import com.gbz.algoGenetique.recupArtefact.Sol;
import com.gbz.algoGenetique.recupArtefact.Wall;

public class GridHelperTest {

	private static int ID_MY_PLAYER = 0;
	private static int ID_OTHER_PLAYER = 1;
	private Grid g;
	private Entities entities;

	@Before
	public void setUp() {
		g = new Grid();

		for (int i = 0; i < Configuration.width; i++) {
			for (int j = 0; j < Configuration.height; j++) {
				g.grid[i][j] = new Sol();
			}
		}

		entities = new Entities();
	}

	@Test
	public void test_exploding_1_bomb() {

		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		entities.bombs.add(new Bomb(6, 6, ID_MY_PLAYER, 2, 3));

		int res = GridHelper.simulateGrid(g, null, entities, ID_MY_PLAYER);

		assertThat(entities.bombs.size(), is(1));
		assertThat(entities.bombs.get(0).leftRoundToExplode, is(1));
		assertThat(res, is(0));
	}

	@Test
	public void test_exploding_1_bomb_other_are_limit_out_of_range() {

		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		entities.bombs.add(new Bomb(8, 5, ID_MY_PLAYER, 2, 3));

		int res = GridHelper.simulateGrid(g, null, entities, ID_MY_PLAYER);

		assertThat(entities.bombs.size(), is(1));
		assertThat(entities.bombs.get(0).leftRoundToExplode, is(1));
		assertThat(res, is(0));
	}

	@Test
	public void test_chain_explosion_2_bomb() {

		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		entities.bombs.add(new Bomb(7, 5, ID_MY_PLAYER, 8, 3));

		int res = GridHelper.simulateGrid(g, null, entities, ID_MY_PLAYER);

		assertThat(entities.bombs.size(), is(0));
		assertThat(res, is(0));
	}

	@Test
	public void test_chain_explosion_3_bomb() {
		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		entities.bombs.add(new Bomb(7, 5, ID_MY_PLAYER, 8, 3));
		entities.bombs.add(new Bomb(7, 7, ID_MY_PLAYER, 8, 3));

		int res = GridHelper.simulateGrid(g, null, entities, ID_MY_PLAYER);

		assertThat(entities.bombs.size(), is(0));
		assertThat(res, is(0));
	}

	@Test
	public void chain_explosion_destroy_box() {
		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		g.grid[5][7] = new Caisse();
		g.grid[5][3] = new Caisse();
		g.grid[7][5] = new Caisse();
		g.grid[3][5] = new Caisse();
		g.grid[4][4] = new Caisse();

		int res = GridHelper.simulateGrid(g, null, entities, ID_MY_PLAYER);

		assertThat(entities.bombs.size(), is(0));
		assertThat(g.grid[5][7], instanceOf(Sol.class));
		assertThat(g.grid[5][3], instanceOf(Sol.class));
		assertThat(g.grid[7][5], instanceOf(Sol.class));
		assertThat(g.grid[3][5], instanceOf(Sol.class));
		assertThat(g.grid[4][4], instanceOf(Caisse.class));
		assertThat(res, is(GridHelper.POINT_FOR_A_BOX * 4));
	}

	@Test
	public void chain_explosion_kill_my_player() {
		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		entities.joueurs.add(new Joueur(5, 6, ID_MY_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, null, entities, ID_MY_PLAYER);

		assertThat(entities.bombs.size(), is(0));
		assertThat(entities.joueurs.size(), is(1));
		assertThat(entities.joueurs.get(0).isDead, is(true));
		assertThat(res, is(GridHelper.DEATH_PLAYER));

	}

	@Test
	public void chain_explosion_kill_other_player() {
		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		entities.joueurs.add(new Joueur(5, 6, ID_OTHER_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, null, entities, ID_MY_PLAYER);

		assertThat(entities.bombs.size(), is(0));
		assertThat(entities.joueurs.size(), is(1));
		assertThat(entities.joueurs.get(0).isDead, is(true));
		assertThat(res, is(GridHelper.OTHER_PLAYER_KILL));

	}

	@Test
	public void chain_explosion_stop_on_wall() {
		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		g.grid[5][7] = new Wall();
		g.grid[5][3] = new Wall();
		g.grid[7][5] = new Wall();
		g.grid[3][5] = new Wall();
		g.grid[4][4] = new Wall();

		int res = GridHelper.simulateGrid(g, null, entities, ID_MY_PLAYER);

		assertThat(entities.bombs.size(), is(0));
		assertThat(g.grid[5][7], instanceOf(Wall.class));
		assertThat(g.grid[5][3], instanceOf(Wall.class));
		assertThat(g.grid[7][5], instanceOf(Wall.class));
		assertThat(g.grid[3][5], instanceOf(Wall.class));
		assertThat(g.grid[4][4], instanceOf(Wall.class));
		assertThat(res, is(0));
	}

	@Test
	public void move_player_up() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.UP, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(4));
		assertThat(res, is(0));

	}

	@Test
	public void move_player_left() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.LEFT, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(4));
		assertThat(entities.joueurs.get(0).pos.y, is(5));
		assertThat(res, is(0));

	}

	@Test
	public void move_player_right() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.RIGHT, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(6));
		assertThat(entities.joueurs.get(0).pos.y, is(5));
		assertThat(res, is(0));
	}

	@Test
	public void move_player_down() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.DOWN, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(6));
		assertThat(res, is(0));
	}

	@Test
	public void move_player_bomb() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 1, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.BOMB, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(5));
		assertThat(entities.bombs.size(), is(1));
		assertThat(entities.bombs.get(0).pos.x, is(5));
		assertThat(entities.bombs.get(0).pos.y, is(5));
		assertThat(res, is(0));
	}

	@Test
	public void move_player_bomb_without_any_bomb_to_pose() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.BOMB, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(5));
		assertThat(entities.bombs.size(), is(0));
		assertThat(res, is(0));
	}

	@Test
	public void move_player_idle() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.IDLE, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(5));
		assertThat(res, is(0));
	}

	@Test
	public void move_into_Box() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));
		g.grid[5][6] = new Caisse();

		int res = GridHelper.simulateGrid(g, PossibleMouvement.DOWN, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(5));
		assertThat(res, is(0));
	}

	@Test
	public void move_into_Wall() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));
		g.grid[5][6] = new Wall();

		int res = GridHelper.simulateGrid(g, PossibleMouvement.DOWN, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(5));
		assertThat(res, is(0));
	}

	@Test
	public void move_out_of_board() {
		entities.joueurs.add(new Joueur(11, 10, ID_MY_PLAYER, 0, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.DOWN, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(11));
		assertThat(entities.joueurs.get(0).pos.y, is(10));
		;
		assertThat(res, is(0));
	}

	@Test
	public void move_into_bomb() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));
		entities.bombs.add(new Bomb(5, 6, ID_MY_PLAYER, 8, 3));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.DOWN, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(5));
		assertThat(res, is(0));
	}

	@Test
	public void move_into_item() {
		entities.joueurs.add(new Joueur(5, 5, ID_MY_PLAYER, 0, 3));
		entities.items.add(new ItemBomb(5, 6));

		int res = GridHelper.simulateGrid(g, PossibleMouvement.DOWN, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(5));
		assertThat(entities.joueurs.get(0).pos.y, is(6));
		assertThat(res, is(GridHelper.POINT_FOR_PICKING_UP_ITEM));
	}
	
	@Test
	public void test_multiple_mouvement_resolve_all_pending_bomb(){
		entities.joueurs.add(new Joueur(1, 1, ID_MY_PLAYER, 1, 3));
		entities.bombs.add(new Bomb(5, 5, ID_MY_PLAYER, 1, 3));
		g.grid[5][7] = new Caisse();
		g.grid[5][3] = new Caisse();
		g.grid[7][5] = new Caisse();
		g.grid[3][5] = new Caisse();
		g.grid[4][4] = new Caisse();
		
		PossibleMouvement[] tabMouvement ={PossibleMouvement.BOMB}; 

		int res = GridHelper.simulateGridForMultipleMouvement(g, tabMouvement, entities, ID_MY_PLAYER);

		assertThat(entities.joueurs.get(0).pos.x, is(1));
		assertThat(entities.joueurs.get(0).pos.y, is(1));
		assertThat(res, is(GridHelper.POINT_FOR_A_BOX * 4));
	}

}
