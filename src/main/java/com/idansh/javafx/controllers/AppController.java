package com.idansh.javafx.controllers;

import com.idansh.dto.environment.EnvironmentVariableDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.javafx.display.ConsoleOut;
import com.idansh.javafx.manager.SimulationManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
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

    // Second window - New Execution
    @FXML
    private GridPane newExecutionComponent;
    @FXML
    private NewExecutionController newExecutionComponentController;


    // Third window - Results
    @FXML
    private ScrollPane resultsComponent;
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
    private CurrentSimulationDTO currentSimulationDTO;
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
            simulationManager.runSimulation(currentSimulationDTO.getEnvironmentVariablesListDTO());
        } catch (RuntimeException e) {
            showErrorAlert("Simulation Stopped.\nReason: " + e.getMessage());
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
            showInformationAlert("Successfully loaded simulation");
            isSimulationLoaded = true;

            simulationPathTextField.setText(selectedFile.getPath());    // Set the file's path into the TextField

            // Get the current simulation's details, save it for variable updating, and output its details to the screen
            currentSimulationDTO = simulationManager.getCurrentSimulationDetails();
            detailsComponentController.displayCurrentSimulationDetails(currentSimulationDTO);   // Display the current simulation's details
            newExecutionComponentController.displayDetails(currentSimulationDTO);               // Display the various variables that the user can interact with
        }  catch (RuntimeException e) {
            showErrorAlert("Simulation load failed...\nReason:  " + e.getMessage());
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
    public void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Predictions - Error");
        alert.setHeaderText("Error  :(");
        alert.setContentText(errorMessage);
        alert.show();
    }


    /**
     * Displays an information alert (popup), with a given information message.
     * @param infoMessage an information message to display to inform the user on an even that happened.
     */
    public void showInformationAlert(String infoMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Predictions - Information");
        alert.setHeaderText("Message");
        alert.setContentText(infoMessage);
        alert.show();
    }
}
