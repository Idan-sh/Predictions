package com.idansh.engine.expression;

/**
 * Base interface for - helper functions / entity's property / a free value.
 */
public interface Expression {

    /**
     * Digest the expression according to the first word of its name
     * @return The first word of the name of the expression
     */
    public String getFirstWord();


    /**
     * Finds and returns the value of an environment variable.
     * @param name The name of the environment variable.
     * @return The value of the environment variable.
     */
    public Object environment(String name);


    /**
     * Generate a random number between 0 and the received integer.
     * @param max an integer that is greater than 0
     * @return randomly generated number between 0 and max
     */
    public int random(int max);
}
