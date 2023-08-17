package com.idansh.dto.simulation;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains details/information of a current simulation loaded into the program.
 */
public class CurrentSimulationDTO {
    List<EntityDTO> entityDTOList;
    List<RuleDTO> ruleDTOList;
    List<TerminationRuleDTO> terminationRuleDTOList;

    public CurrentSimulationDTO() {
        entityDTOList = new ArrayList<>();
        ruleDTOList = new ArrayList<>();
        terminationRuleDTOList = new ArrayList<>();
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
