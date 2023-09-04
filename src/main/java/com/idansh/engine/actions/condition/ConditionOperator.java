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
                throw new IllegalArgumentException("invalid condition operator string \"" + operatorString + "\" received!");
        }
    }

    public static String getConditionOperatorString(ConditionOperator conditionOperator) {
        switch(conditionOperator) {
            case EQUAL:
                return "=";

            case NOT_EQUAL:
                return "!=";

            case LESS_THAN:
                return "lt";

            case MORE_THAN:
                return "bt";

            default:
                throw new IllegalArgumentException("invalid condition operator \"" + conditionOperator + "\" received!");
        }
    }
}
