package com.idansh.engine.manager.result;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds information on a single simulation that was completed.
 */
public class SimulationResult {
    private final int id;                   // a unique ID identifier of the simulation
    private final LocalDateTime dateTime;   // The date and time of the time the simulation ran

    public SimulationResult() {
        dateTime = LocalDateTime.now();
        id = SimulationIdGenerator.getID();
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDateTimeString()
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        return dateTimeFormatter.format(dateTime);
    }
}
