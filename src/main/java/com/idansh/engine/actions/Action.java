package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.expression.Expression;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.instance.PropertyType;

// todo: divide into multiple classes for each action...
public class Action implements EntityActions {
    Entity entity;    // On which the action will be preformed.

    public Action(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void increase(String propertyName, Expression amount) {
        PropertyFactory propertyFactory = entity.getPropertyFactory(propertyName);

        if(!isNumericPropertyFactory(propertyFactory))
            throw new IllegalArgumentException("Error: can preform increase only on numeric property factories!");

        // todo- preform increase on a property instance of an entity instance (not on the entity factory i guess)
    }

    @Override
    public void decrease(String propertyName, Expression amount) {

    }

    @Override
    public void calculation(String propertyName, Expression arg1, Expression arg2) {

    }

    @Override
    public void condition(String propertyName, String operator, Expression value) {

    }

    @Override
    public void condition(String logicOp) {

    }

    @Override
    public void set(String propertyName, Expression amount) {

    }

    @Override
    public void kill() {

    }


    /**
     * Checks if a given property factory is of the type FLOAT or INTEGER.
     * @return true if the property factory given is numeric, false otherwise.
     */
    private boolean isNumericPropertyFactory(PropertyFactory propertyFactory) {
        return PropertyType.INTEGER.equals(propertyFactory.getType()) || PropertyType.FLOAT.equals(propertyFactory.getType());
    }
}
