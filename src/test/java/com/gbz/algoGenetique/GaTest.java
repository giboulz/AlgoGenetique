package com.gbz.algoGenetique;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gbz.algoGenetique.recupArtefact.Caisse;
import com.gbz.algoGenetique.recupArtefact.Configuration;
import com.gbz.algoGenetique.recupArtefact.Entities;

import com.gbz.algoGenetique.recupArtefact.Grid;
import com.gbz.algoGenetique.recupArtefact.Joueur;
import com.gbz.algoGenetique.recupArtefact.Sol;

public class GaTest {

	public final static int TOTAL_MS_TO_COMPUTE = 100;
	public final static int POPULATION_SIZE_INITIAL = 50;
	public final static int LENGHT_GENE = 8;
	
	private Grid g;
	private Entities entities;
	private int ID_PLAYER = 0;

	@Before
	public void setUp() {
		g = new Grid();

		for (int i = 0; i < Configuration.width; i++) {
			for (int j = 0; j < Configuration.height; j++) {
				g.grid[i][j] = new Sol();
			}
		}
		g.grid[2][0] = new Caisse();
		g.grid[2][1] = new Caisse();
		g.grid[2][2] = new Caisse();
		g.grid[1][2] = new Caisse();

		entities = new Entities();
		entities.joueurs.add(new Joueur(0, 0, ID_PLAYER, 1, 3));
	}

	@Test
	public void test_position_for_given_grid() {

		Algorithm.grid = g;
		Algorithm.entities = entities;
		Algorithm.idPlayer = ID_PLAYER;
		Individual.setDefaultGeneLength(LENGHT_GENE);
		// Create an initial population
		Population myPop = new Population(POPULATION_SIZE_INITIAL, true, g, entities, ID_PLAYER);

		// Evolve our population until we reach an optimum solution

		long startTimeFor1Generation = System.nanoTime();
		long startTimeForAllGeneration = startTimeFor1Generation;
		int generationCount = 0;
		long timeEndOfAllGenerationtimeTotal2 = 0;
		long timeEnd1Generation = 0;
		double timeToExecuteAllGeneration = 0.0f;
		double timeToExecute1Generation = 0.0f;
		// while(generationCount < NB_GENERATION_TO_COMPUTE ) {
		while (timeToExecuteAllGeneration + timeToExecute1Generation < TOTAL_MS_TO_COMPUTE) {
			generationCount++;
			System.out.println(
					"Generation: " + generationCount + " competence: " + myPop.getMostCompetent().getCompetence());
			myPop = Algorithm.evolvePopulation(myPop);
			timeEnd1Generation = System.nanoTime();
			timeToExecute1Generation = (double) ((timeEnd1Generation - startTimeFor1Generation) / 1e6);
			System.out.println("time to execute : " + timeToExecute1Generation);
			startTimeFor1Generation = timeEnd1Generation;
			timeEndOfAllGenerationtimeTotal2 = System.nanoTime();
			timeToExecuteAllGeneration = (double) ((timeEndOfAllGenerationtimeTotal2 - startTimeForAllGeneration)
					/ 1e6);
		}

		System.out.println("Generation: " + generationCount);
		System.out.println("Genes:");
		System.out.println(myPop.getMostCompetent());
		System.out.println("Competences : " + myPop.getMostCompetent().getCompetence());
		System.out.println("Compute in : " + timeToExecuteAllGeneration);
	}
}
