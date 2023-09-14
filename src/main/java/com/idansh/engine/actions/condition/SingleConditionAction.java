package com.idansh.engine.actions.condition;

import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;

import java.util.List;


/**
 * Defines a single condition, defined by two Expressions which will be compared by a given Operator.
 * When the condition is invoked, the comparison will be performed.
 * When the condition is invoked:
 *      If resulted "true", a collection of "then" actions will be invoked,
 *      otherwise (resulted "false"), a collection of "else" actions will be invoked.
 */
public class SingleConditionAction extends ConditionAction{
    private final Expression propertyExpression; // Possible types will be a PropertyExpression or a FunctionActivationExpression
    private final ConditionOperator operator;
    private final Expression expression;

    /**
     * Checks if a single condition is met,
     * if the condition is met then the "then" actions will be performed,
     * if not then the "else" actions will be preformed (if exist).
     * Referred by the singularity value "single"
     * @param worldContext  The simulated world in which the action is defined.
     * @param entityContext Name of the entity on which the condition is set.
     * @param operator      comparison operator for the condition.
     *                      possible values:  = (equal) / != (not equal) / bt (greater than) / lt (less than)
     * @param value         a number that will be compared to the property's value
     */
    public SingleConditionAction(World worldContext, String entityContext, Expression propertyExpression, String operator, Expression value, ThenOrElseActions thenActions, ThenOrElseActions elseActions, boolean isMainCondition) {
        super(worldContext, entityContext, thenActions, elseActions, isMainCondition);
        this.propertyExpression = propertyExpression;
        this.expression = value;
        this.operator = ConditionOperator.getConditionOperator(operator); // Try to convert to ConditionOperator, if fails throw exception
    }


    @Override
    public String getActionTypeString() {
        return "condition";
    }


    @Override
    public void invoke(Entity entity) {
        Object propertyValue = propertyExpression.getValue();
        boolean res;

        switch (operator) {
            case EQUAL:
                res = isEqual(propertyValue, expression.getValue());
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(entity, res);
                break;

            case NOT_EQUAL:
                res = !isEqual(propertyValue, expression.getValue());
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(entity, res);
                break;

            case LESS_THAN:
                // Check if the property's value is less than the expression's value
                res = isLessThan(propertyValue, expression.getValue());
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(entity, res);
                break;

            case MORE_THAN:
                // Check if the property's value is more than the expression's value
                res = isLessThan(expression.getValue(), propertyValue);
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(entity, res);
                break;
        }
    }


    @Override
    public Action copy(World worldContext) {
        ThenOrElseActions thenActions = null, elseActions = null;

        // Single condition can be nested inside a multi condition, and not have "Then" or "Else" actions
        if (isMainCondition()) {
            thenActions = new ThenOrElseActions(getThenActions(), worldContext);
            elseActions = new ThenOrElseActions(getElseActions(), worldContext);
        }

        return new SingleConditionAction(worldContext, getEntityContext(), propertyExpression, ConditionOperator.getConditionOperatorString(operator), expression, thenActions, elseActions, isMainCondition());
    }


    /**
     * Checks if the left numeric object is less than the right numeric object.
     * Accepts only Integer/Float numbers.
     * @return true if the left number is less than the right number, false otherwise.
     */
    private boolean isLessThan(Object left, Object right) {
        if(left instanceof Integer && right instanceof Integer)
            return (int) left < (int) right;

        if(left instanceof Integer && right instanceof Float )
            return (int) left < (float) right;

        if(left instanceof Float && right instanceof Integer )
            return (float) left < (int) right;

        if(left instanceof Float && right instanceof Float)
            return (float) left < (float) right;

        throw new IllegalArgumentException("can only perform less than on numeric values" +
                " of the same type! got types \"" + left.getClass() + "\" and \"" + right.getClass() + "\".");
    }


    /**
     * Checks if two objects' values are equal.
     * Accepts only Integer/Float/Boolean/String values.
     * @return true if the objects' values are equal, false otherwise.
     */
    private boolean isEqual(Object obj1, Object obj2) {
        if(obj1 instanceof Integer && obj2 instanceof Integer ) {
            return (int) obj1 == (int) obj2;
        } else if(obj1 instanceof Float && obj2 instanceof Float) {
            return (float) obj1 == (float) obj2;
        } else if(obj1 instanceof Boolean && obj2 instanceof Boolean) {
            return (boolean) obj1 == (boolean) obj2;
        } else if(obj1 instanceof String && obj2 instanceof String) {
            return ((String) obj1).equals((String) obj2);
        } else
            throw new IllegalArgumentException("can only check if values are equal on values" +
                " of the same type! got types \"" + obj1.getClass() + "\" and \"" + obj2.getClass() + "\".");
    }
}
