package com.idansh.engine.actions.condition;

import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
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


    public ProximityConditionAction(World worldContext, String sourceEntity, String targetEntity, ThenOrElseActions thenActions, Expression proximityDepth) {
        super(worldContext, sourceEntity, thenActions, null, true);

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
    public void invoke(Entity entity) {
        // todo - complete proximity invoke
    }


    @Override
    public Action copy(World worldContext) {
        ThenOrElseActions thenActions = new ThenOrElseActions(getThenActions(), worldContext);

        return new ProximityConditionAction(
                worldContext,
                getEntityContext(),
                targetEntity,
                thenActions,
                proximityDepth
        );
    }
}
