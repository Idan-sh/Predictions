package com.idansh.engine.actions.condition;

import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for a collection of actions, to be invoked when a Then/Else block
 * is activated.
 */
public class ThenOrElseActions {
    private final List<Action> actionsToInvoke;


    public ThenOrElseActions() {
        actionsToInvoke = new ArrayList<>();
    }


    /**
     * Creates a new TheOrElseActions instance with the given worldContext,
     * updates each action to the new world context given.
     * @param thenOrElseActions then or else actions to copy.
     * @param worldContext the world in which the new ThenOrElseActions is defined.
     */
    public ThenOrElseActions(ThenOrElseActions thenOrElseActions, World worldContext) {
        actionsToInvoke = new ArrayList<>();

        for(Action action : thenOrElseActions.getActionsToInvoke()) {
            addAction(action.copy(worldContext));
        }
    }


    public void addAction(Action action) {
        actionsToInvoke.add(action);
    }


    public boolean isEmpty() {
        return actionsToInvoke.isEmpty();
    }


    /**
     * Invokes each action in the Then/Else set of actions.
     * @param entity the entity instance on which the actions will be invoked
     */
    public void invoke(Entity entity){
        actionsToInvoke.forEach(
                action -> action.invoke(entity)
        );
    }


    public List<Action> getActionsToInvoke() {
        return actionsToInvoke;
    }
}
