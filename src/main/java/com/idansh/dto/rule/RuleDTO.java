package com.idansh.dto.rule;

import com.idansh.dto.action.ActionDTO;

import java.util.ArrayList;
import java.util.List;

public class RuleDTO {
    private final String name;
    private final int ticks;
    private final double probability;
    private final int nofActions;
    private final List<ActionDTO> actionDTOList;

    public RuleDTO(String name, int ticks, double probability, int nofActions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.nofActions = nofActions;
        this.actionDTOList = new ArrayList<>();
    }

    public void addActionDTO(ActionDTO actionDTO) {
        actionDTOList.add(actionDTO);
    }

    public String getName() {
        return name;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }

    public int getNofActions() {
        return nofActions;
    }

    public List<ActionDTO> getActionDTOList() {
        return actionDTOList;
    }

    @Override
    public String toString() {
        return name;
    }
}
