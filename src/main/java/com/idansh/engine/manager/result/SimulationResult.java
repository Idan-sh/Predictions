package com.idansh.engine.manager.result;

import com.idansh.engine.entity.EntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds information on a single simulation that was completed.
 */
public class SimulationResult {
    private final int id;                   // a unique ID identifier of the simulation
    private final LocalDateTime dateTime;   // The date and time of the time the simulation ran
    private final String endReason;
    private final EntityManager entityManager;

    public SimulationResult(String endReason, EntityManager entityManager) {
        this.dateTime = LocalDateTime.now();
        this.id = SimulationIdGenerator.getID();
        this.endReason = endReason;
        this.entityManager = entityManager;
    }

    public int getId() {
        return id;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getEndReason() {
        return endReason;
    }

    public String getDateTimeString()
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        return dateTimeFormatter.format(dateTime);
    }
}
