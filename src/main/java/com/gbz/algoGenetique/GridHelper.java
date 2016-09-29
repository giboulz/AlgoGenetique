package com.gbz.algoGenetique;

import java.util.List;

import com.gbz.algoGenetique.recupArtefact.*;

public class GridHelper {

	public static void simulateGrid(Grid grid, PossibleMouvement move, List<Item> items, List<Joueur> joueur){
		descreaseBombTimer(); 
		explodeBomb(); 
		propagateExplodingBomb(); 
		exterminateWithBombRange(); 
		countPointOfBoxDestroyed(); 
		movePlayer(); 
		popItem(); 
		popBomb(); 
	}

	private static void popBomb() {
		// TODO Auto-generated method stub
		
	}

	private static void popItem() {
		// TODO Auto-generated method stub
		
	}

	private static void movePlayer() {
		// TODO Auto-generated method stub
		
	}

	private static void countPointOfBoxDestroyed() {
		// TODO Auto-generated method stub
		
	}

	private static void exterminateWithBombRange() {
		// TODO Auto-generated method stub
		
	}

	private static void propagateExplodingBomb() {
		// TODO Auto-generated method stub
		
	}

	private static void explodeBomb() {
		// TODO Auto-generated method stub
		
	}

	private static void descreaseBombTimer() {
		// TODO Auto-generated method stub
		
	}
	
}
