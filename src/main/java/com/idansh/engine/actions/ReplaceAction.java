package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
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
     * @param worldContext Reference to the simulated world in which the action is preformed.
     * @param entityToKill Name of the entity on which the action will be preformed.
     * @param entityToCreate Name of the entity on which the action will be preformed.
     * @param mode Mode of the 'Replace' action.
     *             'scratch' to create a fresh entity with new values.
     *             'derived' to create an entity with some values of the old entity which was killed.
     */
    public ReplaceAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityToKill, String entityToCreate, String mode) {
        super(worldContext, mainEntityContext, secondaryEntity, entityToKill);

        checkEntityContext(entityToCreate); // Check if an entity with the received name entityToCreate exists.

        this.entityToCreate = entityToCreate;
        this.mode = Mode.getMode(mode);
    }


    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        World worldContext = getWorldContext();
        Entity entityToInvokeOn = getEntityToInvokeOn(mainEntity, secondaryEntity);

        switch (mode) {
            case SCRATCH:
                // Kill EntityToKill from the population
                worldContext.entityManager.replaceEntity(entityToInvokeOn, entityToCreate, true);
                break;

            case DERIVED:
                worldContext.entityManager.replaceEntity(entityToInvokeOn, entityToCreate, false);
                break;

            default:
                throw new IllegalArgumentException("Unhandled mode received \"" + mode + "\", cannot invoke the replace action.");
        }
    }


    @Override
    public void invoke(Entity entity) {
        invoke(entity, null);
    }


    @Override
    public Action copy(World worldContext) {
        return new ReplaceAction(
                worldContext,
                getMainEntityContext(),
                getSecondaryEntity(),
                getEntityToInvokeOnName(),
                entityToCreate,
                Mode.getModeString(mode)
        );
    }

    @Override
    public String getActionTypeString() {
        return "replace";
    }
}
