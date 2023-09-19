package com.idansh.dto.entity;

import com.idansh.dto.property.PropertyDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains details/information of a main entity in the world.
 */
public class EntityDTO {
    private final String name;
    private final Integer currAmountInPopulation;
    private final Integer initAmountInPopulation;
    private final List<PropertyDTO> propertyDTOList;

    public EntityDTO(String name, Integer currAmountInPopulation, Integer initAmountInPopulation) {
        this.name = name;
        this.currAmountInPopulation = currAmountInPopulation;
        this.initAmountInPopulation = initAmountInPopulation;
        this.propertyDTOList = new ArrayList<>();
    }

    public void addPropertyDTOtoList(PropertyDTO propertyDTO) {
        propertyDTOList.add(propertyDTO);
    }

    public String getName() {
        return name;
    }

    public Integer getCurrAmountInPopulation() {
        return currAmountInPopulation;
    }

    public Integer getInitAmountInPopulation() {
        return initAmountInPopulation;
    }

    public List<PropertyDTO> getPropertyDTOList() {
        return propertyDTOList;
    }

    @Override
    public String toString() {
        return name;
    }
}
