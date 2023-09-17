package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.world.World;

/**
 * Increases the value of a numeric property of an entity.
 */
public class IncreaseAction extends Action {
    String propertyName;
    Expression amount;

    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param mainEntityContext name entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     * @param amount the amount to be added to the property's value.
     */
    public IncreaseAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityName, String propertyName, Expression amount) {
        super(worldContext, mainEntityContext, secondaryEntity, entityName);
        this.propertyName = propertyName;
        this.amount = amount;
    }

    public IncreaseAction(World worldContext, String mainEntityContext, String entityName, String propertyName, Expression amount) {
        super(worldContext, mainEntityContext, entityName);
        this.propertyName = propertyName;
        this.amount = amount;
    }

    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        PropertyFactory propertyFactory = getWorldContext().entityManager.getEntityFactory(getEntityToInvokeOnName()).getPropertyFactory(propertyName);
        Property property;

        if (!propertyFactory.isNumericProperty())
            throw new IllegalArgumentException("Can preform increase only on numeric properties!\n" +
                    "the property if of type \"" + propertyFactory.getType() + "\".");

        Entity entityToInvokeOn = getEntityToInvokeOn(mainEntity, secondaryEntity);
        property = entityToInvokeOn.getPropertyByName(propertyName);

        if(property != null)
            property.addNumToValue(amount.getValue(mainEntity, secondaryEntity));
        else throw new IllegalArgumentException("Cannot perform increase action on " + getEntityToInvokeOnName() +
                " invalid property name defined.");
    }

    @Override
    public void invoke(Entity entity) {
        invoke(entity, null);
    }


    @Override
    public Action copy(World worldContext) {
        return new IncreaseAction(
                worldContext,
                getMainEntityContext(),
                getSecondaryEntity(),
                getEntityToInvokeOnName(),
                propertyName,
                amount
        );
    }


    @Override
    public String getActionTypeString() {
        return "increase";
    }
}
