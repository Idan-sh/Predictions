package com.idansh.engine.actions.condition;

import com.idansh.dto.action.ActionDTO;
import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a single logical OR/AND condition that will be invoked on multiple inner conditions.
 * These inner conditions can be also of type MultiConditionAction, or of type SingleConditionAction.
 */
public class MultiConditionAction extends ConditionAction{
    private enum LogicOp {
        OR, AND;

        private static LogicOp getLogicOp(String logicOpStr) {
            switch (logicOpStr) {
                case "or":
                    return OR;

                case "and":
                    return AND;

                default:
                    throw new IllegalArgumentException("invalid logic operand string \"" + logicOpStr + "\" received!");
            }
        }

        private static String getLogicOpString(LogicOp logicOp) {
            switch (logicOp) {
                case OR:
                    return "or";

                case AND:
                    return "and";

                default:
                    throw new IllegalArgumentException("invalid logic operand \"" + logicOp + "\" received!");
            }
        }
    }

    private final LogicOp logicOp;
    private final List<ConditionAction> innerConditions;

    /**
     * Allows for multiple conditioning, using both simple and multiple conditions.
     * This condition does not contain a specific condition to be tested, but allows for the OR / AND logic operands
     * to compile multiple simple conditions into a bigger block.
     * Referred by the singularity value "multiple"
     *
     * @param logicOp the logic operand OR/AND that will be used
     */
    public MultiConditionAction(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityToInvokeOn, String logicOp, ThenOrElseActions thenActions, ThenOrElseActions elseActions, boolean isMainCondition) {
        super(worldContext, mainEntityContext, secondaryEntity, entityToInvokeOn, thenActions, elseActions, isMainCondition);
        this.logicOp = LogicOp.getLogicOp(logicOp);
        this.innerConditions = new ArrayList<>();
    }

    public MultiConditionAction(World worldContext, String mainEntityContext, String entityName, String logicOp, ThenOrElseActions thenActions, ThenOrElseActions elseActions, boolean isMainCondition) {
        super(worldContext, mainEntityContext, entityName, thenActions, elseActions, isMainCondition);
        this.logicOp = LogicOp.getLogicOp(logicOp);
        this.innerConditions = new ArrayList<>();
    }

    public void addInnerCondition(ConditionAction conditionAction) {
        innerConditions.add(conditionAction);
    }

    @Override
    public String getActionTypeString() {
        return "condition";
    }


    @Override
    public void invoke(Entity mainEntity, Entity secondaryEntity) {
        List<Boolean> innerConditionsResults = new ArrayList<>();

        if(innerConditions.size() == 0)
            throw new RuntimeException("no inner conditions were set inside multi-condition action!");

        for (ConditionAction conditionAction : innerConditions) {
            // Try to activate the inner condition
            conditionAction.invoke(mainEntity, secondaryEntity);

            // Check if the inner condition was activated, add the result to the list
            if (conditionAction.isActivated()) {
                innerConditionsResults.add(true);

                // Reset condition activation for future use
                conditionAction.setActivated(false);
            } else
                innerConditionsResults.add(false);
        }

        setActivated(innerConditionsResults);

        // Invoke "then" actions if both the multi-condition was activated and the condition is a main condition, otherwise invoke "else" actions
        invokeActionsSet(mainEntity, secondaryEntity, isActivated() && isMainCondition());
    }

    @Override
    public void invoke(Entity entity) {
        List<Boolean> innerConditionsResults = new ArrayList<>();

        if(innerConditions.size() == 0)
            throw new RuntimeException("no inner conditions were set inside multi-condition action!");

        for (ConditionAction conditionAction : innerConditions) {
            // Try to activate the inner condition
            conditionAction.invoke(entity);

            // Check if the inner condition was activated, add the result to the list
            if (conditionAction.isActivated()) {
                innerConditionsResults.add(true);

                // Reset condition activation for future use
                conditionAction.setActivated(false);
            } else
                innerConditionsResults.add(false);
        }

        setActivated(innerConditionsResults);

        // Invoke "then" actions if both the multi-condition was activated and the condition is a main condition, otherwise invoke "else" actions
        invokeActionsSet(entity, isActivated() && isMainCondition());
    }


    /**
     * Check if the condition is activated.
     * @param innerConditionsResults Boolean results of inner conditions.
     */
    private void setActivated(List<Boolean> innerConditionsResults) {
        switch (logicOp) {
            case OR:
                // Check if at least one of the inner conditions resulted as "true", if so activate this condition
                setActivated(innerConditionsResults.contains(true));
                break;

            case AND:
                // Check if at least one of the inner conditions resulted as "false",
                // if so deactivate this condition, otherwise activate it
                setActivated(!innerConditionsResults.contains(false));
                break;
        }
    }


    @Override
    public Action copy(World worldContext) {
        ThenOrElseActions thenActions = null, elseActions = null;

        // Multi condition can be nested inside another multi condition, and not have "Then" or "Else" actions
        if(isMainCondition()) {
            thenActions = new ThenOrElseActions(getThenActions(), worldContext);
            elseActions = new ThenOrElseActions(getElseActions(), worldContext);
        }

        MultiConditionAction retMultiConditionAction =
                new MultiConditionAction(
                        worldContext,
                        getMainEntityContext(),
                        getSecondaryEntity(),
                        getEntityToInvokeOnName(),
                        LogicOp.getLogicOpString(logicOp),
                        thenActions,
                        elseActions,
                        isMainCondition()
                );

        // Copy inner conditions
        for (ConditionAction conditionAction : innerConditions) {
            retMultiConditionAction.addInnerCondition((ConditionAction) conditionAction.copy(worldContext));  // We know the inner conditions are of condition action, so we can convert them
        }

        return retMultiConditionAction;
    }


    @Override
    public ActionDTO getActionDTO() {
        ActionDTO retActionDTO = getBasicActionDTO();

        retActionDTO.addArgument("Entity Name", getEntityToInvokeOnName());
        retActionDTO.addArgument("Logic Operand", LogicOp.getLogicOpString(logicOp));
        retActionDTO.addArgument("Amount of Inner Conditions", Integer.valueOf(innerConditions.size()).toString());

        retActionDTO.addExtraInfo("Amount of 'then' Actions", Integer.valueOf(getThenActions().getActionsToInvoke().size()).toString());
        retActionDTO.addExtraInfo("Amount of 'else' Actions", Integer.valueOf(getElseActions().getActionsToInvoke().size()).toString());

        return retActionDTO;
    }
}
