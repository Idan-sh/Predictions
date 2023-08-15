package com.idansh.engine.actions;

import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;

/**
 * Perform a mathematical calculation on a value of a property of the entity.
 * Possible calculations: Multiply / Divide
 * Receives two argument expressions (arg1, arg2) that will be used for the calculation.
 */
public class CalculationAction extends Action {
    private final String propertyName;
    private final Expression arg1, arg2;

    /**
     * Perform a mathematical calculation on a value of a property of the entity,
     * Using two argument expressions.
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param entityContext name entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     */
    public CalculationAction(World worldContext, String entityContext, String propertyName, Expression arg1, Expression arg2) {
        super(worldContext, entityContext);
        this.propertyName = propertyName;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }


    public void invoke() {
        // todo- complete calculation
    }

}
