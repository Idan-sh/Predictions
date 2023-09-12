package com.idansh.engine.expression.functions;

import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.property.instance.PropertyType;

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
    public Object getValue() {
        return entityManager.getEntityFactory(entityName).getPropertyFactory(propertyName).getValue();
    }
}
