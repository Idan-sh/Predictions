package com.idansh.javafx.controllers;

import com.idansh.dto.simulation.LoadedSimulationDTO;
import com.idansh.dto.simulation.RunningSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.javafx.helpers.ResultsTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

/**
 * FXML Controller class for the third screen of the application.
 * this screen will be in charge of showing the result of previous ran simulations.
 */
public class ResultsController implements Initializable {
    private AppController mainController;

    private final String COMPLETED = "Completed", IN_PROGRESS = "In Progress", NOT_AVAILABLE = "N/A";

    // Execution List Components:
    @FXML
    private TableView<ResultsTableItem> executionListTableView;
    @FXML
    private TableColumn<ResultsTableItem, String> idTableColumn;
    @FXML
    private TableColumn<ResultsTableItem, String> startTableColumn;
    @FXML
    private TableColumn<ResultsTableItem, String> statusTableColumn;

    // Execution Details Components:
    @FXML
    private ListView<String> progressListView;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        startTableColumn.setCellValueFactory(new PropertyValueFactory<>("startDateString"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    /**
     * Shows a table view of all the current and previous simulation executions.
     * Displays the ID, End time, and status of each simulation execution.
     */
    public void showExecutions() {
        // Clear previously added items
        executionListTableView.getItems().clear();

        // Get a List of all executions currently in the program.
        // Each list item should be of the type SimulationResultDTO or CurrentSimulationDTO.
        List<Object> executionsList = mainController.getSimulationExecutions();

        executionsList.forEach(
                item -> {
                    if(item instanceof SimulationResultDTO) {
                        SimulationResultDTO itemResult = (SimulationResultDTO) item;
                        executionListTableView.getItems().add(
                                new ResultsTableItem(
                                        itemResult.getId(),
                                        itemResult.getStartDate(),
                                        itemResult.getStartTimeInMillis(),
                                        itemResult.getEndTimeInMillis(),
                                        COMPLETED,
                                        itemResult.getCompletedTicks(),
                                        itemResult.getMaxTicks()
                                        )
                        );
                    } else if(item instanceof RunningSimulationDTO) {
                        RunningSimulationDTO itemRunning = (RunningSimulationDTO) item;
                        executionListTableView.getItems().add(
                                new ResultsTableItem(
                                        itemRunning.getId(),
                                        itemRunning.getStartDate(),
                                        itemRunning.getStartTimeInMillis(),
                                        -1,
                                        IN_PROGRESS,
                                        itemRunning.getCompletedTicks(),
                                        itemRunning.getMaxTicks()
                                )
                        );
                    } else throw new IllegalArgumentException("Invalid execution type received: \""
                            + item.getClass()
                            + "\", only accepts executions of type SimulationResultDTO or CurrentSimulationDTO");
                }
        );
    }


    /**
     * Activates on user table item click.
     * Shows more information about the chosen execution.
     */
    public void selectTableItem() {
        BiFunction<Integer, Integer, String> ticksFormatter =
                (completedTicks, maxTicks) -> String.format("Ticks Reached: %d/%d", completedTicks, maxTicks);
        BiFunction<Long, Long, String> timerFormatter =
                (startTime, endTime) -> String.format("Time Passed: %.3f Seconds", (endTime - startTime) / 1000f);

        ResultsTableItem selectedItem = executionListTableView.getSelectionModel().getSelectedItem();

        // todo- output data to another component, this will query the engine every 200-300 ms
        //  and request the most recent details of the simulation chosen. TASK thread will be appointed.

        // ### for the time being, only shows the completed simulations: ###
        // Remove previously shown items
        progressListView.getItems().clear();

        // Check if the selected execution is running or completed
        if(selectedItem.getStatus().equals(COMPLETED)) {
            progressListView.getItems().addAll(ticksFormatter.apply(
                            selectedItem.getCompletedTicks(),
                            selectedItem.getMaxTicks()
                    ),
                    timerFormatter.apply(
                            selectedItem.getStartTimeInMillis(),
                            selectedItem.getEndTimeInMillis()
                    )
            );
        } else {
            // todo- finish in progress item
        }
    }
}
