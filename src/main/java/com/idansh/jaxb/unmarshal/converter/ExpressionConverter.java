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

//        // Check for type error
//        if(!compareActionValueToGivenPropertyValue(prdAction, value)){
//            throw new RuntimeException("Error: the created expression's type does not match the action's type!");
//        }

        return retExpression;
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

        try {
            // Try to convert to integer
            retValue = Integer.parseInt(prdStr);
        } catch (NumberFormatException notInt) {
            try {
                // Try to convert to float
                retValue = Float.parseFloat(prdStr);
            } catch (NumberFormatException notFloat) {
                // Try to convert to boolean
                if(prdStr.equalsIgnoreCase("true"))
                    retValue = true;
                else if (prdStr.equalsIgnoreCase("false"))
                    retValue = false;
                else // Keep as string
                    retValue = prdStr;
            }
        }

        // todo- check value for type error

        return new FixedValueExpression(retValue);
    }
}