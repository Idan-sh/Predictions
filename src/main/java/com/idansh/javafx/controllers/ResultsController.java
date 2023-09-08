package com.idansh.javafx.controllers;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.simulation.RunningSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.javafx.helpers.ResultsTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
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

    // Execution Results Components:
    @FXML
    private ChoiceBox<EntityDTO> entityChoiceBox;
    @FXML
    private ChoiceBox<PropertyDTO> propertyChoiceBox;
    @FXML
    private TreeView<String> propertyDetailsTreeView;
    private int chosenExecutionID;


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

        // Setup on action events for entity and property choice boxes:
        entityChoiceBox.setOnAction(event -> onEntitySelect(entityChoiceBox.getValue()));
        propertyChoiceBox.setOnAction(event -> onPropertySelect(propertyChoiceBox.getValue()));

        // Setup Execution Results tree view:
        propertyDetailsTreeView.setRoot(new TreeItem<>("root-item"));

        // todo- create thread that calls for showExecutions every 200ms
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

        if (selectedItem != null) {
            // todo- output data to another component, this will query the engine every 200-300 ms
            //  and request the most recent details of the simulation chosen. TASK thread will be appointed.
            chosenExecutionID = selectedItem.getId();

            // Remove previously shown items
            progressListView.getItems().clear();
            entityAmountsTableView.getItems().clear();
            entityChoiceBox.getItems().clear();
            propertyChoiceBox.getItems().clear();
            propertyDetailsTreeView.getRoot().getChildren().clear();

            // Add completed ticks and time passed to the list view
            progressListView.getItems().addAll(ticksFormatter.apply(
                            selectedItem.getCompletedTicks(),
                            selectedItem.getMaxTicks()
                    ),
                    timerFormatter.apply(selectedItem.getSimulationTime().getSecondsPassed())
            );

            // Add entity amounts to the table
            entityAmountsTableView.getItems().addAll(selectedItem.getEntitiesList());

            // Add entities to the execution results choice box
            entityChoiceBox.getItems().addAll(selectedItem.getEntitiesList());
        }
    }


    /**
     * Chooses the last simulation execution in the executions list,
     * shows that simulation execution's details.
     */
    public void chooseLastExecution() {
        executionListTableView.getSelectionModel().select(executionListTableView.getItems().size() - 1);    // select the last added simulation execution
        selectTableItem();  // show its details
    }

    /**
     * When an entity is chosen from the execution results section's entities choice box,
     * Shows all properties of chosen entity in the properties choice box.
     * @param entityDTO the entityDTO which was chosen by the user.
     */
    public void onEntitySelect(EntityDTO entityDTO) {
        if(entityDTO != null) {
            propertyChoiceBox.getItems().clear();
            propertyChoiceBox.getItems().addAll(entityDTO.getPropertyDTOList());
        }
    }


    /**
     * When a property is chosen from the execution results section's properties choice box,
     * Shows various information on it in the Tree View of the execution results.
     * @param propertyDTO the propertyDTO which was chosen by the user.
     */
    public void onPropertySelect(PropertyDTO propertyDTO) {
        if (propertyDTO != null) {
            boolean isNumeric = propertyDTO.getValue() instanceof Float || propertyDTO.getValue() instanceof Integer;
            Float sum = 0f;     // Sum of all property values
            Integer count = 0;  // Counter of how many different property values exist

            TreeItem<String> root = propertyDetailsTreeView.getRoot();
            root.getChildren().clear();  // Clear previously added items

            // For each property value, show the number of instances with said value
            TreeItem<String> histogramItem = new TreeItem<>("Final Population's Property Values");
            for (Map.Entry<Object, Integer> entry : mainController.getPropertyValues(chosenExecutionID, propertyDTO).entrySet()) {
                if (isNumeric) {
                    if ((entry.getKey() instanceof Integer))
                        sum += (Integer) entry.getKey();
                    else
                        sum += (Float) entry.getKey();

                    count++;
                }

                histogramItem.getChildren().add(new TreeItem<>("Value: " + entry.getKey() + ", Amount in population: " + entry.getValue()));
            }

            // Show the consistency of the property's value
            TreeItem<String> consistencyItem = new TreeItem<>("Consistency");
            // todo - calculate consistency of the property

            // If property is numeric, show the average value of the property in the final population
            if (isNumeric) {
                float average = sum / count;

                TreeItem<String> averageValueItem = new TreeItem<>("Average Value in Final Population");
                averageValueItem.getChildren().add(new TreeItem<>(String.valueOf(average)));

                root.getChildren().addAll(histogramItem, consistencyItem, averageValueItem);
            } else {
                root.getChildren().addAll(histogramItem, consistencyItem);
            }
        }
    }
}
