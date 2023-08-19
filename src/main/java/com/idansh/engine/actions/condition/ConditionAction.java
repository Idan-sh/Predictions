package com.idansh.engine.actions.condition;

import com.idansh.engine.actions.Action;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;


public abstract class ConditionAction extends Action {
    public enum Type {
        SINGLE, MULTI
    }

    private final Type type;
    private final ThenOrElseActions thenActions;
    private final ThenOrElseActions elseActions;


    public ConditionAction(World worldContext, String entityContext, Type type, ThenOrElseActions thenActions, ThenOrElseActions elseActions) {
        super(worldContext, entityContext);
        this.type = type;
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }

    public void invoke(boolean isCondition) {
        if(isCondition)
            thenActions.invoke();
        else
            if(!elseActions.isEmpty())
                elseActions.invoke();
    }
}