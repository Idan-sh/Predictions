package com.idansh.engine.actions.condition;

import com.idansh.engine.world.World;

import java.util.ArrayList;
import java.util.List;

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
                    throw new IllegalArgumentException("invalid logic operand \"" + logicOpStr + "\" received!");
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
    public MultiConditionAction(World worldContext, String entityContext, String logicOp, ThenOrElseActions thenActions, ThenOrElseActions elseActions, boolean isMainCondition) {
        super(worldContext, entityContext, Type.MULTI, thenActions, elseActions, isMainCondition);
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
    public void invoke() {
        List<Boolean> innerConditionsResults = new ArrayList<>();

        innerConditions.forEach(
                conditionAction -> {
                    conditionAction.invoke();

                    // Check if the inner condition was activated, add the result to the list
                    if(conditionAction.isActivated())
                        innerConditionsResults.add(true);
                    else
                        innerConditionsResults.add(false);
                }
        );

        switch (logicOp) {
            case OR:
                // Check if one of the inner conditions resulted as "true", invoke the proper actions set
                invokeActionsSet(innerConditionsResults.contains(true));

            case AND:
                boolean flag = true; // Flag that checks if there is a false inner condition
                for(Boolean innerConditionRes : innerConditionsResults) {
                    // Check if the current condition result is false, if so the AND logic is also false
                    if (!innerConditionRes) {
                        invokeActionsSet(false); // Invoke the "else" actions set
                        flag = false;
                    }
                }
                // There was no inner condition of value false
                if(flag)
                    invokeActionsSet(true); // Invoke the "then" actions set

        }
    }
}
