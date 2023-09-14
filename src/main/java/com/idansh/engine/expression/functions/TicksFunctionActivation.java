package com.idansh.engine.expression.functions;

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
    public Object getValue() {
        return 0; // todo - add ticks getValue()
    }
}
