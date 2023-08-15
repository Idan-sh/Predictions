package com.idansh.jaxb.unmarshal.validator;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.jaxb.schema.generated.PRDAction;

/**
 * Validation methods for expressions created from XML file data.
 */
public class ExpressionValidator {
    /**
     * Check if the expression's value is of the type of the property's value, and is compliant with action received.
     * @param prdAction the given PRDTAction generated from reading the XML file
     * @param value the expression created.
     * @return if all check completed successfully returns true, otherwise returns false.
     */
    private boolean compareActionValueToGivenPropertyValue(PRDAction prdAction, Object expressionValue){
        boolean ret = true;

        if (expressionValue instanceof Integer || expressionValue instanceof Double) {
            ret = compareIntegerOrDoubleCase(prdAction);
        } else if (expressionValue instanceof Boolean) {
            ret = compareBooleanCase(prdAction);
        } else if (expressionValue instanceof String) {
            ret = compareStringCase(prdAction);
        }

        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for integer or double actions/properties.
     */
    private boolean compareIntegerOrDoubleCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type != ActionType.INCREASE && type != ActionType.DECREASE && type != ActionType.CALCULATION && type != ActionType.CONDITION)
        {
            addErrorToList(prdAction, prdAction.getValue(), "Action type not allowed");
            ret = false;
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("INT")) && (!propertyType.name().equals("DOUBLE"))){
            addErrorToList(entity.getProperties().get(propertyName), propertyType.name(), "The property value type doesn't match the action value type");
            ret= false;
        }

        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for boolean actions/properties.
     */
    private boolean compareBooleanCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        PRDCondition prdCondition;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            addErrorToList(prdAction, prdAction.getValue(), "Action type not allowed");
            ret = false;
        }

        if(type == ActionType.CONDITION){
            prdCondition = prdAction.getPRDCondition();
            if(prdCondition.getSingularity().equals("single") && (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))){
                addErrorToList(prdAction, prdAction.getValue(), "Condition operator type not allowed");
                ret = false;
            }
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("BOOLEAN"))){
            addErrorToList(prdAction, prdAction.getValue(), "The property value type doesn't match the action value type");
            ret = false;
        }

        return ret;
    }

    /**
     * 'compareActionValueToGivenPropertyValue' helper for String actions/properties.
     */
    private boolean compareStringCase(PRDAction prdAction){
        String actionType = prdAction.getType(), entityName = prdAction.getEntity(), propertyName = prdAction.getProperty();
        Entity entity = entities.get(entityName);
        ActionType type = ActionType.valueOf(actionType);
        PropertyType propertyType;
        boolean ret = true;

        if(type == ActionType.INCREASE || type == ActionType.DECREASE || type == ActionType.CALCULATION) {
            addErrorToList(prdAction, prdAction.getValue(), "Action type not allowed");
            ret = false;
        }

        propertyType = entity.getProperties().get(propertyName).getType();
        if ((!propertyType.name().equals("STRING"))){
            addErrorToList(prdAction, prdAction.getValue(), "The property value type doesn't match the action value type");
            ret = false;
        }

        return ret;
    }
}
