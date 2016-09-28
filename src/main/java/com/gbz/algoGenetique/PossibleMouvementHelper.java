package com.gbz.algoGenetique;
import java.util.Random;

public class PossibleMouvementHelper {


	    public static final RandomEnum<PossibleMouvement> r =
	        new RandomEnum<PossibleMouvement>(PossibleMouvement.class);


	    public static class RandomEnum<E extends Enum> {

	        private static final Random RND = new Random();
	        private final E[] values;

	        public RandomEnum(Class<E> token) {
	            values = token.getEnumConstants();
	        }

	        public E random() {
	            return values[RND.nextInt(values.length)];
	        }
	    }
	
}
