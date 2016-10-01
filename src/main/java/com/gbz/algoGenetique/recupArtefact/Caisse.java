package com.gbz.algoGenetique.recupArtefact;

public class Caisse extends Tuile {
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