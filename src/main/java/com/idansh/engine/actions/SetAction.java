package com.idansh.engine.actions;

import com.idansh.dto.action.ActionDTO;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.world.World;

/**
 * Sets the value of a property (of any type) of an entity.
 */
public class SetAction extends Action {
    private final String propertyName;
    private final Expression amount;


    /**
     * @param worldContext Reference to the simulated world in which the action is preformed.
     * @param mainEntityContext Name entity on which the action will be preformed.
     * @param entityName Name of the entity whose property value will be changed.
     * @param propertyName Name of the property whose value will be changed.
     * @param amount Amount to be added to the property's value.
     */
    public SetAction(World worldContext, String mainEntityContext, String entityName, String propertyName, Expression amount) {
        super(worldContext, mainEntityContext, entityName);
        this.propertyName = propertyName;
        this.amount = amount;
    }


    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param mainEntityContext name entity on which the action will be preformed.
     * @param entityName Name of the entity whose property value will be changed.
     * @param propertyName name of the property whose value will be changed.
     * @param amount the amount to be added to the property's value.
     */
    public SetAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityName, String propertyName, Expression amount) {
        super(worldContext, mainEntityContext, secondaryEntity, entityName);
        this.propertyName = propertyName;
        this.amount = amount;
    }


    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        Entity entityToInvokeOn = getEntityToInvokeOn(mainEntity, secondaryEntity);
        Property property = entityToInvokeOn.getPropertyByName(propertyName);
        property.setValue(amount.getValue(mainEntity, secondaryEntity));
    }


    @Override
    public void invoke(Entity entity) {
        invoke(entity, null);
    }


    @Override
    public Action copy(World worldContext) {
        return new SetAction(
                worldContext,
                getMainEntityContext(),
                getSecondaryEntity(),
                getEntityToInvokeOnName(),
                propertyName,
                amount
        );
    }


    @Override
    public ActionDTO getActionDTO() {
        ActionDTO retActionDTO = getBasicActionDTO();

        retActionDTO.addArgument("Entity Name", getEntityToInvokeOnName());
        retActionDTO.addArgument("Property Name", propertyName);
        retActionDTO.addArgument("Value", amount.getAsString());

        return retActionDTO;
    }


    @Override
    public String getActionTypeString() {
        return "set";
    }
}
