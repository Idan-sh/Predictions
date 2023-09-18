package com.idansh.engine.actions.condition;

import com.idansh.dto.action.ActionDTO;
import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;


/**
 * Defines a single condition, defined by two Expressions which will be compared by a given Operator.
 * When the condition is invoked, the comparison will be performed.
 * When the condition is invoked:
 *      If resulted "true", a collection of "then" actions will be invoked,
 *      otherwise (resulted "false"), a collection of "else" actions will be invoked.
 */
public class SingleConditionAction extends ConditionAction{
    private final Expression functionExpression;
    private final ConditionOperator operator;
    private final Expression expression;

    /**
     * @param worldContext  The simulated world in which the action is defined.
     * @param mainEntityContext Name of the main entity on which the condition is set.
     * @param secondaryEntity Name of the secondary entity on which the condition is set.
     * @param operator      comparison operator for the condition.
     *                      possible values:  = (equal) / != (not equal) / bt (greater than) / lt (less than)
     * @param value         a number that will be compared to the property's value
     */
    public SingleConditionAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityName, Expression functionExpression, String operator, Expression value, ThenOrElseActions thenActions, ThenOrElseActions elseActions, boolean isMainCondition) {
        super(worldContext, mainEntityContext, secondaryEntity, entityName, thenActions, elseActions, isMainCondition);
        this.functionExpression = functionExpression;
        this.expression = value;
        this.operator = ConditionOperator.getConditionOperator(operator); // Try to convert to ConditionOperator, if fails throw exception
    }

    /**
     * @param worldContext  The simulated world in which the action is defined.
     * @param mainEntityContext Name of the main entity on which the condition is set.
     * @param operator      comparison operator for the condition.
     *                      possible values:  = (equal) / != (not equal) / bt (greater than) / lt (less than)
     * @param value         a number that will be compared to the property's value
     */
    public SingleConditionAction(World worldContext, String mainEntityContext, String entityName, Expression functionExpression, String operator, Expression value, ThenOrElseActions thenActions, ThenOrElseActions elseActions, boolean isMainCondition) {
        super(worldContext, mainEntityContext, entityName, thenActions, elseActions, isMainCondition);
        this.functionExpression = functionExpression;
        this.expression = value;
        this.operator = ConditionOperator.getConditionOperator(operator); // Try to convert to ConditionOperator, if fails throw exception
    }


    @Override
    public String getActionTypeString() {
        return "condition";
    }


    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        Object propertyValue = functionExpression.getValue(mainEntity, secondaryEntity);
        boolean res;

        switch (operator) {
            case EQUAL:
                res = isEqual(propertyValue, expression.getValue(mainEntity, secondaryEntity));
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(mainEntity, secondaryEntity, res);
                break;

            case NOT_EQUAL:
                res = !isEqual(propertyValue, expression.getValue(mainEntity, secondaryEntity));
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(mainEntity, secondaryEntity, res);
                break;

            case LESS_THAN:
                // Check if the property's value is less than the expression's value
                res = isLessThan(propertyValue, expression.getValue(mainEntity, secondaryEntity));
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(mainEntity, secondaryEntity, res);
                break;

            case MORE_THAN:
                // Check if the property's value is more than the expression's value
                res = isLessThan(expression.getValue(mainEntity, secondaryEntity), propertyValue);
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(mainEntity, secondaryEntity, res);
                break;
        }    }


    @Override
    public void invoke(Entity entityInstance) {
        Object propertyValue = functionExpression.getValue(entityInstance);
        boolean res;

        switch (operator) {
            case EQUAL:
                res = isEqual(propertyValue, expression.getValue(entityInstance));
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(entityInstance, res);
                break;

            case NOT_EQUAL:
                res = !isEqual(propertyValue, expression.getValue(entityInstance));
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(entityInstance, res);
                break;

            case LESS_THAN:
                // Check if the property's value is less than the expression's value
                res = isLessThan(propertyValue, expression.getValue(entityInstance));
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(entityInstance, res);
                break;

            case MORE_THAN:
                // Check if the property's value is more than the expression's value
                res = isLessThan(expression.getValue(entityInstance), propertyValue);
                if (res) setActivated(true);
                if (isMainCondition())
                    invokeActionsSet(entityInstance, res);
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

        return new SingleConditionAction(
                worldContext,
                getMainEntityContext(),
                getSecondaryEntity(),
                getEntityToInvokeOnName(),
                functionExpression,
                ConditionOperator.getConditionOperatorString(operator),
                expression,
                thenActions,
                elseActions,
                isMainCondition()
        );
    }


    @Override
    public ActionDTO getActionDTO() {
        ActionDTO retActionDTO = getBasicActionDTO();

        retActionDTO.addArgument("Entity Name", getEntityToInvokeOnName());
        retActionDTO.addArgument("Property", functionExpression.getAsString());
        retActionDTO.addArgument("Operator", ConditionOperator.getConditionOperatorString(operator));

        retActionDTO.addExtraInfo("Amount of 'then' Actions", Integer.valueOf(getThenActions().getActionsToInvoke().size()).toString());
        retActionDTO.addExtraInfo("Amount of 'else' Actions", Integer.valueOf(getElseActions().getActionsToInvoke().size()).toString());

        return retActionDTO;
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
