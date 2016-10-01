package com.gbz.algoGenetique.recupArtefact;

public class Bomb extends Entity {
	public int leftRoundToExplode;
	public int explodingRange;
	public boolean haveExplode;

	public Bomb(int x, int y, int owner, int leftRoundToExplode, int explodingRange) {
		super(x, y, owner);
		this.leftRoundToExplode = leftRoundToExplode;
		this.explodingRange = explodingRange;
		this.haveExplode = false; 
	}
}