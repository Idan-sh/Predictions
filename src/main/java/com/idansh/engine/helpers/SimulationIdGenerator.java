package com.idansh.engine.helpers;

/**
 * Generates a unique ID for each simulation ran.
 * Will generate a number starting from 1 for each simulation.
 */
public class SimulationIdGenerator {
    private static int counter = 1;

    /**
     * @return int ID generated.
     */
    public static int getID() {
        return counter++;
    }
}
