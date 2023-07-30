package com.idansh.engine.expression;

/**
 * Base interface for helper functions or entity's property, or for another free value.
 */
public interface Expression {

    /**
     * Digest the expression according to the first word of its name
     * @return The first word of the name of the expression
     */
    public String getFirstWord();
}
