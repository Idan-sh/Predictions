package com.idansh.javafx.controllers;

import com.idansh.dto.action.ActionDTO;
import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariableDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;
import com.idansh.dto.simulation.LoadedSimulationDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * FXML Controller class for the first screen of the application.
 * this screen will be in charge of loading new simulation data from XML files,
 * and in charge of showing the loaded simulation details.
 */
public class DetailsController {
    private final String MAIN_ROOT_NAME = "Main Root", ENTITIES_ROOT_NAME = "Entities", RULES_ROOT_NAME = "Rules",
            TERMINATION_RULES_ROOT_NAME = "Termination Rules", ENVIRONMENT_VARIABLES_ROOT_NAME = "Environment Variables";
    private final String NAME_TITLE = "Name: ", VALUE_TITLE = "Value: ", TYPE_TITLE = "Type: ";
    private final String RANGE_TITLE = "Range", RANGE_FROM_TITLE = "From: ", RANGE_TO_TITLE = "To: ";
    private final String PROPERTY_IS_RANDOM_TITLE = "Is Random: ";

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
     * @param loadedSimulationDTO DTO containing full details of the current loaded simulation.
     */
    public void displayCurrentSimulationDetails(LoadedSimulationDTO loadedSimulationDTO) {
        // Create the main root of the tree view
        TreeItem<Object> mainRoot = new TreeItem<>(MAIN_ROOT_NAME);
        breakdownTreeView.setRoot(mainRoot);

        // Create roots for various simulation details
        TreeItem<Object> entitiesRoot = new TreeItem<>(ENTITIES_ROOT_NAME);
        TreeItem<Object> rulesRoot = new TreeItem<>(RULES_ROOT_NAME);
        TreeItem<Object> terminationRulesRoot = new TreeItem<>(TERMINATION_RULES_ROOT_NAME);
        TreeItem<Object> environmentVariablesRoot = new TreeItem<>(ENVIRONMENT_VARIABLES_ROOT_NAME);

        // Add all entities to the entities root
        loadedSimulationDTO.getEntityDTOList().forEach(
                entityDTO -> entitiesRoot.getChildren().add(new TreeItem<>(entityDTO))
        );

        // Add all rules to the rules root
        loadedSimulationDTO.getRuleDTOList().forEach(
                ruleDTO -> rulesRoot.getChildren().add(new TreeItem<>(ruleDTO))
        );

        // Add all termination rules to the termination rules root
        loadedSimulationDTO.getTerminationRuleDTOList().forEach(
                terminationRuleDTO -> terminationRulesRoot.getChildren().add(new TreeItem<>(terminationRuleDTO))
        );

        loadedSimulationDTO.getEnvironmentVariablesListDTO().getEnvironmentVariableInputDTOs().forEach(
                environmentVariableDTO -> environmentVariablesRoot.getChildren().add(new TreeItem<>(environmentVariableDTO))
        );

        // Add them to the main root
        mainRoot.getChildren().addAll(entitiesRoot, rulesRoot, terminationRulesRoot, environmentVariablesRoot);
    }


    /**
     * Activates when a tree node is pressed (one of the simulation breakdown's items was chosen).
     * Displays the chosen item's full details in the detailsTreeView component.
     */
    public void selectItem() {
        TreeItem<Object> selectedTreeItem = breakdownTreeView.getSelectionModel().getSelectedItem();

        // Check if received TreeItem is a valid item. if so, show its details.
        if(selectedTreeItem != null && !selectedTreeItem.getParent().getValue().equals(MAIN_ROOT_NAME)) {
            try {
                // The parents if all selectable items are of string type, find the type
                switch ((String) selectedTreeItem.getParent().getValue()) {
                    case ENTITIES_ROOT_NAME:
                        addEntityDetails((EntityDTO) selectedTreeItem.getValue());
                        break;

                    case RULES_ROOT_NAME:
                        addRuleDetails((RuleDTO) selectedTreeItem.getValue());
                        break;

                    case TERMINATION_RULES_ROOT_NAME:
                        addTerminationRuleDetails((TerminationRuleDTO) selectedTreeItem.getValue());
                        break;

                    case ENVIRONMENT_VARIABLES_ROOT_NAME:
                        addEnvironmentVariableDetails((EnvironmentVariableDTO) selectedTreeItem.getValue());
                        break;

                    default:
                        break;
                }
            } catch (IllegalArgumentException e) {
                mainController.showErrorAlert("An Error Occurred :(", e.getMessage());
            }
        }
    }


