package com.idansh.engine.actions;

import com.idansh.dto.action.ActionDTO;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.world.World;

public abstract class Action {
    public enum Type{
        CALCULATION, CONDITION, DECREASE, INCREASE, SET, KILL, REPLACE, PROXIMITY;

        /**
         * Converts a string of an action to its corresponding Action Type.
         * @param s the action type in string format.
         * @return Action.Type that defines the string received.
         * @throws IllegalArgumentException if the string received is in invalid format (not one of the specified strings).
         */
        public static Type getType(String s) {
            switch(s) {
                case "increase":
                    return Type.INCREASE;

                case "decrease":
                    return Type.DECREASE;

                case "calculation":
                    return Type.CALCULATION;

                case "condition":
                    return Type.CONDITION;

                case "set":
                    return Type.SET;

                case "kill":
                    return Type.KILL;

                case "replace":
                    return Type.REPLACE;

                case "proximity":
                    return Type.PROXIMITY;

                default:
                    throw new IllegalArgumentException("cannot convert received string to action type! received string is \"" + s + "\".");
            }
        }
    }
    private final String mainEntityContext;
    private final World worldContext;
    private final SecondaryEntity secondaryEntity; // Optional
    private final String entityToInvokeOnName;


    public Action(World worldContext, String mainEntityContext, String entityToInvokeOnName) {
        this.mainEntityContext = mainEntityContext;
        this.worldContext = worldContext;
        this.secondaryEntity = null;

        checkEntityToInvokeOn(entityToInvokeOnName);
        this.entityToInvokeOnName = entityToInvokeOnName;

        checkEntityContext(mainEntityContext);
    }

    public Action(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityToInvokeOnName) {
        this.mainEntityContext = mainEntityContext;
        this.worldContext = worldContext;
        this.secondaryEntity = secondaryEntity;

        checkEntityToInvokeOn(entityToInvokeOnName);
        this.entityToInvokeOnName = entityToInvokeOnName;

        checkEntityContext(mainEntityContext);
    }


    /**
     * Check that the entity to invoke an action on is valid.
     */
    private void checkEntityToInvokeOn(String entityToInvokeOn) {
        if(entityToInvokeOn == null)
            throw new IllegalArgumentException("Invalid action received in the XML file, " +
                    "received no entity to invoke on...");

        if (!entityToInvokeOn.equals(mainEntityContext)) {
            if (secondaryEntity != null && !entityToInvokeOn.equals(secondaryEntity.getName()))
                throw new IllegalArgumentException("Invalid action received in the XML file, " +
                        "cannot create action on entity \"" + entityToInvokeOn + "\" which is not the main or secondary entity!" +
                        "\nMain entity: " + mainEntityContext + "\nSecondary entity: " + secondaryEntity.getName());
        }
    }


    /**
     * Checks on which entity the action should be performed,
     * returns that entity.
     * Decides it by the entityToInvokeOnName's value.
     */
    public Entity getEntityToInvokeOn(Entity mainEntity, Entity secondaryEntity) {
        if (secondaryEntity == null) return mainEntity;

        // Check if the action can be invoked on each entity instance received
        if (entityToInvokeOnName.equals(mainEntity.getName())) {
            return mainEntity;
        } else if (entityToInvokeOnName.equals(secondaryEntity.getName())) {
            return secondaryEntity;
        } else {
            throw new IllegalArgumentException("Cannot perform action on " + entityToInvokeOnName +
                    " Received invalid entity instances with names \"" + mainEntity.getName() +
                    "\", \"" + secondaryEntity.getName() + "\".");
        }
    }

    /**
     * Check if there is an entity factory with the name of the value of the received entityContext.
     * @throws IllegalArgumentException in case the entity context received is invalid.
     */
    public void checkEntityContext(String entityContext) {
        if(!worldContext.entityManager.isEntityFactoryValid(entityContext))
            throw new IllegalArgumentException("Invalid entity context \"" + entityContext +
                    "\", no entity defined with this name...");
    }


    /**
     * Invokes the action, according to the action's type.
     * @param entity the entity instance on which the action will be performed.
     */
    public abstract void invoke(Entity entity);


    /**
     * Invokes the action, according to the action's type.
     * @param mainEntity The main entity instance on which the action will be performed.
     * @param secondaryEntity The secondary entity instance on which the action will be performed.
     */
    public abstract void invoke(Entity mainEntity, Entity secondaryEntity);


    /**
     * @return The type of action.
     */
    public abstract String getActionTypeString();


    /**
     * Deep copies this Action instance, using a new worldContext for the action's activation.
     */
    public abstract Action copy(World worldContext);


    /**
     * @return DTO containing info on the action.
     */
    public abstract ActionDTO getActionDTO();


    /**
     * @return a basic Action DTO without params or extra information.
     */
    public ActionDTO getBasicActionDTO() {
        return new ActionDTO(
                getActionTypeString(),
                getMainEntityContext(),
                secondaryEntity != null ? secondaryEntity.getName() : null
        );
    }

    public String getMainEntityContext() {
        return mainEntityContext;
    }

    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }

    public World getWorldContext() {
        return worldContext;
    }

    public String getEntityToInvokeOnName() {
        return entityToInvokeOnName;
    }
}
