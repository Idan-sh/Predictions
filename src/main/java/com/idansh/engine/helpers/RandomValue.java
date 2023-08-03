package com.idansh.engine.helpers;

import java.util.Random;

// todo- possibly add possibilities
public abstract class RandomValue {
    private static final Random rnd = new Random();

    /**
     * Get a random boolean value.
     */
    public static Boolean getRandomBoolean() {
        return rnd.nextBoolean();
    }


    /**
     * Get a random decimal number in a range, including both the bottom and top bounds.
     * @param from minimum number of the range.
     * @param to maximum number of the range.
     */
    public static int getRandomInt(int from, int to) {
        return rnd.nextInt(to + 1 - from) + from;
    }


    /**
     * Get a random float number in a range, including both the bottom and top bounds.
     * @param from minimum number of the range.
     * @param to maximum number of the range.
     */
    public static float getRandomFloat(int from, int to) {
        return rnd.nextFloat() * (rnd.nextInt(to + 1 - from) + from);

    }


    /**
     * Get a random number in a range, including both the bottom and top bounds.
     * @param from minimum number of the range.
     * @param to maximum number of the range.
     */
    public static String getRandomString() {
        // todo- random string generator
        return "";
    }

}
