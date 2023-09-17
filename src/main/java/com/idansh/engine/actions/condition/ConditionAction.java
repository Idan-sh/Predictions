package com.idansh.engine.actions.condition;

import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.world.World;


public abstract class ConditionAction extends Action {
    private final ThenOrElseActions thenActions;
    private final ThenOrElseActions elseActions;
    private final boolean isMainCondition;      // Whether this condition action has "then" and "else" actions blocks, which are set only on the outermost condition action.
    private boolean isActivated;


    public ConditionAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityName, ThenOrElseActions thenActions, ThenOrElseActions elseActions, boolean isMainCondition) {
        super(worldContext, mainEntityContext, secondaryEntity, entityName);
        this.thenActions = thenActions;
        this.elseActions = elseActions;
        this.isMainCondition = isMainCondition;
        this.isActivated = false;
    }

    public ConditionAction(World worldContext, String mainEntityContext, String entityName, ThenOrElseActions thenActions, ThenOrElseActions elseActions, boolean isMainCondition) {
        super(worldContext, mainEntityContext, entityName);
        this.thenActions = thenActions;
        this.elseActions = elseActions;
        this.isMainCondition = isMainCondition;
        this.isActivated = false;
    }


    /**
     * Invokes "then" actions set if condition is true,
     * or invokes "else" actions set if condition is false.
     * @param isCondition the value of the condition defined in the action.
     */
    public void invokeActionsSet(Entity mainEntity, Entity secondaryEntity, boolean isCondition) {
        if(thenActions != null && isCondition)
            thenActions.invoke(mainEntity, secondaryEntity);
        else
            if(elseActions != null && !elseActions.isEmpty())
                elseActions.invoke(mainEntity, secondaryEntity);
    }


    /**
     * Invokes "then" actions set if condition is true,
     * or invokes "else" actions set if condition is false.
     * @param isCondition the value of the condition defined in the action.
     */
    public void invokeActionsSet(Entity mainEntity, boolean isCondition) {
        if(thenActions != null && isCondition) {
            thenActions.invoke(mainEntity);
        }
        else if(elseActions != null && !elseActions.isEmpty()) {
            elseActions.invoke(mainEntity);
        }
    }


    public boolean isMainCondition() {
        return isMainCondition;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public ThenOrElseActions getThenActions() {
        return thenActions;
    }

    public ThenOrElseActions getElseActions() {
        return elseActions;
    }
}