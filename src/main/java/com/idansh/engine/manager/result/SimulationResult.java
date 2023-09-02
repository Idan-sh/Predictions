package com.idansh.engine.manager.result;

import com.idansh.engine.entity.EntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds information on a single simulation that was completed.
 */
public class SimulationResult {
    private final int id;                   // a unique ID identifier of the simulation
    private final LocalDateTime startDate;  // The date and time of when the simulation started
    private final long startTimeInMillis, endTimeInMillis;   // The time of when the simulation started and finished in milliseconds
    private final int completedTicks, maxTicks;
    private final String endReason;
    private final EntityManager entityManager;

    public SimulationResult(int id, LocalDateTime startDate, long startTimeInMillis, String endReason ,EntityManager entityManager, int completedTicks, int maxTicks) {
        this.id = id;
        this.startDate = startDate;
        this.startTimeInMillis = startTimeInMillis;
        this.endTimeInMillis = System.currentTimeMillis();
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

    public String getDateTimeString()
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        return dateTimeFormatter.format(startDate);
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

    public long getEndTimeInMillis() {
        return endTimeInMillis;
    }
}
