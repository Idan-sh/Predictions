package com.idansh.engine.actions.condition;

/**
 * Condition operators that condition actions can perform.
 */
public enum ConditionOperator {
    EQUAL, NOT_EQUAL, LESS_THAN, MORE_THAN;

    public static ConditionOperator getConditionOperator(String operatorString) {
        switch(operatorString) {
            case "=":
                return EQUAL;

            case "!=":
                return NOT_EQUAL;

            case "lt":
                return LESS_THAN;

            case "bt":
                return MORE_THAN;

            default:
                throw new IllegalArgumentException("invalid condition operator \"" + operatorString + "\" received!");
        }
    }
}
