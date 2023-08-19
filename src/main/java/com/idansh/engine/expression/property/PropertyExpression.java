package com.idansh.engine.expression.property;

import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.world.World;

/**
 * An expression that is a property name of the main entity in context.
 */
public class PropertyExpression implements Expression {
    private final World worldContext;
    private final String entityName;
    private final String propertyName;

    public PropertyExpression(World worldContext, String entityName, String propertyName) {
        this.worldContext = worldContext;
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    @Override
    public Object getValue() {
        return worldContext.entityManager.getEntityFactory(entityName).getPropertyFactory(propertyName).getValue();
    }

    @Override
    public PropertyType getType() {
        return worldContext.entityManager.getEntityFactory(entityName).getPropertyFactory(propertyName).getType();
    }
}
