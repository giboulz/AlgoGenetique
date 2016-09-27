package com.gbz.algoGenetique;

public class Ga {

	public static void main(String[] args) {

		// Set a candidate solution
		Skill.setSolution("1111000000000000000000000000001111100000000000000000000000001111");

		// Create an initial population
		Population myPop = new Population(50, true);

		// Evolve our population until we reach an optimum solution
		int generationCount = 0;
		while (myPop.getMoreCompetent().getCompetence() < Skill.getMaxSkill()) {
			generationCount++;
			System.out.println(
					"Generation: " + generationCount + " competence: " + myPop.getMoreCompetent().getCompetence());
			myPop = Algorithm.evolvePopulation(myPop);
		}
		System.out.println("Solution found!");
		System.out.println("Generation: " + generationCount);
		System.out.println("Genes:");
		System.out.println(myPop.getMoreCompetent());

	}
}