    /**
     * Adds a single environment variable's details to the details tree view.
     * @param environmentVariableDTO DTO of the environment variable to display.
     */
    private void addEnvironmentVariableDetails(EnvironmentVariableDTO environmentVariableDTO) {
        // Create the main root of the tree view
        TreeItem<String> mainRoot = new TreeItem<>(MAIN_ROOT_NAME);
        detailsTreeView.setRoot(mainRoot);

        TreeItem<String> nameItem = new TreeItem<>(NAME_TITLE + environmentVariableDTO.getName());
        TreeItem<String> valueItem = new TreeItem<>(VALUE_TITLE + environmentVariableDTO.getValue());
        TreeItem<String> typeItem = new TreeItem<>(TYPE_TITLE + environmentVariableDTO.getType());

        if(environmentVariableDTO.getRange() != null) {
            TreeItem<String> rangeRoot = new TreeItem<>(RANGE_TITLE);
            TreeItem<String> rangeFromItem = new TreeItem<>(RANGE_FROM_TITLE + environmentVariableDTO.getRange().getFrom());
            TreeItem<String> rangeToItem = new TreeItem<>(RANGE_TO_TITLE + environmentVariableDTO.getRange().getTo());
            rangeRoot.getChildren().addAll(rangeFromItem, rangeToItem);  // Add range details to the range root

            // Add all details of the property to its root, including range
            mainRoot.getChildren().addAll(nameItem, valueItem, typeItem, rangeRoot);
        } else {
            // Add all details of the property to its root, excluding range
            mainRoot.getChildren().addAll(nameItem, valueItem, typeItem);
        }
    }


    /**
     * Adds a single termination rule's details to the details tree view.
     * @param terminationRuleDTO DTO of the termination rule to display.
     */
    private void addTerminationRuleDetails(TerminationRuleDTO terminationRuleDTO) {
        // Create the main root of the tree view
        TreeItem<String> mainRoot = new TreeItem<>(MAIN_ROOT_NAME);
        detailsTreeView.setRoot(mainRoot);

        // Create and add Termination Rule Type
        TreeItem<String> typeItem = new TreeItem<>(TYPE_TITLE + terminationRuleDTO.getType());
        mainRoot.getChildren().add(typeItem);

        // Create and add Termination Rule Value (if exists)
        if(terminationRuleDTO.getValue() != null) {
            TreeItem<String> valueItem = new TreeItem<>(VALUE_TITLE + terminationRuleDTO.getValue());
            mainRoot.getChildren().add(valueItem);
        }
    }


    /**
     * Adds a single rule's details to the details tree view.
     * @param ruleDTO DTO of the rule to display.
     */
    private void addRuleDetails(RuleDTO ruleDTO) {
        String ACTIONS_TITLE = "Actions:";
        String NOF_ACTIONS_TITLE = "Number of Actions: ";
        String PROBABILITY_TITLE = "Probability: ";
        String TICKS_TITLE = "Ticks: ";

        // Create the main root of the tree view
        TreeItem<String> mainRoot = new TreeItem<>(MAIN_ROOT_NAME);
        detailsTreeView.setRoot(mainRoot);

        TreeItem<String> nameItem = new TreeItem<>(NAME_TITLE + ruleDTO.getName());
        TreeItem<String> ticksItem = new TreeItem<>(TICKS_TITLE + ruleDTO.getTicks());
        TreeItem<String> probabilityItem = new TreeItem<>(PROBABILITY_TITLE + ruleDTO.getProbability());
        TreeItem<String> nofActionsItem = new TreeItem<>(NOF_ACTIONS_TITLE + ruleDTO.getNofActions());

        TreeItem<String> actionsRoot = new TreeItem<>(ACTIONS_TITLE);

        // Add each action to the actions root
        for (ActionDTO actionDTO : ruleDTO.getActionDTOList()) {
            addActionDetails(actionsRoot, actionDTO);
        }

        // Add all rule details to the main root
        mainRoot.getChildren().addAll(nameItem, ticksItem, probabilityItem, nofActionsItem, actionsRoot);
    }

