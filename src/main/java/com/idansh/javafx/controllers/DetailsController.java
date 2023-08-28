package com.idansh.javafx.controllers;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class for the first screen of the application.
 * this screen will be in charge of loading new simulation data from XML files,
 * and in charge of showing the loaded simulation details.
 */
public class DetailsController {
    private final String MAIN_ROOT_NAME = "Main Root";
    private final String ENTITIES_ROOT_NAME = "Entities";
    private final String RULES_ROOT_NAME = "Rules";
    private final String TERMINATION_RULES_ROOT_NAME = "Termination Rules";
    private final String ENVIRONMENT_VARIABLES_ROOT_NAME = "Environment Variables";

    private AppController mainController;

    @FXML
    private TreeView<Object> breakdownTreeView;

    @FXML
    private TreeView<String> detailsTreeView;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


    /**
     * Displays on the screen the current simulation's breakdown by a tree view.
     * Each DTO class (environment, entity, etc.) in the simulation result will be shown as a TreeItem,
     * which can be chosen to view more information about the selected item.
     *
     * @param currentSimulationDTO DTO containing full details of the current loaded simulation.
     */
    public void displayCurrentSimulationDetails(CurrentSimulationDTO currentSimulationDTO) {
        // Create the main root of the tree view
        TreeItem<Object> mainRoot = new TreeItem<>(MAIN_ROOT_NAME);
        breakdownTreeView.setRoot(mainRoot);

        // Create roots for various simulation details
        TreeItem<Object> entitiesRoot = new TreeItem<>(ENTITIES_ROOT_NAME);
        TreeItem<Object> rulesRoot = new TreeItem<>(RULES_ROOT_NAME);
        TreeItem<Object> terminationRulesRoot = new TreeItem<>(TERMINATION_RULES_ROOT_NAME);
        TreeItem<Object> environmentVariablesRoot = new TreeItem<>(ENVIRONMENT_VARIABLES_ROOT_NAME);

        // Add all entities to the entities root
        currentSimulationDTO.getEntityDTOList().forEach(
                entityDTO -> entitiesRoot.getChildren().add(new TreeItem<>(entityDTO))
        );

        // Add all rules to the rules root
        currentSimulationDTO.getRuleDTOList().forEach(
                ruleDTO -> rulesRoot.getChildren().add(new TreeItem<>(ruleDTO))
        );

        // Add all termination rules to the termination rules root
        currentSimulationDTO.getTerminationRuleDTOList().forEach(
                terminationRuleDTO -> terminationRulesRoot.getChildren().add(new TreeItem<>(terminationRuleDTO))
        );

        currentSimulationDTO.getEnvironmentVariablesListDTO().getEnvironmentVariableInputDTOs().forEach(
                environmentVariableDTO -> environmentVariablesRoot.getChildren().add(new TreeItem<>(environmentVariableDTO))
        );

        // Add them to the main root
        mainRoot.getChildren().addAll(entitiesRoot, rulesRoot, terminationRulesRoot, environmentVariablesRoot);
    }


