package com.idansh.jaxb.unmarshal.converter;

import com.idansh.engine.entity.Entity;
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
import com.idansh.jaxb.schema.generated.PRDAction;

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
     * @param prdAction PRDAction object that was read from the XML file.
     * @return Action object with the data of the PRDAction received.
     */
    public Expression convertExpression(PRDAction prdAction, String prdStr) {
        System.out.println("trying to convert expression with- prdStr " + prdStr + " and prdAction " + prdAction.getType());
        Expression retExpression;

        // Try to convert to FunctionExpression
        retExpression = getFunctionExpression(prdStr);

        // Try to convert to PropertyExpression
        if(retExpression == null) {
            retExpression = getPropertyExpression(prdAction, prdStr);
        }

        // Convert to FixedExpression
        if(retExpression == null) {
            retExpression = getFixedValue(prdStr);
        }

        // Check for type error
        validateValueType(prdAction, retExpression);

        return retExpression;
    }


    /**
     * Validates that an expression created by expressionConvert is compatible with the action's type.
     * @param prdAction the action received from the XML file.
     */
    private void validateValueType(PRDAction prdAction, Expression expression) {
        System.out.println("Validating value type with prdAction " + prdAction.getType());
        PropertyType expressionType = expression.getType();
        String actionType = prdAction.getType();

        // Check if the action is of type condition
        if(prdAction.getPRDCondition() != null) {
            PropertyType propertyType = entityManager.getEntityFactory(prdAction.getEntity()).getPropertyFactory(prdAction.getPRDCondition().getProperty()).getType();

            if(!propertyType.equals(expressionType))
                throw new RuntimeException("cannot check condition on expression of type \"" + expressionType + "\" with the property of type \"" + propertyType + "\"");

        } else {
            // Check if action is of type increase/decrease/set
            if(actionType.equals("increase") || actionType.equals("decrease") || actionType.equals("set")) {
                PropertyType propertyType = entityManager.getEntityFactory(prdAction.getEntity()).getPropertyFactory(prdAction.getProperty()).getType();
                if(!propertyType.equals(expressionType))
                    throw new RuntimeException("cannot save expression of type \"" + expressionType + "\" in the action \"" + actionType + "\" to the property of type \"" + propertyType + "\"");
            } // Action type is of calculation
            else if (actionType.equals("calculation")) {
                PropertyType propertyType = entityManager.getEntityFactory(prdAction.getEntity()).getPropertyFactory(prdAction.getResultProp()).getType();
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
     * Checks if the PRDAction is a property expression.
     * If it is then returns a new property expression with its name, otherwise returns null.
     * @param prdAction PRDAction received from the XML file.
     */
    private Expression getPropertyExpression(PRDAction prdAction, String prdStr) {
        try{
            String entityName = prdAction.getEntity();

            // Try to get an entity with the given name, if one does not exist continue
            Entity entity = entityManager.getEntityInPopulation(entityName);
            if(entity == null)
                return null;

            // Try to get the entity's property with the given name, if one does not exist continue
            entity.getPropertyByName(prdStr);

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