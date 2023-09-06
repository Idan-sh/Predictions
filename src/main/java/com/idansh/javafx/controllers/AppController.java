package com.idansh.javafx.controllers;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.simulation.LoadedSimulationDTO;
import com.idansh.javafx.manager.SimulationManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Main FXML Controller class of the application.
 * In charge of the header and switching between the subcomponents.
 */
public class AppController implements Initializable {
    /*
     Subcomponents' controllers and references, this controller will automatically know the child components,
     but child components will need to be set up manually.
     The controller of each of the component must be in the format of: "[componentName]Controller"
     */

    // First window - Details
    @FXML
    private HBox detailsComponent;
    @FXML
    private DetailsController detailsComponentController;
    @FXML
    private Tab detailsTab;

    // Second window - New Execution
    @FXML
    private GridPane newExecutionComponent;
    @FXML
    private NewExecutionController newExecutionComponentController;
    @FXML
    private Tab newExecutionTab;


    // Third window - Results
    @FXML
    private HBox resultsComponent;
    @FXML
    private ResultsController resultsComponentController;
    @FXML
    private Tab resultsTab;


    // Main Screen Elements:
    @FXML
    private Button loadFileButton;
    @FXML
    private TextField simulationPathTextField;
    @FXML
    private TabPane appTabPane;


    // Simulation manager object that handles the simulation itself
    private SimulationManager simulationManager;
    private LoadedSimulationDTO loadedSimulationDTO;
    private boolean isSimulationLoaded = false;

    /**
     * Initializes the controller class,
     * while setting references to this main controller in the subcomponents' controllers.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set references from subcomponents' controllers to the main controller (this controller)
        if(detailsComponentController != null && newExecutionComponentController != null && resultsComponentController != null) {
            detailsComponentController.setMainController(this);
            newExecutionComponentController.setMainController(this);
            resultsComponentController.setMainController(this);
            simulationManager = new SimulationManager();
        }
    }


    /**
     * Runs the current loaded simulation.
     */
    public void runCurrentLoadedSimulation() {
        try{
            int finishedSimulationID = simulationManager.runSimulation(loadedSimulationDTO.getEnvironmentVariablesListDTO());
            showInformationAlert(
                    "Simulation Completed Successfully",
                    "completed simulation ID: " + finishedSimulationID
            );
        } catch (RuntimeException e) {
            e.printStackTrace();
            showErrorAlert("Simulation Stopped!", e.getMessage());
        }
    }


    /**
     * Opens file chooser dialog, which allows the user
     * to choose and upload an XML file into the program.
     */
    @FXML
    public void loadFileButtonListener() {
        FileChooser fileChooser = new FileChooser();

        // Let the user choose only XML files.
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );

        File selectedFile = fileChooser.showOpenDialog(loadFileButton.getScene().getWindow());

        // Check if the user canceled and chose no file
        if(selectedFile == null) {
            return;
        }

        // Try to load the simulation from the received file
        try {
            simulationManager.loadSimulationFromFile(selectedFile);
            showInformationAlert(
                    "Successfully Loaded Simulation",
                    "Showing details of the loaded simulation...\n" +
                            "To run the loaded simulation, go to the \"New Execution\" tab."
            );
            isSimulationLoaded = true;

            simulationPathTextField.setText(selectedFile.getPath());    // Set the file's path into the TextField

            // Get the current simulation's details, save it for variable updating, and output its details to the screen
            loadedSimulationDTO = simulationManager.getLoadedSimulationDetails();
            detailsComponentController.displayCurrentSimulationDetails(loadedSimulationDTO);   // Display the current loaded simulation's details
            newExecutionComponentController.displayDetails(loadedSimulationDTO);               // Display the various variables that the user can interact with
            moveToDetailsTab();
        }  catch (RuntimeException e) {
            showErrorAlert("Simulation Load Failed!", e.getMessage());
        }
    }


    /**
     * Returns whether a simulation was loaded into the program
     * from an XML file.
     */
    public boolean isSimulationLoaded() {
        return isSimulationLoaded;
    }


    /**
     * Chooses and shows the Details tab.
     */
    public void moveToDetailsTab() {
        appTabPane.getSelectionModel().select(detailsTab);
    }


    /**
     * Chooses and shows the Results tab.
     */
    public void moveToResultsTab() {
        appTabPane.getSelectionModel().select(resultsTab);
    }


    public int getMaxNofEntities() {
        return Integer.MAX_VALUE; // todo - change to the max number received from new format of XML files.
//        return simulationManager.getCurrentSimulationDetails();
    }


    /**
     * Displays an error alert (popup), with a given error message.
     * @param errorMessage an error message to display to inform the user on what error occurred.
     */
    public void showErrorAlert(String errorTitle, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Predictions - Error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorMessage);
        alert.show();
    }


    /**
     * Displays an information alert (popup), with a given information message.
     * @param infoMessage an information message to display to inform the user on an even that happened.
     */
    public void showInformationAlert(String title, String infoMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Predictions - Information");
        alert.setHeaderText(title);
        alert.setContentText(infoMessage);
        alert.show();
    }


    /**
     * Get a list of all simulation executions, past and present.
     * @return Executions of the type SimulationResultDTO or CurrentSimulationDTO.
     */
    public List<Object> getSimulationExecutions() {
        // Create executions list and add all past executions to it
        List<Object> executionsList = new ArrayList<>(simulationManager.getPastSimulationsResults());

        // todo- add running executions, after threads is done

        return executionsList;
    }


    /**
     * Show the executions screen in the Results tab.
     */
    public void showExecutions() {
        resultsComponentController.showExecutions();
    }


    /**
     *  Send the received entity amount input to the engine to be saved for the simulation run process.
     */
    public void setEntityAmount(String entityName, int amount) {
        simulationManager.setEntityAmount(entityName, amount);
    }

    /**
     * Gets from the simulation manager a map of:
     * 1. key: the property's value
     * 2. value: the amount of entities in the population with this value
     */
    public Map<Object, Integer> getPropertyValues(int simulationResultID, PropertyDTO propertyDTO) {
        return simulationManager.getPropertyValues(simulationResultID, propertyDTO);
    }
}
