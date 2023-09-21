package com.idansh.engine.expression.functions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;

/**
 * Gets an entity instance's property value.
 */
public class EvaluateFunctionExpression extends FunctionActivationExpression {
    private final String entityName;
    private final String propertyName;
    private final EntityManager entityManager;

    public EvaluateFunctionExpression(EntityManager entityManager, String entityName, String propertyName) {
        super(Type.EVALUATE);
        this.entityManager = entityManager;
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    @Override
    public PropertyType getType() {
        return entityManager.getEntityFactory(entityName).getPropertyFactory(propertyName).getType();
    }

    @Override
    public Object getValue(Entity entityInstance) {
        if(!entityInstance.getName().equals(entityName) || entityInstance.getPropertyByName(propertyName) == null)
            throw new IllegalArgumentException("Cannot get entity instance's value in the evaluate expression, " +
                    "received entity instance's name \"" + entityInstance.getName() + "\" does not have the defined entity name \"" + entityName + " or property name \"" + propertyName + "\".");

        return entityInstance.getPropertyByName(propertyName).getValue();
    }

    @Override
    public Object getValue(Entity mainEntityInstance, Entity secondaryEntityInstance) {
        Property property;
        if (mainEntityInstance.getName().equals(entityName) && (property = mainEntityInstance.getPropertyByName(propertyName)) != null) {
            return property.getValue();
        } else if (secondaryEntityInstance != null && secondaryEntityInstance.getName().equals(entityName) && (property = secondaryEntityInstance.getPropertyByName(propertyName)) != null) {
            return property.getValue();
        } else {
            throw new IllegalArgumentException("Cannot get entity instance's value in the evaluate function expression.\n" +
                    "main entity \"" + mainEntityInstance.getName() + "\" and secondary entity \"" +
                    (secondaryEntityInstance != null ? secondaryEntityInstance.getName() : "N/A") +
                    "\" instances received does not have the defined entity name \"" + entityName +
                    "\" or property name \"" + propertyName + "\".");
        }
    }


    @Override
    public String getAsString() {
        return "Evaluate(" + entityName  + ", " + propertyName + ")";
    }
}
