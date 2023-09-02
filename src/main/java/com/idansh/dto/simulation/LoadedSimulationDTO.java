package com.idansh.dto.simulation;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains details/information of a current simulation loaded into the program.
 */
public class LoadedSimulationDTO {
    private final List<EntityDTO> entityDTOList;
    private final List<RuleDTO> ruleDTOList;
    private final List<TerminationRuleDTO> terminationRuleDTOList;
    private final EnvironmentVariablesListDTO environmentVariablesListDTO;


    public LoadedSimulationDTO(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        this.entityDTOList = new ArrayList<>();
        this.ruleDTOList = new ArrayList<>();
        this.terminationRuleDTOList = new ArrayList<>();
        this.environmentVariablesListDTO = environmentVariablesListDTO;
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
}
