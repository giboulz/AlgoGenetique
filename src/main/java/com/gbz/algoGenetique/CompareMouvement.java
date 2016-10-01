package com.gbz.algoGenetique;

import java.util.Comparator;

public class CompareMouvement implements  Comparator<Individual> {

	public int compare(Individual arg0, Individual arg1) {
		return arg1.getCompetence() - arg0.getCompetence();
	}

}
