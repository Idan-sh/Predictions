package com.idansh.engine.actions.condition;

import com.idansh.engine.world.World;

public class MultiConditionAction extends ConditionAction{
    private final String logicOp;

    /**
     * Allows for multiple conditioning, using both simple and multiple conditions.
     * This condition does not contain a specific condition to be tested, but allows for the OR / AND logic operands
     * to compile multiple simple conditions into a bigger block.
     * Referred by the singularity value "multiple"
     *
     * @param logicOp the logic operand OR/AND that will be used
     */
    public MultiConditionAction(World worldContext, String entityContext, String logicOp) {
        super(worldContext, entityContext, Type.MULTI);
        this.logicOp = logicOp;
    }
}
