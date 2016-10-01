package com.gbz.algoGenetique.recupArtefact;

class Wall extends Tuile {
	
	public Wall(){
		super(); 
		this.stopDeflagration = true; 
	}
	
	public String toString() {
		return "X";
	}

	public boolean isBox() {
		return false;
	}
	
	public boolean isWalkable() {
		return false;
	}