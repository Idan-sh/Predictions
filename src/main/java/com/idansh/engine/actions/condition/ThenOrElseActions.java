package com.idansh.engine.actions.condition;

import com.idansh.engine.actions.Action;

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

    public void addAction(Action action) {
        actionsToInvoke.add(action);
    }

    public boolean isEmpty() {
        return actionsToInvoke.isEmpty();
    }

    /**
     * Invokes each action in the Then/Else set of actions.
     */
    public void invoke(){
        actionsToInvoke.forEach(Action::invoke);
    }
}
