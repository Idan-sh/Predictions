package com.idansh.dto.simulation;

import com.idansh.dto.entity.EntityDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains details/information of a past simulation.
 */
public class SimulationResultDTO {
    private final LocalDateTime dateTime;
    private final String dateTimeString;
    private final int id;
    private final List<EntityDTO> entityDTOList;

    public SimulationResultDTO(LocalDateTime dateTime, String dateTimeString, int id) {
        this.dateTime = dateTime;
        this.dateTimeString = dateTimeString;
        this.id = id;
        this.entityDTOList = new ArrayList<>();
    }

    public void addEntityDTO(EntityDTO entityDTO) {
        entityDTOList.add(entityDTO);

    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public int getId() {
        return id;
    }

    public List<EntityDTO> getEntityDTOList() {
        return entityDTOList;
    }
}
