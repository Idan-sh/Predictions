package com.idansh.engine.jaxb.unmarshal.converter;

import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.expression.fixed.FixedValueExpression;
import com.idansh.engine.expression.functions.*;
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
     * Checks if the expression's value's type matches the action's type.
     * If the type matches, returns the Expression object, otherwise throws exception.
     *
     * @param mainEntityName        Name of the main entity in context.
     * @param secondaryEntity       The secondary entity in context (optional).
     * @param propertyName          Name of the property in context.
     * @param actionType            Type of the action in which the expression is defined.
     * @param expressionStr         String that contains expression to be converted.
     * @return                      Converted Expression according to the string received.
     */
    public Expression convertExpression(String actionType, String mainEntityName, SecondaryEntity secondaryEntity, String propertyName, String expressionStr) {
        Expression retExpression;

        // Try to convert to FunctionExpression
        retExpression = getFunctionExpression(actionType, mainEntityName, secondaryEntity, propertyName, expressionStr);

        // Try to convert to PropertyExpression
        if (retExpression == null) {
            retExpression = getPropertyExpression(mainEntityName, expressionStr);
        }

        // Convert to FixedExpression
        if (retExpression == null) {
            retExpression = getFixedValue(expressionStr);
        }

        // Check for errors in the created expression
        validateValueType(actionType, mainEntityName, secondaryEntity, propertyName, retExpression);

        return retExpression;
    }


    /**
     * Validates that both expressions of a condition expression created by expressionConvert are compatible.
     * @param propertyExpression Expression for the property value.
     * @param expression Expression for the value field.
     * @throws RuntimeException if the expressions are not compatible.
     */
    private void validateConditionValueType(Expression propertyExpression, Expression expression) {
        PropertyType expressionType = expression.getType();
        PropertyType propertyType = propertyExpression.getType();

        // Check if types are not equal and if at least one of them is not numeric (if both are numeric it is possible to convert between the types)
        if(!propertyType.equals(expressionType) && ((!propertyType.isNumeric()) || (!expressionType.isNumeric()))) {
            throw new RuntimeException("cannot check condition on expression of type \"" + expressionType + "\" with the property of type \"" + propertyType + "\"");
        }
    }


    /**
     * Validates that an expression created by expressionConvert is compatible with the action's type.
     * @param actionType Type action in which the expression is set.
     * @param mainEntityName Name of the main entity defined in the action.
     * @param secondaryEntity The secondary entity defined in the action.
     * @param propertyName Mame of the property defined in the action.
     * @param expression The expression to check its type.
     * @throws RuntimeException if the expression created is not compatible with the action's type.
     */
    private void validateValueType(String actionType, String mainEntityName, SecondaryEntity secondaryEntity, String propertyName, Expression expression) {
        PropertyType expressionType = expression.getType();

        // Check if action is of type increase/decrease
        if (actionType.equals("increase") || actionType.equals("decrease")) {
            PropertyType propertyType = entityManager.getEntityFactory(mainEntityName).getPropertyFactory(propertyName).getType();

            if (!propertyType.isNumeric())
                throw new RuntimeException("cannot use expression of type \"" + expressionType + "\" in the action \"" + actionType + "\" on the property of type \"" + propertyType + "\"");
        }
        // Action type is of set
        else if (actionType.equals("set")) {
            PropertyType propertyType = entityManager.getEntityFactory(mainEntityName).getPropertyFactory(propertyName).getType();

            if (!propertyType.equals(expressionType) && !(propertyType.equals(PropertyType.FLOAT) && expressionType.equals(PropertyType.INTEGER)))
                throw new RuntimeException("cannot set expression of type \"" + expressionType + "\" in the action \"" + actionType + "\" to the property of type \"" + propertyType + "\"");
        }
        // Action type is of calculation
        else if (actionType.equals("calculation")) {
            PropertyType propertyType = entityManager.getEntityFactory(mainEntityName).getPropertyFactory(propertyName).getType();

            if (!propertyType.equals(expressionType))
                throw new RuntimeException("cannot save expression of type \"" + expressionType + "\" in the action \"" + actionType + "\" to the property of type \"" + propertyType + "\"");
        }
        // Action type is of condition
        else if(actionType.equals("condition")) {
            // Create an Expression for the property, then validate its compatibility
            Expression propertyExpression = convertPropertyExpression(mainEntityName, secondaryEntity, propertyName);
            validateConditionValueType(propertyExpression ,expression);
        }
        // Check if ignored type, if not then received illegal action type
        else if (!actionType.equals("proximity")) {
            throw new RuntimeException("cannot validate expression's value type, action type \"" + actionType + "\" is invalid!");
        }
    }


    /**
     * Converts a string of an expression written in the property field of a condition action into an Expression object.
     * @param mainEntityName Name of the main entity in which the property expression is defined.
     * @param secondaryEntity The secondary entity in which the property expression is defined.
     * @param propertyValue String value to be converted into an Expression object.
     */
    public Expression convertPropertyExpression(String mainEntityName, SecondaryEntity secondaryEntity, String propertyValue) {
        Expression retExpression;

        // Try to convert to FunctionExpression
        retExpression = getFunctionExpression(
                "condition",
                mainEntityName,
                secondaryEntity,
                null,
                propertyValue);

        // Convert to PropertyExpression
        if(retExpression == null) {
            retExpression = getPropertyExpression(mainEntityName, propertyValue);
        }

        return retExpression;
    }


    // -------------------------- Function --------------------------

    /**
     * Checks if the PRDAction is a function expression.
     * If it is then returns the proper function expression, otherwise returns null.
     * @param expressionStr the string that contains the expression to convert.
     */
    private Expression getFunctionExpression(String actionType, String mainEntityName, SecondaryEntity secondaryEntity, String propertyName, String expressionStr) {
        Expression retFunctionExpression = null;
        String functionName = getFunctionName(expressionStr);
        Pair<String, String> entityPropertyPair;

        if (functionName != null) {
            switch (FunctionActivationExpression.Type.getType(functionName)) {
                case ENVIRONMENT:
                    retFunctionExpression = new EnvironmentFunctionExpression(
                            environmentVariables, getFunctionArgument(expressionStr));
                    break;

                case RANDOM:
                    retFunctionExpression = new RandomFunctionExpression(
                            Integer.parseInt(getFunctionArgument(expressionStr)));
                    break;

                case EVALUATE:
                    entityPropertyPair = getEntityPropertyFunctionArguments(expressionStr);
                    checkEntityContext(entityPropertyPair, mainEntityName, secondaryEntity);

                    retFunctionExpression = new EvaluateFunctionExpression(
                            entityManager, entityPropertyPair.getKey(), entityPropertyPair.getValue());
                    break;

                case PERCENT:
                    Pair<String, String> percentExpressions = getPercentageExpressionsFromArgument(expressionStr);
                    retFunctionExpression = new PercentFunctionExpression(
                            convertExpression(
                                    actionType, mainEntityName, secondaryEntity, propertyName, percentExpressions.getKey()),
                            convertExpression(
                                    actionType, mainEntityName, secondaryEntity, propertyName, percentExpressions.getValue())
                    );
                    break;

                case TICKS:
                    entityPropertyPair = getEntityPropertyFunctionArguments(expressionStr);
                    checkEntityContext(entityPropertyPair, mainEntityName, secondaryEntity);

                    retFunctionExpression = new TicksFunctionActivation(
                            entityPropertyPair.getKey(), entityPropertyPair.getValue());
                    break;
            }
        }

        return retFunctionExpression;
    }


    /**
     * Check if a function expression's argument is defined in the same
     * entity context as the main or secondary entities of the action.
     * Secondary entity is optional and doesn't have to be defined, send as null if undefined.
     * @param mainEntityContext The main entity in which the function expression is defined.
     * @param secondaryEntityContext The secondary entity in which the function expression is defined (optional).
     * @param entityPropertyPair Pair of entity name and property name arguments of a function expression.
     * @throws IllegalArgumentException in case that the entity context name doesn't equal to the function argument's entity name.
     */
    private void checkEntityContext(Pair<String, String> entityPropertyPair, String mainEntityContext, SecondaryEntity secondaryEntityContext) {
        if(!mainEntityContext.equals(entityPropertyPair.getKey()) && !(secondaryEntityContext != null && secondaryEntityContext.getName().equals(entityPropertyPair.getKey()))) {
            throw new IllegalArgumentException("Cannot evaluate with entity " + entityPropertyPair.getKey()
                    + " on main entity context \"" + mainEntityContext + "\", and secondary entity context \"" + (secondaryEntityContext == null ? "N/A" : secondaryEntityContext) + "\"."
                    + "\nEvaluate only works on an argument with an entity name "
                    + "that equals to the main or secondary entities in context."
            );
        }
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
    private String getFunctionArgument(String str) {
        String ret = null;
        int openInd = str.indexOf("(");
        int closeInd = str.lastIndexOf(")");

        if (openInd != -1 && closeInd != -1 && closeInd > openInd) {
            ret = str.substring(openInd + 1, closeInd);
        }

        return ret;
    }


    /**
     * Get two arguments from a function's argument, each string representing an expression.
     * The format of the string should be:
     * "<function_name>(<first_expression>,<second_expression>)".
     * @return a pair of two expression names to be converted to expression.
     */
    private Pair<String, String> getPercentageExpressionsFromArgument(String str) {
        String withoutParenthesis = getFunctionArgument(str);
        String[] parts = withoutParenthesis.split(",");

        if(parts.length == 2) {
            if(parts[0].isEmpty() || parts[1].isEmpty())
                throw new IllegalArgumentException("Invalid percentage argument, " +
                        "at least one of the expressions received is empty.");

            return new Pair<>(parts[0], parts[1]);
        } else {
            throw new IllegalArgumentException("Invalid percentage argument, " +
                    "format should be \"<first_expression>,<second_expression>\".");
        }
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

            return new EvaluateFunctionExpression(worldContext.entityManager, entityName, prdStr);
        } catch (IllegalArgumentException ignored) { }
        return null;
    }


    // ------------------------ Fixed Value ------------------------


    /**
     * Parses a string to one of the following:
     * Integer, Float, Boolean, String,
     * and creates a FixedValueExpression with the resulted value.
     * Will try to convert to Integer, then to Float, then to Boolean,
     * and if all fails will keep as String.
     * @param prdStr PRDAction's value string.
     * @return a FixedValueExpression with the resulted converted value.
     */
    private Expression getFixedValue(String prdStr){
        Object retValue;
        PropertyType retType;

        try {
            // Try to convert to Integer
            retValue = Integer.parseInt(prdStr);
            retType = PropertyType.INTEGER;
        } catch (NumberFormatException notInteger) {
            try {
                // Try to convert to Float
                retValue = Float.parseFloat(prdStr);
                retType = PropertyType.FLOAT;
            } catch (NumberFormatException notFloat) {
                // Try to convert to Boolean
                if (prdStr.equalsIgnoreCase("true")) {
                    retValue = true;
                    retType = PropertyType.BOOLEAN;
                } else if (prdStr.equalsIgnoreCase("false")) {
                    retValue = false;
                    retType = PropertyType.BOOLEAN;
                } else { // Keep as String
                    retValue = prdStr;
                    retType = PropertyType.STRING;
                }
            }
        }

        return new FixedValueExpression(retValue, retType);
    }
}