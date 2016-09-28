package com.gbz.algoGenetique;

public class Individual {

	static int defaultGeneLength = 8;
	private final PossibleMouvement[] genes = new PossibleMouvement[defaultGeneLength];
	// Cache
	private int competence = 0;

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
			competence = Skill.getSkill(this);
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