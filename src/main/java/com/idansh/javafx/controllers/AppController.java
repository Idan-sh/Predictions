package com.idansh.javafx.controllers;

import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.javafx.manager.SimulationManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
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
    @FXML private ScrollPane detailsComponent;
    @FXML private DetailsController detailsComponentController;

    // Second window - New Execution
    @FXML private ScrollPane newExecutionComponent;
    @FXML private NewExecutionController newExecutionComponentController;


    // Third window - Results
    @FXML private ScrollPane resultsComponent;
    @FXML private ResultsController resultsComponentController;


    // Main Screen Elements:
    @FXML private Button loadFileButton;
    @FXML private TextField simulationPathTextField;


    // Simulation manager object that handles the simulation itself
    private SimulationManager simulationManager;


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
        simulationManager.loadSimulationFromFile(selectedFile);
        simulationPathTextField.setText(selectedFile.getPath());

        CurrentSimulationDTO currentSimulationDTO = simulationManager.getCurrentSimulationDetails();
        // todo- display current simulation's details in the UI
    }
}
