package com.gbz.algoGenetique;

import java.util.Random;

public class EnumTest {

    private enum Season { WINTER, SPRING, SUMMER, FALL }

    private static final RandomEnum<Season> r =
        new RandomEnum<Season>(Season.class);

    public static void main(String[] args) {
        System.out.println(r.random());
    }

    private static class RandomEnum<E extends Enum> {

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