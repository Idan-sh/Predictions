package com.idansh.javafx.controllers;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.simulation.RunningSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.javafx.helpers.ResultsTableItem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * FXML Controller class for the third screen of the application.
 * this screen will be in charge of showing the result of previous ran simulations.
 */
public class ResultsController implements Initializable {
    /**
     * a Thread that when being run will go through every currently running execution,
     * and update its data by pulling it from the engine.
     * After all currently running executions are finished, the thread will die,
     * so every time a new execution is added into the system, it will be checked if the thread is running,
     * and if not - a new one will be created.
     */
    private class UpdaterThread extends Thread {
        @Override
        public void run() {
            try {
                while (!runningExecutionsIdSet.isEmpty()) {
                    Iterator<Integer> idIterator = runningExecutionsIdSet.iterator();
                    while (idIterator.hasNext()) {
                        int id = idIterator.next();                                     // Get the ID of the simulation execution
                        if (updateExecution(id)) idIterator.remove();                   // Update the simulation execution with the ID, if the simulation has ended, removes it from the runningExecutionsIdSet

                        Platform.runLater(ResultsController.this::selectTableItem);     // Tell the JAT to show the updated chosen execution info
                    }
                    sleep(200);     // Pause updating
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final String COMPLETED = "Completed", IN_PROGRESS = "In Progress";

    private AppController mainController;
    private Set<Integer> runningExecutionsIdSet;
    private Map<Integer, ResultsTableItem> executionsPool;
    private Thread updaterThread;

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
    @FXML
    private HBox simulationConrolHBox;
    @FXML
    private GridPane executionDetailsGridPane;

    // Execution Results Components:
    @FXML
    private ChoiceBox<EntityDTO> entityChoiceBox;
    @FXML
    private ChoiceBox<PropertyDTO> propertyChoiceBox;
    @FXML
    private TreeView<String> propertyDetailsTreeView;
    @FXML
    private HBox executionResultsHBox;

    private int chosenExecutionID;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup Execution List TableView:
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        startTableColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTableColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        statusTableColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Setup Entity Amounts TableView:
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("currAmountInPopulation"));

        // Setup on action events for entity and property choice boxes:
        entityChoiceBox.setOnAction(event -> onEntitySelect(entityChoiceBox.getValue()));
        propertyChoiceBox.setOnAction(event -> onPropertySelect(propertyChoiceBox.getValue()));

        // Setup Execution Results tree view:
        propertyDetailsTreeView.setRoot(new TreeItem<>("root-item"));

        // Setup running executions data:
        executionsPool = new HashMap<>();
        runningExecutionsIdSet = new HashSet<>();
        updaterThread = new UpdaterThread();        // Will update the running executions' info
    }


    /**
     * Adds a newly created simulation execution to a table view of all the current and previous simulation executions.
     * Displays the ID, End time, and status of the simulation execution.
     * @param chosenExecutionID ID of the simulation execution to add to the UI.
     * @throws IllegalArgumentException when ID received is not of a running/finished simulation execution.
     */
    public void addExecution(int chosenExecutionID) {
        // Get info on the execution, the execution should be of the type SimulationResultDTO or CurrentSimulationDTO
        Object execution = mainController.getSimulationExecutionDTO(chosenExecutionID);

        // Newly created execution is still running
        if (execution instanceof RunningSimulationDTO) {
            RunningSimulationDTO runningSimulation = (RunningSimulationDTO) execution;
            ResultsTableItem resultsTableItem = new ResultsTableItem(
                    runningSimulation.getId(),
                    runningSimulation.getEntityDTOList(),
                    runningSimulation.getSimulationTime(),
                    IN_PROGRESS,
                    runningSimulation.getCompletedTicks(),
                    runningSimulation.getMaxTicks()
            );
            executionListTableView.getItems().add(resultsTableItem);
            executionsPool.put(runningSimulation.getId(), resultsTableItem);
            runningExecutionsIdSet.add(runningSimulation.getId());
        } else throw new IllegalArgumentException("Invalid execution type received: \""
                + execution.getClass()
                + "\", only accepts executions of type SimulationResultDTO or CurrentSimulationDTO");

        // Check if there is an updater already running, if not then create and start one
        if(!updaterThread.isAlive()) {
            updaterThread = new UpdaterThread();
            updaterThread.start();
        }
    }


    /**
     * Update info of a simulation execution in the Executions List.
     * @param chosenExecutionID ID of the simulation to update.
     * @return true if the chosen execution has finished, false otherwise.
     */
    public boolean updateExecution(int chosenExecutionID) {
        ResultsTableItem resultsTableItem = executionsPool.get(chosenExecutionID);

        if(resultsTableItem == null)
            throw new IllegalArgumentException("Cannot update simulation execution with ID " + chosenExecutionID + ", simulation execution with this ID does not exist.");

        // Get updated info on the received execution, the new execution data should be of the type SimulationResultDTO or CurrentSimulationDTO
        Object execution = mainController.getSimulationExecutionDTO(chosenExecutionID);

        // Check if the execution is still running or if it has finished
        if (execution instanceof SimulationResultDTO) {
            SimulationResultDTO simulationResult = (SimulationResultDTO) execution;
            resultsTableItem.update(
                    simulationResult.getSimulationTime(),
                    COMPLETED,
                    simulationResult.getCompletedTicks(),
                    simulationResult.getEntityDTOList()
            );

            // Tell JAT to output popup alert that this simulation execution has finished
            Platform.runLater(() ->
                mainController.showInformationAlert(
                        "Simulation Completed Successfully",
                        "Completed Simulation ID: " + chosenExecutionID +
                                "\nSimulation End Reason: " + simulationResult.getEndReason()
                )
            );

            return true;    // This simulation execution has finished, return true
        } else if (execution instanceof RunningSimulationDTO) {
            RunningSimulationDTO simulationResult = (RunningSimulationDTO) execution;
            resultsTableItem.update(
                    simulationResult.getSimulationTime(),
                    IN_PROGRESS,
                    simulationResult.getCompletedTicks(),
                    simulationResult.getEntityDTOList()
            );

            return false;   // This simulation execution is still running, return false
        } else throw new IllegalArgumentException("Invalid execution type received: \""
                + execution.getClass()
                + "\", only accepts executions of type SimulationResultDTO or CurrentSimulationDTO");
    }


    /**
     * Activates on user simulation execution item click.
     * Shows more information about the chosen execution -
     *      shows how many ticks were completed,
     *      and how much time had passed since the beginning of the simulation (in seconds).
     */
    public void selectTableItem() {
        BiFunction<Integer, Integer, String> ticksFormatterWithMax =
                (completedTicks, maxTicks) -> String.format("Ticks Reached: %d/%d", completedTicks, maxTicks);
        Function<Integer, String> ticksFormatterWithoutMax =
                (completedTicks) -> String.format("Ticks Reached: %d", completedTicks);
        Function<Float, String> timerFormatter = (timePassed) -> String.format("Time Passed: %.3f Seconds", timePassed);

        ResultsTableItem selectedItem = executionListTableView.getSelectionModel().getSelectedItem();
        executionDetailsGridPane.setDisable(selectedItem == null);  // Disable details section if no execution was chosen

        if (selectedItem != null) {
            simulationConrolHBox.setDisable(selectedItem.getStatus().equals(COMPLETED));    // Disable the simulation control buttons if the execution chosen has finished, otherwise enable them
            executionResultsHBox.setDisable(selectedItem.getStatus().equals(IN_PROGRESS));  // Disable the simulation results section if the execution chosen is still in progress, otherwise enable it

            chosenExecutionID = selectedItem.getId();

            // Remove previously shown items
            progressListView.getItems().clear();
            entityAmountsTableView.getItems().clear();
            entityChoiceBox.getItems().clear();
            propertyChoiceBox.getItems().clear();
            propertyDetailsTreeView.getRoot().getChildren().clear();

            // Add completed ticks and time passed to the list view
            if(selectedItem.getMaxTicks() != null) {
                progressListView.getItems().addAll(
                        ticksFormatterWithMax.apply(selectedItem.getCompletedTicks(), selectedItem.getMaxTicks()),
                        timerFormatter.apply(selectedItem.getSimulationTime().getSecondsPassed())
                );
            } else {
                progressListView.getItems().addAll(
                        ticksFormatterWithoutMax.apply(selectedItem.getCompletedTicks()),
                        timerFormatter.apply(selectedItem.getSimulationTime().getSecondsPassed())
                );
            }


            // Add entity amounts to the table
            entityAmountsTableView.getItems().addAll(selectedItem.getEntityDTOList());

            // Add entities to the execution results choice box
            entityChoiceBox.getItems().addAll(selectedItem.getEntityDTOList());
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
            boolean isNumeric = propertyDTO.getType().equals("float") || propertyDTO.getType().equals("decimal");

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
