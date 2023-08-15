package com.idansh.engine.expression.handler;

import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.world.World;

/**
 * Given an expression, digests it and returns the proper value.
 */
public class ExpressionDigest {
    private final ActiveEnvironmentVariables environmentVariables;
    private final EntityManager entityManager;

    public ExpressionDigest(World world) {
        this.environmentVariables = world.getActiveEnvironmentVariables();
        this.entityManager = world.entityManager;
    }

    public void analyzeExpression() {


    }
}
