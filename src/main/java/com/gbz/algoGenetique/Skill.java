package com.gbz.algoGenetique;

import com.gbz.algoGenetique.recupArtefact.*;

public class Skill {

	public static int getSkill(Individual individual, Grid grid, Entities entities, int myId) {

		return GridHelper.simulateGridForMultipleMouvement(grid, individual.genes, entities, myId);

	}

}
