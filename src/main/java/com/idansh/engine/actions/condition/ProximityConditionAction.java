package com.idansh.engine.actions.condition;

import com.idansh.dto.action.ActionDTO;
import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.world.World;

import java.awt.*;

/**
 * Defines a condition that on invoke checks the distance between two entities.
 * If the distance is within the defined range, the condition will invoke all actions defined within it on the main entity defined.
 */
public class ProximityConditionAction extends ConditionAction {
    private final String targetEntityName;          // Will be searched in the population within the proximity of the source entity
    private final Expression proximityDepth;    // The distance (location radius) of which the target entity will be searched from the source entity


    public ProximityConditionAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String sourceEntity, String targetEntityName, ThenOrElseActions thenActions, Expression proximityDepth) {
        super(worldContext, mainEntityContext, secondaryEntity, sourceEntity, thenActions, null, true);

        if(!proximityDepth.getType().equals(PropertyType.INTEGER) && !proximityDepth.getType().equals(PropertyType.FLOAT))
            throw new IllegalArgumentException("Cannot create proximity action with an non-numeric expression, got expression of type \"" + proximityDepth.getType() + "\".");

        this.targetEntityName = targetEntityName;
        this.proximityDepth = proximityDepth;
    }

    public ProximityConditionAction(World worldContext, String mainEntityContext, String sourceEntity, String targetEntityName, ThenOrElseActions thenActions, Expression proximityDepth) {
        super(worldContext, mainEntityContext, sourceEntity, thenActions, null, true);

        if(!proximityDepth.getType().equals(PropertyType.INTEGER) && !proximityDepth.getType().equals(PropertyType.FLOAT))
            throw new IllegalArgumentException("Cannot create proximity action with an non-numeric expression, got expression of type \"" + proximityDepth.getType() + "\".");

        this.targetEntityName = targetEntityName;
        this.proximityDepth = proximityDepth;
    }


    @Override
    public String getActionTypeString() {
        return "proximity";
    }


    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        int depth;
        Object val = proximityDepth.getValue(mainEntity, secondaryEntity);

        if (val instanceof Integer)
            depth = (Integer) proximityDepth.getValue(mainEntity, secondaryEntity);
        else if (val instanceof Float)
            depth = ((Float) proximityDepth.getValue(mainEntity, secondaryEntity)).intValue();
        else
            throw new RuntimeException("Failed to invoke proximity, depth expression is not of a numeric type...");

        Point entityLocation = mainEntity.getGridLocation();

        for (int i = -depth; i <= depth; i++) {
            for (int j = -depth; j <= depth; j++) {
                int x = getWorldContext().entityManager.getAxisLocation(getWorldContext().entityManager.getNofGridRows(), entityLocation.x + i);
                int y = getWorldContext().entityManager.getAxisLocation(getWorldContext().entityManager.getNofGridColumns(), entityLocation.y + j);

                // Check if current entity is if the target entity, if so activate the actions set and quit
                Entity currEntity = getWorldContext().entityManager.getEntityFromGrid(new Point(x, y));
                if (currEntity != null && currEntity.getName().equals(targetEntityName)) {
                    invokeActionsSet(mainEntity, currEntity, true);
                    return;
                }
            }
        }
    }


    @Override
    public void invoke(Entity entity) {
        invoke(entity, null);
    }


    @Override
    public ActionDTO getActionDTO() {
        ActionDTO retActionDTO = getBasicActionDTO();

        retActionDTO.addArgument("Source Entity", getEntityToInvokeOnName());
        retActionDTO.addArgument("Target Entity", targetEntityName);
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
                targetEntityName,
                thenActions,
                proximityDepth
        );
    }
}
