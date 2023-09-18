package com.idansh.engine.expression.functions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;

public class TicksFunctionActivation extends FunctionActivationExpression {
    private final String entityName;
    private final String propertyName;

    public TicksFunctionActivation(String entityName, String propertyName) {
        super(Type.TICKS);
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.INTEGER;
    }

    @Override
    public Object getValue(Entity entityInstance) {
        if(!entityInstance.getName().equals(entityName) || entityInstance.getPropertyByName(propertyName) != null)
            throw new IllegalArgumentException("Cannot get entity instance's value in the ticks expression, " +
                    "entity instance received does not have the defined entity name \"" + entityName + " or property name \"" + propertyName + "\".");

        return 0; // todo - add ticks getValue()
    }


    @Override
    public Object getValue(Entity mainEntityInstance, Entity secondaryEntityInstance) {
        Property property;
        if (mainEntityInstance.getName().equals(entityName) && (property = mainEntityInstance.getPropertyByName(propertyName)) != null) {
            return 0; // todo - add ticks getValue()
        } else if (secondaryEntityInstance.getName().equals(entityName) && (property = secondaryEntityInstance.getPropertyByName(propertyName)) != null) {
            return 0;
        } else {
            throw new IllegalArgumentException("Cannot get entity instance's value in the ticks function expression, " +
                    "main and secondary entity instances received does not have the defined entity name \"" + entityName + " or property name \"" + propertyName + "\".");
        }
    }


    @Override
    public String getAsString() {
        return "Ticks(" + entityName + ", " + propertyName + ")";
    }
}
