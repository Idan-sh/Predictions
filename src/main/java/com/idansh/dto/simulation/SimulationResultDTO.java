package com.idansh.dto.simulation;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.engine.helpers.SimulationTime;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains details/information of a past simulation.
 */
public class SimulationResultDTO {
    private final int id;
    private final SimulationTime simulationTime;
    private final int completedTicks, maxTicks;
    private final List<EntityDTO> entityDTOList;
    private final String endReason;

    public SimulationResultDTO(int id, SimulationTime simulationTime, int completedTicks, int maxTicks, String endReason) {
        this.id = id;
        this.simulationTime = simulationTime;
        this.completedTicks = completedTicks;
        this.maxTicks = maxTicks;
        this.entityDTOList = new ArrayList<>();
        this.endReason = endReason;
    }

    public void addEntityDTO(EntityDTO entityDTO) {
        entityDTOList.add(entityDTO);

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

    public SimulationTime getSimulationTime() {
        return simulationTime;
    }

    public String getEndReason() {
        return endReason;
    }
}
