package com.idansh.engine.actions;

import com.idansh.engine.expression.Expression;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.world.World;

/**
 * Decreases the value of a numeric property of an entity.

 */
public class DecreaseAction extends Action {
    String propertyName;
    Expression amount;

    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param entityContext name entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     * @param amount the amount to be added to the property's value.
     */
    public DecreaseAction(World worldContext, String entityContext, String propertyName, Expression amount) {
        super(worldContext, entityContext);
        this.propertyName = propertyName;
        this.amount = amount;
    }

    public void invoke() {
        Property property = super.getWorldContext().entityManager.getEntityInPopulation(getEntityContext()).getPropertyByName(propertyName);

        if(!super.isNumericProperty(property))
            throw new IllegalArgumentException("Error: can preform increase only on numeric property factories!");

        // todo- preform decrease on the property
        // todo- make sure after the decrease the value is within the property range
    }
}
