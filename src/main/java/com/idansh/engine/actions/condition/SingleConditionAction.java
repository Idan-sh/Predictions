package com.idansh.engine.actions.condition;

import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleConditionAction extends ConditionAction{
    private final String propertyName;
    private final ConditionOperator operator;
    private final Expression value;

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
        this.value = value;
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
                super.invoke(propertyValue.equals(value)); // todo - add check equal between all types in a function
                break;

            case NOT_EQUAL:
                super.invoke(!propertyValue.equals(value)); // todo - use the above function
                break;

            case LESS_THAN:
                break;  // todo - add check less than function only on numeric values

            case MORE_THAN:
                break; // todo - add more than function only on numeric values
        }
    }
}
