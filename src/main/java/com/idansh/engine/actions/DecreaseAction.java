package com.idansh.engine.actions;

import com.idansh.dto.action.ActionDTO;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.world.World;

/**
 * Decreases the value of a numeric property of an entity.

 */
public class DecreaseAction extends Action {
    String propertyName;
    Expression amount;

    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param mainEntityContext name entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     * @param amount the amount to be added to the property's value.
     */
    public DecreaseAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityName, String propertyName, Expression amount) {
        super(worldContext, mainEntityContext, secondaryEntity, entityName);
        this.propertyName = propertyName;
        this.amount = amount;
    }

    public DecreaseAction(World worldContext, String mainEntityContext, String entityName, String propertyName, Expression amount) {
        super(worldContext, mainEntityContext, entityName);
        this.propertyName = propertyName;
        this.amount = amount;
    }

    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        PropertyFactory propertyFactory = getWorldContext().entityManager.getEntityFactory(getEntityToInvokeOnName()).getPropertyFactory(propertyName);
        Property property;

        if (!propertyFactory.isNumericProperty())
            throw new IllegalArgumentException("Can preform decrease only on numeric properties!\n" +
                    "the property if of type \"" + propertyFactory.getType() + "\".");

        Entity entityToInvokeOn = getEntityToInvokeOn(mainEntity, secondaryEntity);
        property = entityToInvokeOn.getPropertyByName(propertyName);

        if(property != null)
            property.addNumToValue(invertValue(amount.getValue(mainEntity, secondaryEntity)));
        else throw new IllegalArgumentException("Cannot perform decrease action on " + getEntityToInvokeOnName() +
                " invalid property name defined.");
    }

    @Override
    public void invoke(Entity entity) {
        invoke(entity, null);
    }


    @Override
    public Action copy(World worldContext) {
        return new DecreaseAction(worldContext, getMainEntityContext(), getEntityToInvokeOnName(), propertyName, amount);
    }


    @Override
    public ActionDTO getActionDTO() {
        ActionDTO retActionDTO = getBasicActionDTO();

        retActionDTO.addArgument("Amount", amount.getAsString());
        retActionDTO.addArgument("Entity Name", getEntityToInvokeOnName());
        retActionDTO.addArgument("Property Name", propertyName);

        return retActionDTO;
    }


    /**
     * Inverts a number, from positive to negative or from negative to positive.
     * @param valueToInvert number of the type Integer or Float (non-primitive).
     * @return inverted value of the number received.
     */
    private Object invertValue(Object valueToInvert) {
        if(valueToInvert instanceof Integer)
            return ((int) valueToInvert) * (-1);
        if(valueToInvert instanceof Float)
            return ((float) valueToInvert) * (-1);

        throw new IllegalArgumentException("invertValue can be preformed only on Integer or Float values! got value to invert of type \"" + valueToInvert.getClass() + "\".");
    }

    @Override
    public String getActionTypeString() {
        return "decrease";
    }
}
