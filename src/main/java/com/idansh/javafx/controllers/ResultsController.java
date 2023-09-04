package com.idansh.javafx.controllers;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.simulation.RunningSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.javafx.helpers.ResultsTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;

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
    private TableColumn<ResultsTableItem, String> endTableColumn;
    @FXML
    private TableColumn<ResultsTableItem, String> statusTableColumn;

    // Execution Details Components:
    @FXML
    private ListView<String> progressListView;
    @FXML
    private TableView<EntityDTO> entityAmountsTableView;
    @FXML
    private TableColumn<EntityDTO, String> nameTableColumn;
    @FXML
    private TableColumn<EntityDTO, Integer> amountTableColumn;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup Execution List TableView:
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        startTableColumn.setCellValueFactory(new PropertyValueFactory<>("startDateString"));
        endTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDateString"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Setup Entity Amounts TableView:
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("currAmountInPopulation"));
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
                                        itemResult.getEntityDTOList(),
                                        itemResult.getSimulationTime(),
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
                                        itemRunning.getEntityDTOList(),
                                        itemRunning.getSimulationTime(),
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
     * Activates on user simulation execution item click.
     * Shows more information about the chosen execution -
     *      shows how many ticks were completed,
     *      and how much time had passed since the beginning of the simulation (in seconds).
     */
    public void selectTableItem() {
        BiFunction<Integer, Integer, String> ticksFormatter =
                (completedTicks, maxTicks) -> String.format("Ticks Reached: %d/%d", completedTicks, maxTicks);
        Function<Float, String> timerFormatter = (timePassed) -> String.format("Time Passed: %.3f Seconds", timePassed);

        ResultsTableItem selectedItem = executionListTableView.getSelectionModel().getSelectedItem();

        if(selectedItem != null) {
            // todo- output data to another component, this will query the engine every 200-300 ms
            //  and request the most recent details of the simulation chosen. TASK thread will be appointed.

            // Remove previously shown items
            progressListView.getItems().clear();

            // Add completed ticks and time passed to the list view
            progressListView.getItems().addAll(ticksFormatter.apply(
                            selectedItem.getCompletedTicks(),
                            selectedItem.getMaxTicks()
                    ),
                    timerFormatter.apply(selectedItem.getSimulationTime().getSecondsPassed())
            );

            // Add entity amounts to the table
            entityAmountsTableView.getItems().addAll(selectedItem.getEntitiesList());
        }
    }
}
