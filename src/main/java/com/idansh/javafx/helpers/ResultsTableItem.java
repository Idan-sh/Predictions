package com.idansh.javafx.helpers;

import com.idansh.engine.helpers.SimulationTime;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Defines an item in the table view of the simulation executions in the "Results" tab.
 */
public class ResultsTableItem {
    private final int id;
    private final SimulationTime simulationTime;
    private final String startDateString, endDateString;
    private final int completedTicks, maxTicks;
    private final String status;

    public ResultsTableItem(int id, SimulationTime simulationTime, String status, int completedTicks, int maxTicks) {
        this.id = id;
        this.simulationTime = simulationTime;
        this.completedTicks = completedTicks;
        this.maxTicks = maxTicks;
        this.status = status;
        this.startDateString = simulationTime.getStartDateString();
        this.endDateString = simulationTime.getEndDateString();
    }

    public int getId() {
        return id;
    }


    public String getStatus() {
        return status;
    }

    public SimulationTime getSimulationTime() {
        return simulationTime;
    }

    public int getCompletedTicks() {
        return completedTicks;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }
}
