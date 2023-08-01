package com.idansh.engine.handler;

import com.idansh.engine.expression.Expression;

public class EngineFunctions implements EngineHandler{
    @Override
    public void increase(String entityName, String propertyName, Expression amount) {

    }

    @Override
    public void decrease(String entityName, String propertyName, Expression amount) {

    }

    @Override
    public void calculation(String entityName, String propertyName, Expression arg1, Expression arg2) {

    }

    @Override
    public void condition(String entityName, String propertyName, String operator, Expression value) {

    }

    @Override
    public void condition(String logicOp) {

    }

    @Override
    public void set(String entityName, String propertyName, Expression amount) {

    }

    @Override
    public void kill(String entityName) {

    }
}
