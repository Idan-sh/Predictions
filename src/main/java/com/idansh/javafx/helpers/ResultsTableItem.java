package com.idansh.javafx.helpers;

/**
 * Defines an item in the table view of the simulation executions in the "Results" tab.
 */
public class ResultsTableItem {
    private final int id;
    private final String endTime;
    private final String status;

    public ResultsTableItem(int id, String endTime, String status) {
        this.id = id;
        this.endTime = endTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }
}
