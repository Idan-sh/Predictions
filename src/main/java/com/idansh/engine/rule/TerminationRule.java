package com.idansh.engine.rule;

/**
 * Determines a single term/rule that when met will end the simulation.
 */
public class TerminationRule {
    /**
     * Sets the type of termination rule to be:
     * 0. TICKS -           maximum clock tick to reach.
     * 1. SECONDS -         number of seconds for a timer to be set by.
     * 2. USER_DEFINED-     user will stop the simulation manually.
     */
    public enum Type {
        TICKS, SECONDS, USER_DEFINED;

        /**
         * @param type TerminationRule.Type object of the termination rule.
         * @return the termination rule type in string format.
         */
        public static String getTypeString(Type type){
            switch (type) {
                case TICKS:
                    return "ticks";
                case SECONDS:
                    return "seconds";
                case USER_DEFINED:
                    return "user-defined";
                default:
                    throw new RuntimeException("termination rule type received is invalid!");
            }
        }
    }
    Type type;      // the type of the termination rule
    Integer value;  // nof ticks/seconds, if user defined will be null

    public TerminationRule(Type type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }
}
