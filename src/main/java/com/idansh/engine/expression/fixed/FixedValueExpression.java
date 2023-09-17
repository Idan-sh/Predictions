package com.idansh.engine.expression.fixed;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.PropertyType;

/**
 * An expression with a fixed value.
 * Can be numeric/boolean/string, depends on its context
 */
public class FixedValueExpression implements Expression {
    private final Object value;
    private final PropertyType type;

    public FixedValueExpression(Object value, PropertyType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public Object getValue(Entity ignored) {
        return value;
    }

    @Override
    public Object getValue(Entity ignored1, Entity ignored2) {
        return value;
    }

    @Override
    public PropertyType getType() {
        return type;
    }
}
