package com.idansh.engine.actions;

import com.idansh.engine.expression.api.Expression;
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
        super.getWorldContext().entityManager.getPopulation().forEach(
                entity -> {
                    // Preform action only on the entities instances of the entity context
                    if (entity.getName().equals(super.getEntityContext())) {
                        Property property = entity.getPropertyByName(propertyName);

                        if (!property.isNumericProperty())
                            throw new IllegalArgumentException("Error: can preform decrease only on numeric properties!");

                        property.addNumToValue(invertValue(amount.getValue()));
                    }
                }
        );
    }


    /**
     * Inverts a number, from positive to negative or from negative to positive.
     * @param valueToInvert number of the type Integer or Float (non-primitive).
     * @return inverted value of the number received.
     */
    private Object invertValue(Object valueToInvert) {
        if(valueToInvert instanceof Integer)
            return ((int) valueToInvert) * (-1);
        if(valueToInvert instanceof Float)
            return ((float) valueToInvert) * (-1);

        throw new IllegalArgumentException("Error: invertValue can be preformed only on Integer or Float (non-primitive) values!");
    }

    @Override
    public String getActionTypeString() {
        return "decrease";
    }
}
