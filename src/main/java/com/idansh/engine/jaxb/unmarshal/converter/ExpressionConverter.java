package com.idansh.engine.jaxb.unmarshal.converter;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.expression.fixed.FixedValueExpression;
import com.idansh.engine.expression.functions.*;
import com.idansh.engine.expression.property.PropertyExpression;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.world.World;
import javafx.util.Pair;

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
        retExpression = getFunctionExpression(entityName, prdStr);

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
     * @throws RuntimeException if the expression created is not compatible with the action's type.
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
                if(!propertyType.isNumeric())
                    throw new RuntimeException("cannot use expression of type \"" + expressionType + "\" in the action \"" + actionType + "\" on the property of type \"" + propertyType + "\"");
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
    private Expression getFunctionExpression(String entityName, String prdStr) {
        Expression retFunctionExpression = null;
        String functionName = getFunctionName(prdStr);
        Pair<String, String> entityPropertyPair;

        if (functionName != null) {
            switch (FunctionActivationExpression.Type.getType(functionName)) {
                case ENVIRONMENT:
                    retFunctionExpression = new EnvironmentFunctionExpression(
                            environmentVariables, getFunctionArgument(prdStr));
                    break;

                case RANDOM:
                    retFunctionExpression = new RandomFunctionExpression(
                            Integer.parseInt(getFunctionArgument(prdStr)));
                    break;

                case EVALUATE:
                    entityPropertyPair = getEntityPropertyFunctionArguments(prdStr);
                    checkEntityContext(entityPropertyPair, entityName);

                    retFunctionExpression = new EvaluateFunctionExpression(
                            entityManager, entityPropertyPair.getKey(), entityPropertyPair.getValue());
                    break;

                case PERCENT:
                    retFunctionExpression = new PercentFunctionExpression();
                    break;

                case TICKS:
                    entityPropertyPair = getEntityPropertyFunctionArguments(prdStr);
                    checkEntityContext(entityPropertyPair, entityName);

                    retFunctionExpression = new TicksFunctionActivation(
                            entityPropertyPair.getKey(), entityPropertyPair.getValue());
                    break;
            }
        }

        return retFunctionExpression;
    }


    /**
     * Check if the entity context equals to the entity received in the function expression's argument.
     * @throws IllegalArgumentException in case that the entity context name doesn't equal
     * to the function argument's entity name.
     */
    private void checkEntityContext(Pair<String, String> entityPropertyPair, String entityContext) {
        if(!entityContext.equals(entityPropertyPair.getKey()))
            throw new IllegalArgumentException("Cannot evaluate with entity " + entityPropertyPair.getKey()
                    + " on entity context " + entityContext + ". Evaluate only works on an argument with an entity name " +
                    "that equals to the entity in context.");
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


    /**
     * Get a pair of entity name and property name from a string in the format of:
     * "<function_name>(<first_word>.<second_word>)".
     * @param str str in the format specified.
     * @return a pair where the first word is the entity name, and the second word is the property name.
     */
    private Pair<String, String> getEntityPropertyFunctionArguments(String str) {
        String withoutParenthesis = getFunctionArgument(str);

        String[] parts = withoutParenthesis.split("\\.");

        if(parts.length == 2) {
            if(parts[0].isEmpty() || parts[1].isEmpty())
                throw new IllegalArgumentException("Invalid entity-property argument, " +
                        "at least one of the words received is empty.");

            return new Pair<>(parts[0], parts[1]);
        } else
            throw new IllegalArgumentException("Invalid entity-property argument, " +
                    "format should be \"<first_word>.<second_word>\".");
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
     * Float, Boolean, String,
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
            // Try to convert to float
            retValue = Float.parseFloat(prdStr);
            retType = PropertyType.FLOAT;
        } catch (NumberFormatException notFloat) {
            // Try to convert to boolean
            if (prdStr.equalsIgnoreCase("true")) {
                retValue = true;
                retType = PropertyType.BOOLEAN;
            } else if (prdStr.equalsIgnoreCase("false")) {
                retValue = false;
                retType = PropertyType.BOOLEAN;
            } else { // Keep as string
                retValue = prdStr;
                retType = PropertyType.STRING;
            }
        }

        return new FixedValueExpression(retValue, retType);
    }
}