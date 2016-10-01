package com.gbz.algoGenetique.recupArtefact;

class Caisse extends Tuile {
	public Caisse(){
		super(); 
		this.stopDeflagration = true; 
	}
	
	public String toString() {
		return "0";
	}

	public boolean isBox() {
		return true;
	}
}