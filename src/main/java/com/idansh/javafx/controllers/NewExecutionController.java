package com.idansh.javafx.controllers;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariableDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.javafx.helpers.TextFormatterHelper;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FXML Controller class for the second screen of the application.
 * this screen will be in charge of the running process of the simulation.
 */
public class NewExecutionController {
    private AppController mainController;

    @FXML
    private Button clearButton;
    @FXML
    private Button startButton;

    // Entities amounts section
    @FXML
    private Label entitiesTitleLabel;
    @FXML
    private VBox entitiesVBox;
    private Map<EntityDTO, TextField> entityDTOTextFieldMap;

    // Environment variables' values section
    @FXML
    private Label environmentTitleLabel;
    @FXML
    private VBox environmentVBox;
    private Map<EnvironmentVariableDTO, TextField> environmentVariableDTOTextFieldMap;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    /**
     * Validates user input, if user input is valid then starts
     * a new simulation from the loaded file with the variables received.
     */
    public void startButtonListener() {
        if(!mainController.isSimulationLoaded()){
            mainController.showErrorAlert("Please load a simulation before trying to run!");
            return;
        }

        // Check if the input in valid, if not show error message
        try {
            getEntitiesInput();
            getEnvironmentVariablesInput();
        } catch (IllegalArgumentException e) {
            mainController.showErrorAlert(e.getMessage());
            return;
        }

        // Run the current loaded simulation
        mainController.runCurrentLoadedSimulation();

        // Move to the Results tab to see the simulation in progress
        mainController.moveToResultsTab();
    }


    /**
     * Checks if all entity text fields were filled,
     * and if the total amount of entities entered are within the correct range.
     */
    private void getEntitiesInput() {
        AtomicInteger totalAmount = new AtomicInteger();
        entityDTOTextFieldMap.forEach(
                (entityDTO, textField) -> {
                    // Check if no input was entered
                    if(textField.getText().isEmpty())
                        throw new IllegalArgumentException("no amount of entity instances entered for entity \""
                                + entityDTO.getName() + "\"");

                    int amountInput = Integer.parseInt(textField.getText()); // todo - set this amount in the current simulation

                    totalAmount.addAndGet(amountInput);
                }
        );

        int maxNofEntities = mainController.getMaxNofEntities();
        if(totalAmount.get() > maxNofEntities)
            throw new IllegalArgumentException("there can be only " + maxNofEntities
                    + " entities in the simulation. Please lower the number of entities...");
    }


    /**
     * Checks if all environment variables' text fields were filled,
     * and if each one is of the correct type..
     */
    private void getEnvironmentVariablesInput() {
        environmentVariableDTOTextFieldMap.forEach(
                (environmentVariableDTO, textField) -> {
                    String input = textField.getText();
                    if(input.isEmpty())
                        throw new IllegalArgumentException("no environment variable value entered for environment variable \""
                                + environmentVariableDTO.getName() + "\"");

                    // Try to parse into the correct type and update the environment variable, show error if failed.
                    try {
                        switch (environmentVariableDTO.getType()) {
                            case "decimal":
                                environmentVariableDTO.setValue(Integer.parseInt(input));
                                break;

                            case "float":
                                environmentVariableDTO.setValue(Float.parseFloat(input));
                                break;

                            case "boolean":
                                environmentVariableDTO.setValue(convertStringToBoolean(input));
                                break;

                            case "string":
                                environmentVariableDTO.setValue(input);
                                break;

                            default:
                                throw new IllegalArgumentException("invalid environment variable type "
                                        + environmentVariableDTO.getType()
                                        + " received, cannot display the environment variable of name "
                                        + environmentVariableDTO.getName());
                        }
                    } catch (RuntimeException e) {
                        throw new IllegalArgumentException("entered input invalid, not of correct type \""
                                + environmentVariableDTO.getType() + "\"");
                    }
                }
        );
    }


    /**
     * Activates when the Clear button is clicked.
     * Clears the user input and resets to the previous environment variables' values.
     */
    public void clearButtonListener() {
        if(!mainController.isSimulationLoaded()){
            mainController.showErrorAlert("Please load a simulation before trying to clear!");
            return;
        }

        entityDTOTextFieldMap.forEach(
                (entity, textField) -> textField.clear()
        );

        environmentVariableDTOTextFieldMap.forEach(
                (environmentVariable, textField) -> textField.setText(environmentVariable.getValue().toString())
        );
    }

