package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.world.World;

/**
 * Kills a single entity from the population.
 */
public class KillAction extends Action {
    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param entityContext name entity on which the action will be preformed.
     */
    public KillAction(World worldContext, String entityContext) {
        super(worldContext, entityContext);
    }

    @Override
    public void invoke(Entity entity) {
        World worldContext = getWorldContext();

        if(entity != null)
            worldContext.entityManager.killEntity(entity);
    }

    @Override
    public Action copy(World worldContext) {
        return new KillAction(worldContext, getEntityContext());
    }

    @Override
    public String getActionTypeString() {
        return "kill";
    }
}
