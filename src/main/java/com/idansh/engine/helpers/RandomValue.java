package com.idansh.engine.helpers;

import java.util.Random;

// todo- possibly add possibilities
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
