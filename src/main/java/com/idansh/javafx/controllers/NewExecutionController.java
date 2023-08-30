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

    @FXML
    private VBox entitiesVBox;
    private Map<EntityDTO, TextField> entityDTOTextFieldMap;

    @FXML
    private VBox environmentVBox;
    private Map<EnvironmentVariableDTO, TextField> environmentVariableDTOTextFieldMap;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    /**
     * Activates when the Clear button is clicked.
     * Clears the user input and resets to the previous environment variables' values.
     */
    public void clearButtonListener() {
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

        // Create text fields for entity amounts input
        currentSimulationDTO.getEntityDTOList().forEach(this::displayEntity);
        // todo - on start pressed, check if all text fields are filled, and if the total amount is correct. if so then save the input.

        currentSimulationDTO.getEnvironmentVariablesListDTO().getEnvironmentVariableInputDTOs().forEach(this::displayEnvironmentVariable);
        // todo- validate input.
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
}

