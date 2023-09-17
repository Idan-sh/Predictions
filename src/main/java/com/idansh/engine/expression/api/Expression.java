package com.idansh.engine.expression.api;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.property.instance.PropertyType;

/**
 * Base interface for - helper functions / entity's property / a free value.
 */
public interface Expression {
    /**
     * @return The value of the expression,
     * using an entity instance's properties' values.
     */
    Object getValue(Entity entityInstance);


    /**
     * @return The value of the expression,
     * using a main or secondary entity instance's properties' values,
     * according to the entity defined in the expression.
     */
    Object getValue(Entity mainEntityInstance, Entity secondaryEntityInstance);

    /**
     * @return The type of the expression.
     */
    PropertyType getType();
}
