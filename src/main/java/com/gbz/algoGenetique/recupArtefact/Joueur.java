package com.gbz.algoGenetique.recupArtefact;


public class Joueur extends Entity {
	public int nbLeftBomb;
	public int explodingRange;
	public boolean isDead;

	public Joueur(int x, int y, int owner, int nbLeftBomb, int explodingRange) {
		super(x, y, owner);
		this.nbLeftBomb = nbLeftBomb;
		this.explodingRange = explodingRange;
		this.isDead = false; 
	}
}


