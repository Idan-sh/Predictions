package com.idansh.engine.actions;

import com.idansh.dto.action.ActionDTO;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
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
     * @param mainEntityContext name entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     */
    public CalculationAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityName, String propertyName, Expression arg1, Expression arg2, Type type) {
        super(worldContext, mainEntityContext, secondaryEntity, entityName);
        this.propertyName = propertyName;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type;
    }

    public CalculationAction(World worldContext, String mainEntityContext, String entityName, String propertyName, Expression arg1, Expression arg2, Type type) {
        super(worldContext, mainEntityContext, entityName);
        this.propertyName = propertyName;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type;
    }

    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        Object val1 = getArgValue(arg1, mainEntity, secondaryEntity);
        Object val2 = getArgValue(arg2, mainEntity, secondaryEntity);


        Property propertyToSave = getEntityToInvokeOn(mainEntity, secondaryEntity).getPropertyByName(propertyName);

        if (!propertyToSave.isNumericProperty())
            throw new IllegalArgumentException("can preform calculation only on numeric properties! the property if of type \"" + propertyToSave.getType() + "\".");

        if (!isNumber(val1) || !isNumber(val2))
            throw new IllegalArgumentException("can preform calculation only on numeric properties! the property if of type \"" + propertyToSave.getType() + "\".");

        // Perform action according to the type
        switch (type) {
            case MULTIPLY:
                if ((val1 instanceof Float) || (val2 instanceof Float))
                    propertyToSave.setValue((Float) val1 * (Float) val2);
                else
                    propertyToSave.setValue((Integer) val1 * (Integer) val2);
                break;
            case DIVIDE:
                if ((val1 instanceof Float) || (val2 instanceof Float))
                    propertyToSave.setValue((Float) val1 / (Float) val2);
                else
                    propertyToSave.setValue((Integer) val1 / (Integer) val2);
                break;
        }
    }

    @Override
    public void invoke(Entity entity) {
        invoke(entity, null);
    }

    private Object getArgValue(Expression arg, Entity mainEntity, Entity secondaryEntity) {
        if(secondaryEntity != null)
            return arg.getValue(mainEntity, secondaryEntity);

        return arg.getValue(mainEntity);
    }


    @Override
    public Action copy(World worldContext) {
        return new CalculationAction(
                worldContext,
                getMainEntityContext(),
                getSecondaryEntity(),
                getEntityToInvokeOnName(),
                propertyName,
                arg1,
                arg2,
                type
        );
    }


    @Override
    public ActionDTO getActionDTO() {
        ActionDTO retActionDTO = getBasicActionDTO();

        retActionDTO.addArgument("Entity Name", getEntityToInvokeOnName());
        retActionDTO.addArgument("Result Property Name", propertyName);

        if(type.equals(Type.DIVIDE))
            retActionDTO.addExtraInfo("Divide", arg1.getAsString() + " / " + arg2.getAsString());
        else
            retActionDTO.addExtraInfo("Multiply", arg1.getAsString() + " * " + arg2.getAsString());

        return retActionDTO;
    }


    private boolean isNumber(Object obj) {
        return (obj instanceof Integer) || (obj instanceof Float);
    }


    @Override
    public String getActionTypeString() {
        return "calculation";
    }
}
