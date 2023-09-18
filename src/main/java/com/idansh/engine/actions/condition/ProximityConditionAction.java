package com.idansh.engine.actions.condition;

import com.idansh.dto.action.ActionDTO;
import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.world.World;

/**
 * Defines a condition that on invoke checks the distance between two entities.
 * If the distance is within the defined range, the condition will invoke all actions defined within it.
 */
public class ProximityConditionAction extends ConditionAction {
    private final String targetEntity;          // Will be searched in the population within the proximity of the source entity
    private final Expression proximityDepth;    // The distance (location radius) of which the target entity will be searched from the source entity


    public ProximityConditionAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String sourceEntity, String targetEntity, ThenOrElseActions thenActions, Expression proximityDepth) {
        super(worldContext, mainEntityContext, sourceEntity, thenActions, null, true);

        if(!proximityDepth.getType().equals(PropertyType.INTEGER) && !proximityDepth.getType().equals(PropertyType.FLOAT))
            throw new IllegalArgumentException("Cannot create proximity action with an non-numeric expression, got expression of type \"" + proximityDepth.getType() + "\".");

        this.targetEntity = targetEntity;
        this.proximityDepth = proximityDepth;
    }

    public ProximityConditionAction(World worldContext, String mainEntityContext, String sourceEntity, String targetEntity, ThenOrElseActions thenActions, Expression proximityDepth) {
        super(worldContext, mainEntityContext, sourceEntity, thenActions, null, true);

        if(!proximityDepth.getType().equals(PropertyType.INTEGER) && !proximityDepth.getType().equals(PropertyType.FLOAT))
            throw new IllegalArgumentException("Cannot create proximity action with an non-numeric expression, got expression of type \"" + proximityDepth.getType() + "\".");

        this.targetEntity = targetEntity;
        this.proximityDepth = proximityDepth;
    }


    @Override
    public String getActionTypeString() {
        return "proximity";
    }


    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        invoke(mainEntity);
    }

    @Override
    public void invoke(Entity entity) {
        // todo - complete proximity invoke
    }

    @Override
    public ActionDTO getActionDTO() {
        ActionDTO retActionDTO = getBasicActionDTO();

        retActionDTO.addArgument("Source Entity", getEntityToInvokeOnName());
        retActionDTO.addArgument("Target Entity", targetEntity);
        retActionDTO.addArgument("Depth", proximityDepth.getAsString());

        retActionDTO.addExtraInfo("Amount of Actions", Integer.valueOf(getThenActions().getActionsToInvoke().size()).toString());

        return retActionDTO;
    }

    @Override
    public Action copy(World worldContext) {
        ThenOrElseActions thenActions = new ThenOrElseActions(getThenActions(), worldContext);

        return new ProximityConditionAction(
                worldContext,
                getMainEntityContext(),
                getSecondaryEntity(),
                getEntityToInvokeOnName(),
                targetEntity,
                thenActions,
                proximityDepth
        );
    }
}
