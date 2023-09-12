package com.idansh.engine.expression.functions;

import com.idansh.engine.property.instance.PropertyType;

public class TicksFunctionActivation extends FunctionActivationExpression {
    private String entityName;
    private String propertyName;

    public TicksFunctionActivation(String entityName, String propertyName) {
        super(Type.TICKS);
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    @Override
    public PropertyType getType() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }
}
