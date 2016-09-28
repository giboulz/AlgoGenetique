package com.gbz.algoGenetique;

public class Ga {

	public final static int POPULATION_SIZE_INITIAL = 50; 
	
	public static void main(String[] args) {

		PossibleMouvement[] solution = new PossibleMouvement[8];
		solution[0] = PossibleMouvement.BOMB;
		solution[1] = PossibleMouvement.UP; 
		solution[2] = PossibleMouvement.DOWN; 
		solution[3] = PossibleMouvement.LEFT; 
		solution[4] = PossibleMouvement.RIGHT; 
		solution[5] = PossibleMouvement.BOMB; 
		solution[6] = PossibleMouvement.UP;
		solution[7] = PossibleMouvement.UP;

		System.out.println("Wanted Solution : ");
		for(PossibleMouvement p : solution){
			System.out.print(p +" ");
		}
		System.out.println();
		
		// Set a candidate solution
		Skill.setSolution(solution);

		// Create an initial population
		Population myPop = new Population(POPULATION_SIZE_INITIAL, true);

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
