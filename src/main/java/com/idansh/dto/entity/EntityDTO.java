package com.idansh.dto.entity;

import com.idansh.dto.property.PropertyDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains details/information of a main entity in the world.
 */
public class EntityDTO {
    private final String name;
    private final int amountInPopulation;
    private final List<PropertyDTO> propertyDTOList;

    public EntityDTO(String name, int amountInPopulation) {
        this.name = name;
        this.amountInPopulation = amountInPopulation;
        this.propertyDTOList = new ArrayList<>();
    }

    public void addPropertyDTOtoList(PropertyDTO propertyDTO) {
        propertyDTOList.add(propertyDTO);
    }

    public String getName() {
        return name;
    }

    public int getAmountInPopulation() {
        return amountInPopulation;
    }

    public List<PropertyDTO> getPropertyDTOList() {
        return propertyDTOList;
    }
}
