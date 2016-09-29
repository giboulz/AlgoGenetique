package com.gbz.algoGenetique.recupArtefact;


public class Joueur extends Entity {
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
