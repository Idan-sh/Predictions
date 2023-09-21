package com.idansh.engine.helpers;

import java.util.Random;

/**
 * Abstract class that contains static methods which
 * returns random values of various types.
 */
public abstract class RandomValue {
    private static final Random rnd = new Random();
    private static final String VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?,-(). ";


    /**
     * Get a random boolean value.
     */
    public static Boolean getRandomBoolean() {
        return rnd.nextBoolean();
    }


    /**
     * Get a random decimal number in a range, including both the bottom and top bounds.
     * If from/to params received have a fraction, they will be rounded down to the closest integer,
     * @param from minimum number of the range.
     * @param to maximum number of the range.
     */
    public static int getRandomInt(double from, double to) {
        int fromInt = (int) from;
        int toInt = (int) to;
        return rnd.nextInt(toInt + 1 - fromInt) + fromInt;
    }


    /**
     * Get a random float number in a range, including both the bottom and top bounds.
     * @param from minimum number of the range.
     * @param to maximum number of the range.
     */
    public static float getRandomFloatFromRange(double from, double to) {
        double randomDouble = from + (to - from) * rnd.nextDouble(); // Get a random double within the specified range
        return (float) randomDouble; // Convert to float and return it
    }

    /**
     * Get a random float number without a range.
     */
    public static float getRandomFloatWithoutRange() {
        double randomDouble = rnd.nextInt() * rnd.nextDouble(); // Get a random double within the specified range
        return (float) randomDouble; // Convert to float and return it
    }


    /**
     * Get a random double number from 0 to 1, including both the bottom and top bounds.
     */
    public static double getRandomDouble() {
        return rnd.nextInt(1001) / 1000.0;
    }


    /**
     * Get a random string in a pre-defined range (defined internally).
     * @return a string of a random length with random characters.
     */
    public static String getRandomString() {
        final int maxStringLen = 52;        // The maximum length of a possible generated string
        final int minStringLen = 1;         // The minimum length of a possible generated string
        int stringLen = rnd.nextInt(maxStringLen - minStringLen) + minStringLen;  // Generate a random string length

        // Start building the string:
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < stringLen; i++) {
            int randomIndex = rnd.nextInt(VALID_CHARACTERS.length());   // Get an index of a random character
            char randomChar = VALID_CHARACTERS.charAt(randomIndex);     // Get the random character at that index
            stringBuilder.append(randomChar);                           // Add the random character to the string
        }

        return stringBuilder.toString();
    }
}
