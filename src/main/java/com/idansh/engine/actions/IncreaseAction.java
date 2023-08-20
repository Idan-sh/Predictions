package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.world.World;

/**
 * Increases the value of a numeric property of an entity.
 */
public class IncreaseAction extends Action {
    String propertyName;
    Expression amount;

    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param entityContext name entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     * @param amount the amount to be added to the property's value.
     */
    public IncreaseAction(World worldContext, String entityContext, String propertyName, Expression amount) {
        super(worldContext, entityContext);
        this.propertyName = propertyName;
        this.amount = amount;
    }

    @Override
    public void invoke(Entity entity) {
        Property property = entity.getPropertyByName(propertyName);

        if (!property.isNumericProperty())
            throw new IllegalArgumentException("can preform increase only on numeric properties! the property if of type \"" + property.getType() + "\".");

        property.addNumToValue(amount.getValue());
    }


    @Override
    public String getActionTypeString() {
        return "increase";
    }
}
