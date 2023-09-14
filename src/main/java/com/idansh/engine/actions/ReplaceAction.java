package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.world.World;

/**
 * Replaces a single entity in the population.
 */
public class ReplaceAction extends Action {
    private enum Mode {
        SCRATCH, DERIVED;

        private static Mode getMode(String mode) {
            switch (mode) {
                case "scratch":
                    return SCRATCH;

                case "derived":
                    return DERIVED;

                default:
                    throw new IllegalArgumentException("Cannot create Replace Action, invalid mode received \"" + mode + "\".");
            }
        }

        private static String getModeString(Mode mode) {
            switch (mode) {
                case SCRATCH:
                    return "scratch";

                case DERIVED:
                    return "derived";

                default:
                    throw new IllegalArgumentException("Unhandled mode received \"" + mode + "\" in Replace Action, cannot convert it to string.");
            }
        }
    }
    private final String entityToCreate;
    private final Mode mode;


    /**
     * @param worldContext reference to the simulated world in which the action is preformed.
     * @param entityToKill name entity on which the action will be preformed.
     * @param entityToCreate name entity on which the action will be preformed.
     * @param mode Mode of the 'Replace' action.
     *             'scratch' to create a fresh entity with new values.
     *             'derived' to create an entity with some values of the old entity which was killed.
     */
    public ReplaceAction(World worldContext, String entityToKill, String entityToCreate, String mode) {
        super(worldContext, entityToKill);

        checkEntityContext(entityToCreate); // Check if an entity with the received name entityToCreate exists.

        this.entityToCreate = entityToCreate;
        this.mode = Mode.getMode(mode);
    }


    @Override
    public void invoke(Entity entity) {
        World worldContext = getWorldContext();

        switch (mode) {
            case SCRATCH:
                // Kill EntityToKill from the population
                if(entity != null)
                    worldContext.entityManager.killEntity(entity);

                // Create and add to the population a new EntityToCreate
                worldContext.entityManager.createEntity(entityToCreate);
                break;

            case DERIVED:
                // todo - complete derived (for the time being same as scratch
                // Kill EntityToKill from the population
                if(entity != null)
                    worldContext.entityManager.killEntity(entity);

                // Create and add to the population a new EntityToCreate
                worldContext.entityManager.createEntity(entityToCreate);
                break;

            default:
                throw new IllegalArgumentException("Unhandled mode received \"" + mode + "\", cannot invoke the replace action.");
        }
    }


    @Override
    public Action copy(World worldContext) {
        return new ReplaceAction(worldContext, getEntityContext(), entityToCreate, Mode.getModeString(mode));
    }

    @Override
    public String getActionTypeString() {
        return "kill";
    }
}
