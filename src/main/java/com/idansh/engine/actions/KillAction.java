package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.environment.ActiveEnvironmentVariables;

/**
 * Kills a single entity from the population.
 */
public class KillAction extends Action {


    public KillAction(Entity entity, ActiveEnvironmentVariables activeEnvironmentVariables) {
        super(entity, activeEnvironmentVariables);
    }

    public void invoke() {
        // todo- complete kill action
    }

}
