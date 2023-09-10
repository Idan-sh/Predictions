package com.idansh.javafx.helpers;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.engine.helpers.SimulationTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Defines an item in the table view of the simulation executions in the "Results" tab.
 */
public class ResultsTableItem {
    private final int id;
    private final String startTime;
    private final StringProperty endTime;
    private final StringProperty status;

    private SimulationTime simulationTime;
    private final List<EntityDTO> entitiesList;

    private int completedTicks;
    private final int maxTicks;


    public ResultsTableItem(int id, List<EntityDTO> entitiesList, SimulationTime simulationTime, String status, int completedTicks, int maxTicks) {
        this.id = id;
        this.startTime = simulationTime.getStartDateString();
        this.endTime = new SimpleStringProperty(simulationTime.getEndDateString());
        this.entitiesList = entitiesList;
        this.simulationTime = simulationTime;
        this.completedTicks = completedTicks;
        this.maxTicks = maxTicks;
        this.status = new SimpleStringProperty(status);
    }


    /**
     * Update the running execution table item with newly given data.
     */
    public void update(SimulationTime simulationTime, String status, int completedTicks) {
        this.endTime.set(simulationTime.getEndDateString());
        this.status.set(status);

        this.simulationTime = simulationTime;
        this.completedTicks = completedTicks;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status.get();
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

    public List<EntityDTO> getEntitiesList() {
        return entitiesList;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.get();
    }


    public StringProperty endTimeProperty() {
        return endTime;
    }

    public StringProperty statusProperty() {
        return status;
    }
}
