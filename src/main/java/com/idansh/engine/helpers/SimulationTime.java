package com.idansh.engine.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds the times of the start and end of a simulation's run process,
 * in both formats of milliseconds and LocalDateTime.
 */
public class SimulationTime {
    private final String DATE_TIME_PATTERN = "dd-MM-yyyy | HH:mm:ss";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    // Date and time of when the simulation started running
    private final LocalDateTime startDate;
    private LocalDateTime endDate;

    // Time in milliseconds since epoch to the start of the running process
    private boolean isPaused;
    private long elapsedTime;
    private long lastContinueTime;


    /**
     * Set the simulation's start times to the current time.
     * End times will need to be set when the simulation ends.
     */
    public SimulationTime() {
        this.startDate = LocalDateTime.now();
        this.endDate = null;

        this.lastContinueTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        this.isPaused = false;
    }

    /**
     * Copy a given simulation time's start date and current elapsed time,
     * and finish it.
     */
    public SimulationTime(SimulationTime simulationTime) {
        this.startDate = simulationTime.startDate;
        this.elapsedTime = simulationTime.elapsedTime;
        this.lastContinueTime = simulationTime.lastContinueTime;
        this.isPaused = simulationTime.isPaused;
        this.endDate = null;

        if(!isPaused)
            this.elapsedTime += System.currentTimeMillis() - lastContinueTime;
    }

    /**
     * Sets the simulation's end times to the current time.
     */
    public void finish() {
        this.endDate = LocalDateTime.now();

        if(!isPaused)
            this.elapsedTime += System.currentTimeMillis() - lastContinueTime;
    }

    public void pauseElapsedTime() {
        this.isPaused = true;
        this.elapsedTime += System.currentTimeMillis() - lastContinueTime;
    }

    public void resumeElapsedTime() {
        this.isPaused = false;
        this.lastContinueTime = System.currentTimeMillis();
    }

    /**
     * @return The time in seconds that has passed from the start of the simulation.
     */
    public float getElapsedTime() {
        return elapsedTime / 1000f;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Returns the date of the simulation start as a string
     * in the format "dd-MM-yyyy | HH:mm:ss".
     */
    public String getStartDateString() {
        return dateTimeFormatter.format(startDate);
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Returns the date of the simulation end as a string
     * in the format "dd-MM-yyyy | HH:mm:ss".
     * And returns "N/A" if the simulation has not yet ended.
     */
    public String getEndDateString() {
        if(endDate != null)
            return dateTimeFormatter.format(endDate);

        return "N/A";
    }
}
