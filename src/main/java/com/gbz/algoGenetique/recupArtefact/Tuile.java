package com.gbz.algoGenetique.recupArtefact;

public class Tuile {


	public boolean hasItem;
	public boolean box;
	public boolean walkable;
	public boolean stopDeflagration; 

	public Tuile() {
		hasItem = false; 
		stopDeflagration = false; 
	}

	public boolean isBox() {
		return false;
	}

	public boolean isWalkable() {
		return false;
	}
	
	public boolean hasItem(){
		return hasItem; 
	}
	
	public boolean isStopDeflagration(){
		return stopDeflagration; 
	}
}