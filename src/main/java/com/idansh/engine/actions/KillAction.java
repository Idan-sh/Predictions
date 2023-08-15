package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.world.World;

/**
 * Kills a single entity from the population.
 */
public class KillAction extends Action {
    public KillAction(World worldContext, String entityContext) {
        super(worldContext, entityContext);
    }

    public void invoke() {
        World worldContext = getWorldContext();
        Entity entityToKill = worldContext.entityManager.getEntityInPopulation(getEntityContext());
        worldContext.entityManager.killEntity(entityToKill);
    }

}
