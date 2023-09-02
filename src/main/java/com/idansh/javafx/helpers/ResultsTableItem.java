package com.idansh.javafx.helpers;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Defines an item in the table view of the simulation executions in the "Results" tab.
 */
public class ResultsTableItem {
    private final int id;
    private final LocalDateTime startDate;
    private final String startDateString;
    private final long startTimeInMillis, endTimeInMillis;
    private final int completedTicks, maxTicks;
    private final String status;

    public ResultsTableItem(int id, LocalDateTime startDate, long startTimeInMillis, long endTimeInMillis, String status, int completedTicks, int maxTicks) {
        this.id = id;
        this.startDate = startDate;
        this.startTimeInMillis = startTimeInMillis;
        this.endTimeInMillis = endTimeInMillis;
        this.completedTicks = completedTicks;
        this.maxTicks = maxTicks;
        this.status = status;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
        startDateString = dateTimeFormatter.format(startDate);
    }

    public int getId() {
        return id;
    }


    public String getStatus() {
        return status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public long getEndTimeInMillis() {
        return endTimeInMillis;
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

    public String getStartDateString() {
        return startDateString;
    }
}
