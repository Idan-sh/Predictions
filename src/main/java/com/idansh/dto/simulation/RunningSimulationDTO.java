package com.idansh.dto.simulation;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * UI will query this DTO every a certain time period,
 * to receive the current state of the simulation.
 */
public class RunningSimulationDTO {
    private final int id;
    private final List<EntityDTO> entityDTOList;
    private final List<RuleDTO> ruleDTOList;
    private final List<TerminationRuleDTO> terminationRuleDTOList;
    private final EnvironmentVariablesListDTO environmentVariablesListDTO;
    private final LocalDateTime startDate;
    private final long startTimeInMillis;
    private final int completedTicks, maxTicks;


    public RunningSimulationDTO(int id, EnvironmentVariablesListDTO environmentVariablesListDTO, LocalDateTime startDate, long startTimeInMillis, int completedTicks, int maxTicks) {
        this.id = id;
        this.entityDTOList = new ArrayList<>();
        this.ruleDTOList = new ArrayList<>();
        this.terminationRuleDTOList = new ArrayList<>();
        this.environmentVariablesListDTO = environmentVariablesListDTO;
        this.startDate = startDate;
        this.startTimeInMillis = startTimeInMillis;
        this.completedTicks = completedTicks;
        this.maxTicks = maxTicks;
    }


    public EnvironmentVariablesListDTO getEnvironmentVariablesListDTO() {
        return environmentVariablesListDTO;
    }

    public void addEntityDTO(EntityDTO entityDTO) {
        entityDTOList.add(entityDTO);
    }

    public void addRuleDTO(RuleDTO ruleDTO) {
        ruleDTOList.add(ruleDTO);
    }

    public void addTerminationRuleDTO(TerminationRuleDTO terminationRuleDTO) {
        terminationRuleDTOList.add(terminationRuleDTO);
    }

    public List<EntityDTO> getEntityDTOList() {
        return entityDTOList;
    }

    public List<RuleDTO> getRuleDTOList() {
        return ruleDTOList;
    }

    public List<TerminationRuleDTO> getTerminationRuleDTOList() {
        return terminationRuleDTOList;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public int getCompletedTicks() {
        return completedTicks;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }
}
