package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.expression.Expression;
import com.idansh.engine.property.instance.Property;

/**
 * Decreases the value of a numeric property of the entity.

 */
public class DecreaseAction extends Action {
    String propertyName;
    Expression amount;

    /**
     * @param entity the entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     * @param amount the amount to be added to the property's value.
     */
    public DecreaseAction(Entity entity, String propertyName, Expression amount) {
        super(entity);
        this.propertyName = propertyName;
        this.amount = amount;
    }

    public void invoke() {
        Property propertyFactory = super.getContextEntity().getPropertyByName(propertyName);

        if(!super.isNumericProperty(propertyFactory))
            throw new IllegalArgumentException("Error: can preform increase only on numeric property factories!");

        // todo- preform decrease on a property instance of an entity instance (not on the entity factory i guess)
    }
}
