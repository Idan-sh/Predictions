package com.idansh.javafx.controllers;

import com.idansh.dto.simulation.LoadedSimulationDTO;
import com.idansh.dto.simulation.RunningSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.javafx.helpers.ResultsTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    private TableColumn<ResultsTableItem, String> endTableColumn;
    @FXML
    private TableColumn<ResultsTableItem, String> statusTableColumn;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        endTableColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
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
                                        itemResult.getDateTimeString(),
                                        COMPLETED)
                        );
                    } else if(item instanceof RunningSimulationDTO) {
                        RunningSimulationDTO itemRunning = (RunningSimulationDTO) item;
                        executionListTableView.getItems().add(
                                new ResultsTableItem(
                                        itemRunning.getId(),
                                        NOT_AVAILABLE,
                                        IN_PROGRESS)
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
        // todo- output data to another component
    }
}