    /**
     * Add action details into the main root received,
     * using info from the actionDTO received.
     */
    private void addActionDetails(TreeItem<String> mainRoot, ActionDTO actionDTO) {
        TreeItem<String> actionDetailsRoot = new TreeItem<>(actionDTO.getType());

        TreeItem<String> mainEntityItem = new TreeItem<>("Main Entity Context: " + actionDTO.getMainEntityContext());

        TreeItem<String> secondaryEntityItem = new TreeItem<>("Secondary Entity Context: " + actionDTO.getSecondaryEntityContext());

        TreeItem<String> argumentsRoot = new TreeItem<>("Arguments");
        actionDTO.getArgumentsMap().forEach(
                (argName, argValue) ->
                    argumentsRoot.getChildren().add(new TreeItem<>(argName + ":  " + argValue))
        );

        TreeItem<String> extraInfoRoot = new TreeItem<>("Extra Information");
        actionDTO.getExtraInfoMap().forEach(
                (extraInfoName, extraInfoValue) ->
                        extraInfoRoot.getChildren().add(new TreeItem<>(extraInfoName + ":  " + extraInfoValue))
        );

        // Add items:
        actionDetailsRoot.getChildren().add(mainEntityItem);

        // Check if there is a secondary entity defined in the action
        if (actionDTO.getSecondaryEntityContext() != null)
            actionDetailsRoot.getChildren().add(secondaryEntityItem);

        actionDetailsRoot.getChildren().add(argumentsRoot);

        // Check if extra info was added
        if (!extraInfoRoot.getChildren().isEmpty())
            actionDetailsRoot.getChildren().add(extraInfoRoot);

        mainRoot.getChildren().add(actionDetailsRoot);
    }


    /**
     * Adds a single entity's details to the details tree view.
     * @param entityDTO DTO of the entity to display.
     */
    private void addEntityDetails(EntityDTO entityDTO) {
        String PROPERTIES_TITLE = "Properties";

        // Create the main root of the tree view
        TreeItem<String> mainRoot = new TreeItem<>(MAIN_ROOT_NAME);
        detailsTreeView.setRoot(mainRoot);

        TreeItem<String> nameItem = new TreeItem<>(NAME_TITLE + entityDTO.getName());
        TreeItem<String> propertiesRoot = new TreeItem<>(PROPERTIES_TITLE);

        // Add each property to the properties root
        entityDTO.getPropertyDTOList().forEach(
                propertyDTO -> {
                    // Each property will have its root
                    TreeItem<String> propertyRoot = new TreeItem<>(propertyDTO.getName());

                    // The property's details will be the roots children
                    TreeItem<String> propertyTypeItem = new TreeItem<>(TYPE_TITLE + propertyDTO.getType());
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

                    // Add the finished property to the main root
                    propertiesRoot.getChildren().add(propertyRoot);
                }
        );

        // Add all entity details to the mainRoot
        mainRoot.getChildren().addAll(nameItem, propertiesRoot);
    }


    /**
     * Converts a propertyDTO's value into a TreeItem of type String.
     * @param propertyDTO DTO containing an object value of the type "decimal"/"float"/"boolean"/"string".
     * @throws IllegalArgumentException in case the property DTO received is not of a valid type.
     */
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
