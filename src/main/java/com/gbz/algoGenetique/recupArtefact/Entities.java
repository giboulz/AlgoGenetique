package com.gbz.algoGenetique.recupArtefact;

import java.util.ArrayList;
import java.util.List;

public class Entities {
	public List<Entity> list;
	public List<Joueur> joueurs;
	public List<Bomb> bombs;
	public List<Item> items; 
	

	public Entities() {
		list = new ArrayList<Entity>();
		joueurs = new ArrayList<Joueur>();
		bombs = new ArrayList<Bomb>();
		items = new ArrayList<Item>();
	}
	
	public Entities(Entities entities){
		list = new ArrayList<Entity>(entities.list);
		joueurs = new ArrayList<Joueur>(entities.joueurs);
		bombs = new ArrayList<Bomb>(entities.bombs);
		items = new ArrayList<Item>(entities.items);
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