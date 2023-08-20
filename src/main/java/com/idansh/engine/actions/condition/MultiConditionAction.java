package com.idansh.engine.actions.condition;

import com.idansh.engine.entity.Entity;
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
    public void invoke(Entity entity) {
        System.out.println("\ninvoking multi-condition");

        List<Boolean> innerConditionsResults = new ArrayList<>();

        innerConditions.forEach(
                conditionAction -> {
                    System.out.println("\nrunning inner condition");
                    conditionAction.invoke(entity);

                    // Check if the inner condition was activated, add the result to the list
                    if(conditionAction.isActivated())
                    {
                        innerConditionsResults.add(true);
                        System.out.println("added inner condition result true");

                        // Reset condition activation for future use
                        conditionAction.setActivated(false);
                    }
                    else
                    {
                        innerConditionsResults.add(false);
                        System.out.println("added inner condition result false");
                    }
                }
        );

        switch (logicOp) {
            case OR:
                System.out.println("got OR logic op");
                System.out.println("res is: " + innerConditionsResults.contains(true));
                // Check if one of the inner conditions resulted as "true", invoke the proper actions set
                invokeActionsSet(entity, innerConditionsResults.contains(true));

            case AND:
                System.out.println("got AND logic op");
                boolean flag = true; // Flag that checks if there is a false inner condition
                for(Boolean innerConditionRes : innerConditionsResults) {
                    System.out.println("in loop got: " + innerConditionRes);
                    // Check if the current condition result is false, if so the AND logic is also false
                    if (!innerConditionRes) {
                        invokeActionsSet(entity, false); // Invoke the "else" actions set
                        flag = false;
                    }
                }
                System.out.println("res is: " + flag);

                // There was no inner condition of value false
                if(flag)
                    invokeActionsSet(entity, true); // Invoke the "then" actions set

        }
    }
}
