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
    private final IntegerProperty id;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty status;

    private SimulationTime simulationTime;
    private int completedTicks, maxTicks;
    private List<EntityDTO> entitiesList;

    public ResultsTableItem(int id, List<EntityDTO> entitiesList, SimulationTime simulationTime, String status, int completedTicks, int maxTicks) {
        this.id = new SimpleIntegerProperty(id);
        this.startTime = new SimpleStringProperty(simulationTime.getStartDateString());
        this.endTime = new SimpleStringProperty(simulationTime.getEndDateString());
        this.entitiesList = entitiesList;
        this.simulationTime = simulationTime;
        this.completedTicks = completedTicks;
        this.maxTicks = maxTicks;
        this.status = new SimpleStringProperty(status);
    }

    public void update(ResultsTableItem resultsTableItem) {
        this.id.set(resultsTableItem.getId());
        this.startTime.set(resultsTableItem.getStartTime());
        this.endTime.set(resultsTableItem.getEndTime());
        this.status.set(resultsTableItem.getStatus());

        this.entitiesList = resultsTableItem.getEntitiesList();
        this.simulationTime = resultsTableItem.getSimulationTime();
        this.completedTicks = resultsTableItem.getCompletedTicks();
        this.maxTicks = resultsTableItem.getMaxTicks();
    }

    public int getId() {
        return id.get();
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
        return startTime.get();
    }

    public String getEndTime() {
        return endTime.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public StringProperty statusProperty() {
        return status;
    }
}
