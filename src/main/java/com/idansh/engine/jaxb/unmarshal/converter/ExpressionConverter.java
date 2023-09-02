package com.idansh.engine.jaxb.unmarshal.converter;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.expression.fixed.FixedValueExpression;
import com.idansh.engine.expression.functions.EnvironmentFunctionExpression;
import com.idansh.engine.expression.functions.FunctionActivationExpression;
import com.idansh.engine.expression.functions.RandomFunctionExpression;
import com.idansh.engine.expression.property.PropertyExpression;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.world.World;

/**
 * Class that converts expressions from generated XML file classes to Expression object,
 * which the simulation can use.
 */
public class ExpressionConverter {
    private final ActiveEnvironmentVariables environmentVariables;
    private final EntityManager entityManager;
    private final World worldContext;

    public ExpressionConverter(World world) {
        this.environmentVariables = world.getActiveEnvironmentVariables();
        this.entityManager = world.entityManager;
        this.worldContext = world;
    }


    /**
     * Converts a PRDAction object that was read from the XML file
     * into an Expression Object.
     * Checks if the expression's value's typ matches the action's type.
     * If the type matches, returns the Expression object, otherwise throws exception.
     * @return Action object with the data of the PRDAction received.
     */
    public Expression convertExpression(String actionType, String entityName, String propertyName, String prdStr) {
        Expression retExpression;

        // Try to convert to FunctionExpression
        retExpression = getFunctionExpression(prdStr);

        // Try to convert to PropertyExpression
        if(retExpression == null) {
            retExpression = getPropertyExpression(entityName, prdStr);
        }

        // Convert to FixedExpression
        if(retExpression == null) {
            retExpression = getFixedValue(prdStr);
        }

        // Check for type error
        validateValueType(actionType, entityName, propertyName, retExpression);

        return retExpression;
    }


    /**
     * Validates that an expression created by expressionConvert is compatible with the action's type.
     * @param actionType type action in which the expression is set.
     * @param entityName name of the entity defined in the action.
     * @param propertyName name of the property defined in the action.
     * @param expression expression that was defined in the action.
     */
    private void validateValueType(String actionType, String entityName, String propertyName, Expression expression) {
        PropertyType expressionType = expression.getType();

        // Check if the action is of type condition
        if(actionType.equals("condition")) {
            PropertyType propertyType = entityManager.getEntityFactory(entityName).getPropertyFactory(propertyName).getType();

            // Check if types are not equal and if at least one of them is not numeric (if both are numeric it is possible to convert between the types)
            if(!propertyType.equals(expressionType) && ((!propertyType.isNumeric()) || (!expressionType.isNumeric()))) {
                throw new RuntimeException("cannot check condition on expression of type \"" + expressionType + "\" with the property of type \"" + propertyType + "\"");
            }

        } else {
            // Check if action is of type increase/decrease/set
            if(actionType.equals("increase") || actionType.equals("decrease") || actionType.equals("set")) {
                PropertyType propertyType = entityManager.getEntityFactory(entityName).getPropertyFactory(propertyName).getType();
                if(!propertyType.equals(expressionType))
                    throw new RuntimeException("cannot save expression of type \"" + expressionType + "\" in the action \"" + actionType + "\" to the property of type \"" + propertyType + "\"");
            } // Action type is of calculation
            else if (actionType.equals("calculation")) {
                PropertyType propertyType = entityManager.getEntityFactory(entityName).getPropertyFactory(propertyName).getType();
                if(!propertyType.equals(expressionType))
                    throw new RuntimeException("cannot save expression of type \"" + expressionType + "\" in the action \"" + actionType + "\" to the property of type \"" + propertyType + "\"");
            } // Illegal action type
            else
                throw new RuntimeException("cannot validate expression's value type, action type \"" + actionType + "\" is invalid!");
        }
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
            switch (FunctionActivationExpression.Type.getType(functionName)) {
                case ENVIRONMENT:
                    retFunctionExpression = new EnvironmentFunctionExpression(environmentVariables, getFunctionArgument(prdStr));
                    break;

                case RANDOM:
                    retFunctionExpression = new RandomFunctionExpression(Integer.parseInt(getFunctionArgument(prdStr)));
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
     * Checks if the expression is a property expression.
     * If it is then returns a new property expression with its data, otherwise returns null.
     * @param entityName name of the entity in which the property will be searched.
     */
    private Expression getPropertyExpression(String entityName, String prdStr) {
        try{
            // Try to get an entity with the given name, if one does not exist continue
            EntityFactory entityFactory = entityManager.getEntityFactory(entityName);
            if(entityFactory == null)
                return null;

            // Try to get the entity's property with the given name, if one does not exist continue
            entityFactory.getPropertyFactory(prdStr);

            return new PropertyExpression(worldContext, entityName, prdStr);
        } catch (IllegalArgumentException ignored) { }
        return null;
    }


    // ------------------------ Fixed Value ------------------------


    /**
     * Parses a string to one of the following:
     * Integer, Double, Boolean, String,
     * and creates a FixedValueExpression with the resulted value.
     * Will try to convert to integer, then to float, then to boolean,
     * and if all fails will keep as string.
     * @param prdStr PRDAction's value string.
     * @return a FixedValueExpression with the resulted converted value.
     */
    private Expression getFixedValue(String prdStr){
        Object retValue;
        PropertyType retType;

        try {
            // Try to convert to integer
            retValue = Integer.parseInt(prdStr);
            retType = PropertyType.INTEGER;
        } catch (NumberFormatException notInt) {
            try {
                // Try to convert to float
                retValue = Float.parseFloat(prdStr);
                retType = PropertyType.FLOAT;
            } catch (NumberFormatException notFloat) {
                // Try to convert to boolean
                if(prdStr.equalsIgnoreCase("true")) {
                    retValue = true;
                    retType = PropertyType.BOOLEAN;
                }
                else if (prdStr.equalsIgnoreCase("false")) {
                    retValue = false;
                    retType = PropertyType.BOOLEAN;
                }
                else { // Keep as string
                    retValue = prdStr;
                    retType = PropertyType.STRING;
                }
            }
        }

        return new FixedValueExpression(retValue, retType);
    }
}