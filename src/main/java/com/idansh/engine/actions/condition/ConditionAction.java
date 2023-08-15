package com.idansh.engine.actions.condition;

import com.idansh.engine.actions.Action;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;


public abstract class ConditionAction extends Action {
    public enum Type {
        SINGLE, MULTI
    }

    private final Type type;

    public ConditionAction(World worldContext, String entityContext, Type type) {
        super(worldContext, entityContext);
        this.type = type;
    }

    public void invoke() {
        // todo- complete condition action
        // todo- maybe check if the new value is within a range for numeric properties?
    }
}