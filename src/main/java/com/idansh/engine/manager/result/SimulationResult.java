package com.idansh.engine.manager.result;

import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.helpers.SimulationTime;
import com.idansh.engine.world.World;

import java.util.List;

/**
 * Holds information on a single simulation that was completed.
 */
public class SimulationResult {
    private final int id;
    private final SimulationTime simulationTime;
    private final int completedTicks, maxTicks;
    private final String endReason;
    private final EntityManager entityManager;


    public SimulationResult(int id, SimulationTime simulationTime, String endReason , EntityManager entityManager, int completedTicks, int maxTicks) {
        this.id = id;
        this.simulationTime = simulationTime;
        this.endReason = endReason;
        this.entityManager = entityManager;
        this.completedTicks = completedTicks;
        this.maxTicks = maxTicks;
    }

    public int getId() {
        return id;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public String getEndReason() {
        return endReason;
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
}
