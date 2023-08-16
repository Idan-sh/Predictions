package com.idansh.engine.actions;

import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.world.World;

/**
 * Perform a mathematical calculation using two arguments, and saves the result
 * in the property defined.
 * Possible calculations: Multiply / Divide
 * Receives two argument expressions (arg1, arg2) that will be used for the calculation.
 */
public class CalculationAction extends Action {
    public enum Type {
        MULTIPLY, DIVIDE
    }

    private final String propertyName;
    private final Expression arg1, arg2;
    private final Type type;

    /**
     * Perform a mathematical calculation on a value of a property of the entity,
     * Using two argument expressions.
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param entityContext name entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     */
    public CalculationAction(World worldContext, String entityContext, String propertyName, Expression arg1, Expression arg2, Type type) {
        super(worldContext, entityContext);
        this.propertyName = propertyName;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type;
    }


    public void invoke() {
        Property propertyToSave = super.getWorldContext().entityManager.getEntityInPopulation(getEntityContext()).getPropertyByName(propertyName);

        Object val1 = arg1.getValue();
        Object val2 = arg2.getValue();

        if(!propertyToSave.isNumericProperty())
            throw new IllegalArgumentException("Error: can preform calculation only on numeric properties!");

        if(!isNumber(val1) || !isNumber(val2))
            throw new IllegalArgumentException("Error: can preform calculation only on numeric arguments!");

        // Perform action according to the type
        switch(type) {
            case MULTIPLY:
                if((val1 instanceof Float) || (val2 instanceof Float))
                    propertyToSave.setValue((Float) val1 * (Float) val2);
                else
                    propertyToSave.setValue((Integer) val1 * (Integer) val2);
                break;
            case DIVIDE:
                if((val1 instanceof Float) || (val2 instanceof Float))
                    propertyToSave.setValue((Float) val1 / (Float) val2);
                else
                    propertyToSave.setValue((Integer) val1 / (Integer) val2);
                break;
        }
    }


    private boolean isNumber(Object obj) {
        return (obj instanceof Integer) || (obj instanceof Float);
    }

}
