package com.idansh.jaxb.unmarshal.converter;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.expression.functions.EnvironmentFunction;
import com.idansh.engine.expression.functions.FunctionActivation;
import com.idansh.engine.expression.functions.RandomFunction;
import com.idansh.engine.expression.property.PropertyExpression;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.world.World;
import com.idansh.jaxb.schema.generated.PRDAction;

/**
 * Class that converts expressions from generated XML file classes to Expression object,
 * which the simulation can use.
 */
public class ExpressionConverter {
    private final ActiveEnvironmentVariables environmentVariables;
    private final EntityManager entityManager;

    public ExpressionConverter(World world) {
        this.environmentVariables = world.getActiveEnvironmentVariables();
        this.entityManager = world.entityManager;
    }

    /**
     * Converts a PRDAction object that was read from the XML file
     * into an Expression Object.
     * @param prdAction PRDAction object that was read from the XML file.
     * @return Action object with the data of the PRDAction received.
     */
    public Expression convertExpression(PRDAction prdAction) {
        Expression retExpression;

        // Try to convert to FunctionExpression
        retExpression = getFunctionExpression(prdAction.getBy());

        // Try to convert to PropertyExpression
        if(retExpression == null) {
            retExpression = getPropertyExpression(prdAction);
        }

        // Convert to FixedExpression
        if(retExpression == null) {

        }

        return retExpression;
    }


    /**
     * Analyze the value string from the PRDAction, in case the given string represent a function, a property or just a regular value.
     * The method also checks whether the object matches the property's value type and the action's type.
     * if yes, the method return the requested object
     * Otherwise, an exception thrown in order to stop this action object creation.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file
     * @param prdValueStr the given value name from the given PRDTAction generated from reading the XML file.
     * The name sent separately in order to analyze the two arguments of 'Calculation' action too.
     * @return the value requested object.
     */
    public Object analyzeAndGetValue(PRDAction prdAction, String prdValueStr){
        Object value;
        value = getObjectIfFunction(prdValueStr);
        if(value == null){
            value = getIfProperty(prdAction, prdValueStr);
        }
        if(value == null){
            value = parseValue(prdValueStr);
        }
        if(!compareActionValueToGivenPropertyValue(prdAction, value)){
            // validation error occurred.
            throw new RuntimeException();
        }

        return value;
    }


    // -------------------------- Function --------------------------

    /**
     * Checks if the PRDAction is a function expression.
     * If it is then returns the proper function expression, otherwise returns null.
     * @param prdStr PRDAction's value string.
     */
    private Expression getFunctionExpression(String prdStr) {
        Expression retFunctionExpression = null;
        String functionName = getFunctionName(prdStr);

        if (functionName != null) {
            switch (FunctionActivation.Type.valueOf(functionName)) {
                case ENVIRONMENT:
                    retFunctionExpression = new EnvironmentFunction(environmentVariables, getFunctionArgument(prdStr));
                    break;

                case RANDOM:
                    retFunctionExpression = new RandomFunction(Integer.parseInt(getFunctionArgument(prdStr)));
                    break;

                case EVALUATE:
                    retFunctionExpression = null;
                    break;

                case PERCENT:
                    retFunctionExpression = null;
                    break;

                case TICKS:
                    retFunctionExpression = null;
                    break;
            }
        }

        return retFunctionExpression;
    }


    /**
     * Get the function's name from a string, if the string does not contain one, return null.
     * @param str string value of PRDAction from the input XML file.
     */
    private String getFunctionName(String str) {
        String retStr = null;
        int ind = str.indexOf("(");

        if(ind != -1)
            retStr = str.substring(0, ind);

        return retStr;
    }


    /**
     * Get the function's argument from a string, if the string does not contain one, return null.
     * @param str string value of PRDAction from the input XML file.
     */
    private String getFunctionArgument(String str){
        String ret = null;
        int openInd = str.indexOf("(");
        int closeInd = str.indexOf(")");

        if (openInd != -1 && closeInd != -1 && closeInd > openInd) {
            ret = str.substring(openInd + 1, closeInd);
        }

        return ret;
    }


    // -------------------------- Property --------------------------

    /**
     * Checks if the PRDAction is a property expression.
     * If it is then returns a new property expression with its name, otherwise returns null.
     * @param prdAction PRDAction received from the XML file.
     */
    private Expression getPropertyExpression(PRDAction prdAction) {
        try{
            // Try to get an entity with the given name, if one does not exist continue
            entityManager.getEntityInPopulation(prdAction.getEntity());
            return new PropertyExpression(prdAction.getBy());
        } catch (IllegalArgumentException ignored) { }
        return null;
    }


    // ------------------------ Fixed Value ------------------------


    /**
     * If 'analyzeAndGetValue' enter this method, the given value from the XML is not a function or a property.
     * This method parse the string to one of the following types: Integer, Double, Boolean or String.
     *
     * @param prdValueStr the PRDAction value string.
     * @return the given value string parse into one of the four types.
     */
    private Object parseValue(String prdValueStr){
        Boolean flag = false;
        Object ret = null;

        ret = getIntOrDouble(prdValueStr, flag);
        if(flag){
            ret = getBooleanOrStr(prdValueStr);
        }

        return ret;
    }

    /**
     * 'parseValue' helper, the boolean flag is an input member in order to change his value for the next checks outside this method.
     */
    private Object getIntOrDouble(String prdValueStr, Boolean flag){
        Object ret = null;

        try{
            ret = Integer.parseInt(prdValueStr);
        }
        catch (NumberFormatException e){
            flag = true;
        }
        if(flag){
            try{
                ret = Double.parseDouble(prdValueStr);
            }
            catch (NumberFormatException e) {
                flag = true;
            }
        }

        return ret;
    }

    /**
     * 'parseValue' helper.
     */
    private Object getBooleanOrStr(String prdValueStr){
        Object ret;
        try {
            ret = Boolean.valueOf(prdValueStr);
        }
        catch (IllegalArgumentException e){
            ret = prdValueStr;
        }

        return ret;
    }



    /**
     * After the object has been decodes, this method checks whether the object matches the property's value type and the action's type.
     * If not, return false.
     *
     * @param prdAction the given PRDTAction generated from reading the XML file
     * @param value the action value object.
     * @return true if the object complete the checks successfully, otherwise return false.
     */
    private boolean compareActionValueToGivenPropertyValue(PRDAction prdAction, Object value){
        boolean ret = true;

        if (value instanceof Integer) {
            ret = compareIntegerOrDoubleCase(prdAction);
        } else if (value instanceof Double) {
            ret = compareIntegerOrDoubleCase(prdAction);
        } else if (value instanceof Boolean) {
            ret = compareBooleanCase(prdAction);
        } else if (value instanceof String) {
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