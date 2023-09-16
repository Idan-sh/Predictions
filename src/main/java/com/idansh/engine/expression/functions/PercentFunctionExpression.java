package com.idansh.engine.expression.functions;

import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.PropertyType;

public class PercentFunctionExpression extends FunctionActivationExpression {
    Expression whole;
    Expression percentage;

    public PercentFunctionExpression(Expression whole, Expression percentage) {
        super(Type.PERCENT);

        // Can only calculate percentage on numeric values
        if(!whole.getType().isNumeric() || !percentage.getType().isNumeric())
            throw new IllegalArgumentException("Cannot create percent function expression," +
                    "expressions received are not numeric." +
                    "first expression type is \"" + whole.getType() + "\"," +
                    "second expression type is \"" + percentage.getType() + "\".");

        this.whole = whole;
        this.percentage = percentage;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.FLOAT;
    }

    @Override
    public Object getValue() {
        // Calculate and return the percentage
        return (float) whole.getValue() * (float) percentage.getValue() / 100;
    }
}