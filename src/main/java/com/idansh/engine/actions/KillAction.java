package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.world.World;

/**
 * Kills a single entity from the population.
 */
public class KillAction extends Action {
    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param mainEntityContext Name of the main entity on which the action will be preformed.
     * @param secondaryEntity secondaryEntity Name of the secondary entity on which the action will be preformed.
     */
    public KillAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityToKill) {
        super(worldContext, mainEntityContext, secondaryEntity, entityToKill);
    }

    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param mainEntityContext Name of the main entity of which an instance will be killed from the population.
     */
    public KillAction(World worldContext, String mainEntityContext, String entityToKill) {
        super(worldContext, mainEntityContext, entityToKill);
    }


    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        if (mainEntity != null && getEntityToInvokeOn().equals(mainEntity.getName()))
            mainEntity.kill();
        else if (secondaryEntity != null && getEntityToInvokeOn().equals(secondaryEntity.getName()))
            secondaryEntity.kill();
    }

    @Override
    public void invoke(Entity entity) {
        World worldContext = getWorldContext();

        if(entity != null && getEntityToInvokeOn().equals(entity.getName()))
            worldContext.entityManager.killEntity(entity);
    }

    @Override
    public Action copy(World worldContext) {
        return new KillAction(worldContext, getMainEntityContext(), getSecondaryEntity(), getEntityToInvokeOn());
    }

    @Override
    public String getActionTypeString() {
        return "kill";
    }
}
