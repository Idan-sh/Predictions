package com.idansh.engine.actions.condition;

import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;

public class SingleConditionAction extends ConditionAction{
    private final String propertyName;
    private final ConditionOperator operator;
    private final Expression expression;

    /**
     * Checks if a single condition is met,
     * if the condition is met then the "then" actions will be performed,
     * if not then the "else" actions will be preformed (if exist).
     * Referred by the singularity value "single"
     * @param propertyName name of the property that will be checked by the condition
     * @param operator     comparison operator for the condition.
     *                     possible values:  = (equal) / != (not equal) / bt (greater than) / lt (less than)
     * @param value        a number that will be compared to the property's value
     */
    public SingleConditionAction(World worldContext, String entityContext, String propertyName, String operator, Expression value, ThenOrElseActions thenActions, ThenOrElseActions elseActions) {
        super(worldContext, entityContext, Type.SINGLE, thenActions, elseActions);
        this.propertyName = propertyName;
        this.expression = value;
        this.operator = ConditionOperator.getConditionOperator(operator); // Try to convert to ConditionOperator, if fails throw exception
    }

    @Override
    public String getActionTypeString() {
        return "condition";
    }

    @Override
    public void invoke() {
        Object propertyValue = getWorldContext().entityManager.getEntityFactory(getEntityContext()).getPropertyFactory(propertyName).getValue();

        switch (operator) {
            case EQUAL:
                super.invokeActionsSet(isEqual(propertyValue, expression.getValue()));
                break;

            case NOT_EQUAL:
                super.invokeActionsSet(!isEqual(propertyValue, expression.getValue()));
                break;

            case LESS_THAN:
                // Check if the property's value is less than the expression's value
                super.invokeActionsSet(isLessThan(propertyValue, expression.getValue()));
                break;

            case MORE_THAN:
                // Check if the property's value is more than the expression's value
                super.invokeActionsSet(isLessThan(expression.getValue(), propertyValue));
                break;
        }
    }


    /**
     * Checks if the left numeric object is less than the right numeric object.
     * Accepts only Integer/Float numbers.
     * @return true if the left number is less than the right number, false otherwise.
     */
    private boolean isLessThan(Object left, Object right) {
        if(left instanceof Integer && right instanceof Integer ) {
            return (int) left < (int) right;
        } else if(left instanceof Float && right instanceof Float) {
            return (float) left < (float) right;
        } else throw new IllegalArgumentException("can only perform less than on numeric values" +
                " of the same type! got types \"" + left.getClass() + "\" and \"" + right.getClass() + "\"");
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
            throw new IllegalArgumentException("can only perform less than on numeric values" +
                " of the same type! got types \"" + obj1.getClass() + "\" and \"" + obj2.getClass() + "\"");
    }
}
