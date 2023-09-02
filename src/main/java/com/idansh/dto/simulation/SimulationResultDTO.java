package com.idansh.dto.simulation;

import com.idansh.dto.entity.EntityDTO;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains details/information of a past simulation.
 */
public class SimulationResultDTO {
    private final int id;
    private final LocalDateTime startDate;
    private final long startTimeInMillis, endTimeInMillis;
    private final int completedTicks, maxTicks;
    private final List<EntityDTO> entityDTOList;

    public SimulationResultDTO(int id, LocalDateTime startDate, long startTimeInMillis, long endTimeInMillis, int completedTicks, int maxTicks) {
        this.id = id;
        this.startDate= startDate;
        this.startTimeInMillis = startTimeInMillis;
        this.endTimeInMillis = endTimeInMillis;
        this.completedTicks = completedTicks;
        this.maxTicks = maxTicks;
        this.entityDTOList = new ArrayList<>();
    }

    public void addEntityDTO(EntityDTO entityDTO) {
        entityDTOList.add(entityDTO);

    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public int getId() {
        return id;
    }

    public List<EntityDTO> getEntityDTOList() {
        return entityDTOList;
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

    public long getEndTimeInMillis() {
        return endTimeInMillis;
    }
}
