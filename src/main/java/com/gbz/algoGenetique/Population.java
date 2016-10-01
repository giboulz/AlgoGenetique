package com.gbz.algoGenetique;

public class Population {

	Individual[] individuals;

	/*
	 * Constructor
	 */
	// Create a population
	public Population(int populationSize, boolean initialise) {
		individuals = new Individual[populationSize];
		// Initialise population
		if (initialise) {
			// Loop and create individuals
			for (int i = 0; i < size(); i++) {
				Individual newIndividual = new Individual();
				newIndividual.generateIndividual();
				saveIndividual(i, newIndividual);
			}
		}
	}

	/* Getters */
	public Individual getIndividual(int index) {
		return individuals[index];
	}

	public Individual getMostCompetent() {
		Individual mostCompetent = individuals[0];
		// Loop through individuals to find more competent
		for (int i = 0; i < size(); i++) {
			if (mostCompetent.getCompetence() <= getIndividual(i).getCompetence()) {
				mostCompetent = getIndividual(i);
			}
		}
		return mostCompetent;
	}

	/* Public methods */
	// Get population size
	public int size() {
		return individuals.length;
	}

	// Save individual
	public void saveIndividual(int index, Individual indiv) {
		individuals[index] = indiv;
	}
}