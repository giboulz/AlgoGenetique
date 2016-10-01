package com.gbz.algoGenetique;

import com.gbz.algoGenetique.recupArtefact.Entities;
import com.gbz.algoGenetique.recupArtefact.Grid;

public class Individual {

	
	//game spécific
	private Entities entities; 
	private Grid grid; 
	private int idPlayer; 
	
	
	public static int defaultGeneLength = 8;
	public final PossibleMouvement[] genes = new PossibleMouvement[defaultGeneLength];
	// Cache
	private int competence = 0;

	public Individual(Grid grid, Entities entities, int idPlayer){
		this.grid = new Grid(grid);
		this.entities = new Entities(entities); 
		this.idPlayer = idPlayer; 
		
	}
	
	
	
	// Create a random individual
	public void generateIndividual() {
		int lower = 0;
		int higher = 4;
		for (int i = 0; i < size(); i++) {
			int random = (int) (Math.random() * (higher - lower)) + lower;
			genes[i] = PossibleMouvementHelper.r.random();
		}
	}

	/* Getters and setters */
	// Use this if you want to create individuals with different gene lengths
	public static void setDefaultGeneLength(int length) {
		defaultGeneLength = length;
	}

	public PossibleMouvement getGene(int index) {
		return genes[index];
	}

	public void setGene(int index, PossibleMouvement value) {
		genes[index] = value;
		competence = 0;
	}

	/* Public methods */
	public int size() {
		return genes.length;
	}

	public int getCompetence() {
		if (competence == 0) {
			competence = Skill.getSkill(this, grid, entities, idPlayer);
		}
		return competence;
	}

	@Override
	public String toString() {
		String geneString = "";
		for (int i = 0; i < size(); i++) {
			geneString += getGene(i) + " ";
		}
		geneString += "\n";
		return geneString;
	}
}