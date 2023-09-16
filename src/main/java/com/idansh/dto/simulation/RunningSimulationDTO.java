package com.idansh.dto.simulation;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;
import com.idansh.engine.helpers.SimulationTime;

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
    private final SimulationTime simulationTime;
    private final int completedTicks;
    private final Integer maxTicks;


    public RunningSimulationDTO(int id, EnvironmentVariablesListDTO environmentVariablesListDTO, SimulationTime simulationTime, int completedTicks, Integer maxTicks) {
        this.id = id;
        this.entityDTOList = new ArrayList<>();
        this.ruleDTOList = new ArrayList<>();
        this.terminationRuleDTOList = new ArrayList<>();
        this.environmentVariablesListDTO = environmentVariablesListDTO;
        this.simulationTime = simulationTime;
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

    public SimulationTime getSimulationTime() {
        return simulationTime;
    }

    public int getCompletedTicks() {
        return completedTicks;
    }

    public Integer getMaxTicks() {
        return maxTicks;
    }
}