    /**
     * Activates when a tree node is pressed (one of the simulation breakdown's items was chosen).
     */
    public void selectItem() {
        TreeItem<Object> selectedTreeItem = breakdownTreeView.getSelectionModel().getSelectedItem();

        // Check if received TreeItem is a valid item. if so, show its details.
        if(selectedTreeItem != null && !selectedTreeItem.getParent().getValue().equals(MAIN_ROOT_NAME)) {
            // The parents if all selectable items are of string type, find the type
            switch((String) selectedTreeItem.getParent().getValue()) {
                case ENTITIES_ROOT_NAME:
                    addEntityDetails((EntityDTO) selectedTreeItem.getValue());
                    break;

                case RULES_ROOT_NAME:
                    break;

                case TERMINATION_RULES_ROOT_NAME:
                    break;

                case ENVIRONMENT_VARIABLES_ROOT_NAME:
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * Adds a single entity's details to the details tree view.
     * @param entityDTO DTO of the entity to display.
     */
    private void addEntityDetails(EntityDTO entityDTO) {
        String PROPERTIES_TITLE = "Properties";
        String RANGE_TITLE = "Range", RANGE_FROM_TITLE = "From: ", RANGE_TO_TITLE = "To: ";
        String NAME_TITLE = "Name: ", INIT_AMOUNT_TITLE = "Initial amount in population: ";
        String PROPERTY_TYPE_TITLE = "Type: ", PROPERTY_IS_RANDOM_TITLE = "Is Random: ";

        // Create the main root of the tree view
        TreeItem<String> mainRoot = new TreeItem<>(MAIN_ROOT_NAME);
        detailsTreeView.setRoot(mainRoot);

        TreeItem<String> nameItem = new TreeItem<>(NAME_TITLE + entityDTO.getName());
        TreeItem<String> initAmountItem = new TreeItem<>(INIT_AMOUNT_TITLE + Integer.toString(entityDTO.getInitAmountInPopulation()));
        TreeItem<String> propertiesRoot = new TreeItem<>(PROPERTIES_TITLE);

        entityDTO.getPropertyDTOList().forEach(
                propertyDTO -> {
                    // Each property will have its root
                    TreeItem<String> propertyRoot = new TreeItem<>(propertyDTO.getName());

                    // The property's details will be the roots children
                    TreeItem<String> propertyTypeItem = new TreeItem<>(PROPERTY_TYPE_TITLE + propertyDTO.getType());
                    TreeItem<String> propertyValueItem = getPropertyValueTreeItem(propertyDTO);
                    TreeItem<String> propertyIsRandomItem = new TreeItem<>(PROPERTY_IS_RANDOM_TITLE + propertyDTO.isRandomGenerated());

                    if(propertyDTO.getRangeDTO() != null) {
                        TreeItem<String> propertyRangeRoot = new TreeItem<>(RANGE_TITLE);
                        TreeItem<String> rangeFromItem = new TreeItem<>(RANGE_FROM_TITLE + propertyDTO.getRangeDTO().getFrom());
                        TreeItem<String> rangeToItem = new TreeItem<>(RANGE_TO_TITLE + propertyDTO.getRangeDTO().getTo());
                        propertyRangeRoot.getChildren().addAll(rangeFromItem, rangeToItem);  // Add range details to the range root

                        // Add all details of the property to its root, including range
                        propertyRoot.getChildren().addAll(propertyTypeItem, propertyRangeRoot, propertyValueItem, propertyIsRandomItem);
                    } else {
                        // Add all details of the property to its root, excluding range
                        propertyRoot.getChildren().addAll(propertyTypeItem, propertyValueItem, propertyIsRandomItem);
                    }

                    // Add the finished property to the properties root
                    propertiesRoot.getChildren().add(propertyRoot);
                }
        );

        // Add all entity details to the mainRoot
        mainRoot.getChildren().addAll(nameItem, initAmountItem, propertiesRoot);
    }


    private TreeItem<String> getPropertyValueTreeItem(PropertyDTO propertyDTO) {
        String VALUE_TITLE = "Value: ";

        // If the property is randomly assigned, then the value will be assigned on simulation start
        if(propertyDTO.isRandomGenerated())
            return new TreeItem<>(VALUE_TITLE + "not assigned");

        switch (propertyDTO.getType()) {
            case "decimal":
                return new TreeItem<>(VALUE_TITLE + (int) propertyDTO.getValue());

            case "float":
                return new TreeItem<>(VALUE_TITLE + (float) propertyDTO.getValue());

            case "boolean":
                return new TreeItem<>(VALUE_TITLE + (boolean) propertyDTO.getValue());

            case "string":
                return new TreeItem<>(VALUE_TITLE + (String) propertyDTO.getValue());

            default:
                throw new IllegalArgumentException("Invalid property type received in getPropertyValueTreeItem");
        }
    }
}