    public void displayDetails(CurrentSimulationDTO currentSimulationDTO) {
        entityDTOTextFieldMap = new HashMap<>();
        environmentVariableDTOTextFieldMap = new HashMap<>();

        // Show titles for entity amounts and environment variables' values input
        entitiesTitleLabel.setVisible(true);
        environmentTitleLabel.setVisible(true);

        // Create text fields for entity amounts input
        currentSimulationDTO.getEntityDTOList().forEach(this::displayEntity);

        currentSimulationDTO.getEnvironmentVariablesListDTO().getEnvironmentVariableInputDTOs().forEach(this::displayEnvironmentVariable);
    }


    /**
     * Displays a pairs of label (name of the entity) and a field box (amount of this entity in the population)
     * to receive user input on how many instances of this entity will be instantiated.
     *
     * @param entityDTO an entity to receive its amount in the population.
     */
    private void displayEntity(EntityDTO entityDTO) {
        String entityName = entityDTO.getName();

        // Create the label and text field for the environment variable
        Label label = createLabel(entityName);
        TextField textField = createTextField(entityName);

        // Set up a TextFormatter to accept only certain characters as input
        textField.setTextFormatter(TextFormatterHelper.getIntTextFormatter());

        // Create and add the UI component into the appropriate component
        entitiesVBox.getChildren().add(createHBox(label, textField));
        entityDTOTextFieldMap.put(entityDTO, textField);
    }


    /**
     * Displays pairs of label (name of the environment variable) and a field box (the current value of the environment variable)
     * to receive user input if a certain value is wanted.
     *
     * @param environmentVariableDTO an environment variable to change or keep its value.
     */
    private void displayEnvironmentVariable(EnvironmentVariableDTO environmentVariableDTO) {
        String environmentVariableName = environmentVariableDTO.getName();

        // Create the label and text field for the environment variable
        Label label = createLabel(environmentVariableName);
        TextField textField = createTextField(environmentVariableName);
        textField.setText(environmentVariableDTO.getValue().toString());

        // Set up a TextFormatter to accept only certain characters as input, according to the environment variable's type
        switch (environmentVariableDTO.getType()) {
            case "decimal":
                textField.setTextFormatter(TextFormatterHelper.getIntTextFormatter());
                break;

            case "float":
                textField.setTextFormatter(TextFormatterHelper.getFloatTextFormatter());
                break;

            case "boolean":
                textField.setTextFormatter(TextFormatterHelper.getLettersTextFormatter());
                break;

            case "string":
                textField.setTextFormatter(TextFormatterHelper.getStringTextFormatter());
                break;

            default:
                throw new IllegalArgumentException("invalid environment variable type "
                        + environmentVariableDTO.getType()
                        + " received, cannot display the environment variable of name "
                        + environmentVariableDTO.getName());
        }

        // Create and add the UI component into the appropriate component
        environmentVBox.getChildren().add(createHBox(label, textField));
        environmentVariableDTOTextFieldMap.put(environmentVariableDTO, textField);
    }


    /**
     * Creates a new HBox with a label (a name) and a text field (for user input)
     */
    private HBox createHBox(Label label, TextField textField) {
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(label, textField);
        hbox.setAlignment(Pos.CENTER_LEFT);

        return hbox;
    }


    /**
     * Creates a new label with the given name.
     */
    private Label createLabel(String name) {
        Label label = new Label(name + ":");
        label.setMinWidth(Region.USE_PREF_SIZE);
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);

        return label;
    }


    /**
     * Creates a new text field with the given name as its ID.
     */
    private TextField createTextField(String name) {
        TextField textField = new TextField();
        textField.setId(name);   // ID of the text field will be of the entity's name, which is unique
        HBox.setHgrow(textField, Priority.ALWAYS);

        return textField;
    }


    /**
     * Tries to convert a string to a boolean.
     * if fails throws exception
     * @throws IllegalArgumentException if user failed to enter a valid boolean- "true"/"false".
     */
    public boolean convertStringToBoolean(String input) {
        if(input.equalsIgnoreCase("true"))
            return true;
        else
        if (input.equalsIgnoreCase("false"))
            return false;
        else throw new IllegalArgumentException("invalid boolean value received! enter true/false!");
    }
}

