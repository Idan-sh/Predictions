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
    private final Long startTimeInMillis;
    private Long endTimeInMillis;


    /**
     * Set the simulation's start times to the current time.
     * End times will need to be set when the simulation ends.
     */
    public SimulationTime() {
        this.startDate = LocalDateTime.now();
        this.endDate = null;

        this.startTimeInMillis = System.currentTimeMillis();
        this.endTimeInMillis = null;
    }

    /**
     * Sets the simulation's end times to the current time.
     */
    public void setEndTimes() {
        this.endDate = LocalDateTime.now();
        this.endTimeInMillis = System.currentTimeMillis();
    }

    /**
     * @return The time in seconds that has passed from the start to the end of the simulation.
     *         Returns -1 in case the end times were not set.
     */
    public float getSecondsPassed() {
        // Check if the timer was stopped
        if(endTimeInMillis == null)
            return (System.currentTimeMillis() - startTimeInMillis) / 1000f;

        return (endTimeInMillis - startTimeInMillis) / 1000f;
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

    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    public long getEndTimeInMillis() {
        return endTimeInMillis;
    }
}
