package com.idansh.ui.display;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariableDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.dto.simulation.SimulationEndTDO;
import com.idansh.dto.simulation.SimulationResultDTO;

import java.util.*;

/**
 * Prints relevant menus and text to the console for the user.
 */
public abstract class ConsoleOut {
    /**
     * Print the main menu of the program.
     */
    public static void printMenu() {
        printTitle("Main Menu");
        System.out.println("1. Load simulation from XML file");
        System.out.println("2. Show loaded simulation details");
        System.out.println("3. Run loaded simulation");
        System.out.println("4. Show details of a past simulation run");
        System.out.println("5. Exit");
        System.out.print("Please enter the number of command to run: ");
    }

    public static void printPastSimulationsMenu() {
        printTitle("Past Simulations");
        System.out.println("Choose how to show the past simulation result:");
        System.out.println("1. Number of entities of each type");
        System.out.println("2. Entity's property value");
        System.out.println("3. Return to main menu");
        System.out.print("Please enter the number of command to run: ");
    }

    /**
     * Print a title in the format:
     * ####### TITLE #######
     * @param text string to print in the title.
     */
    public static void printTitle(String text) {
        System.out.println("\n####### " + text.toUpperCase() + " #######");
    }

    /**
     * Prints an error message to the console.
     * @param text the error message to print.
     */
    public static void printError(String text) {
        System.out.println();
        System.out.println("Error: " + text);
    }


    /**
     * Prints message when exiting the program.
     */
    public static void printGoodbye() {
        System.out.println("Exiting the program, Goodbye...");
    }

    /**
     * Receives a CurrentSimulationDTO with the data of the current simulation,
     * formats it and prints it.
     * @param currentSimulationDTO the current loaded simulation in the engine to print its details.
     */
    public static void printCurrentSimulationDetails(CurrentSimulationDTO currentSimulationDTO) {
        printTitle("simulation details");
        currentSimulationDTO.getEntityDTOList().forEach(ConsoleOut::printFullDetailEntityDTO);                      // Print entities' details
        System.out.println("Rules: ");
        currentSimulationDTO.getRuleDTOList().forEach(ConsoleOut::printFullDetailRuleDTO);                          // Print rules' details
        System.out.println();
        System.out.println("Termination Rules: ");
        currentSimulationDTO.getTerminationRuleDTOList().forEach(ConsoleOut::printFullDetailTerminationRuleDTO);    // Print termination rules' details
    }


    /**
     * Prints out the basic details of past simulation received from the engine.
     * @param pastSimulationsResults - an array of SimulationResultDTO containing simulations results.
     */
    public static void printPastSimulationsShortDetails(List<SimulationResultDTO> pastSimulationsResults) {
        // Sort the simulations results by their date time
        pastSimulationsResults.sort(Comparator.comparing(SimulationResultDTO::getDateTime));

        printTitle("past simulations results");
        System.out.printf("%-22s%-22s%-22s\n", "No.", "Simulation ID", "Date & Time");

        int counter = 1;
        for (SimulationResultDTO r : pastSimulationsResults) {
            System.out.printf("#%-21s%-22s%-22s\n", counter++, r.getId(), r.getDateTimeString());
        }
    }


    /**
     * Prints the initial and final numbers of entities in the population of the given entity.
     * @param entityDTO TDO containing information of the entity to display its amount in the population.
     */
    public static void printNofEntityDTO(EntityDTO entityDTO) {
        ConsoleOut.printTitle("Entities Instances Amounts");
        ConsoleOut.printMessage(entityDTO.getName() + ": initial amount- " + entityDTO.getInitAmountInPopulation() + ", final amount- " + entityDTO.getCurrAmountInPopulation());
    }

    /**
     * Prints the full detail of an entity.
     * @param entityDTO TDO containing information of the entity to display.
     */
    public static void printFullDetailEntityDTO(EntityDTO entityDTO) {
        System.out.println("Printing entity with name " + entityDTO.getName() + ":");
        System.out.println("    Amount in population: " + entityDTO.getInitAmountInPopulation());
        System.out.println("    Properties:");
        entityDTO.getPropertyDTOList().forEach(ConsoleOut::printFullDetailPropertyDTO);
        System.out.println();

    }


    /**
     * Prints the full detail of a property.
     * @param propertyDTO TDO containing information of the property to display.
     */
    public static void printFullDetailPropertyDTO(PropertyDTO propertyDTO) {
        if(propertyDTO.getRangeDTO() != null)
            System.out.println("        Name: " + propertyDTO.getName() + ", Type: " + propertyDTO.getType() + ", Range: from- " + propertyDTO.getRangeDTO().getFrom() + " to- " + propertyDTO.getRangeDTO().getTo() + ", Is Randomly Generated: " + propertyDTO.isRandomGenerated());
        else
            System.out.println("        Name: " + propertyDTO.getName() + ", Type: " + propertyDTO.getType() + ", Is Randomly Generated: " + propertyDTO.isRandomGenerated());

    }

    /**
     * Prints the full detail of a rule.
     * @param ruleDTO TDO containing information of the rule to display.
     */
    public static void printFullDetailRuleDTO(RuleDTO ruleDTO) {
        System.out.print("    Name: " + ruleDTO.getName() + ", When activated: [ticks-" + ruleDTO.getTicks() + ", probability-" + ruleDTO.getProbability() + "], Number of actions: " + ruleDTO.getNofActions() + ", Action names: [");

        // Print action names
        List<String> namesList = ruleDTO.getActionNamesList();
        for (int i = 0; i < namesList.size(); i++) {
            System.out.print(namesList.get(i));
            if(i < namesList.size() - 1)
                System.out.print(", ");
        }

        System.out.println("]");
    }

    /**
     * Prints the full detail of a termination rule.
     * @param terminationRuleDTO TDO containing information of the termination rule to display.
     */
    public static void printFullDetailTerminationRuleDTO(TerminationRuleDTO terminationRuleDTO) {
        System.out.println("    Type: " + terminationRuleDTO.getType() + ", Value: " + terminationRuleDTO.getValue());
    }


    /**
     * Prints entity's name.
     * @param entityDTO DTO containing entity's details.
     */
    public static void printEntity(EntityDTO entityDTO) {
        System.out.println(entityDTO.getName());
    }


    /**
     * Prints all values of a property, and the number of entity instances of each one.
     * @param propertyValuesMap map containing a property's value and counter of how many entity instances are with its amount.
     */
    public static void printPropertyValues(Map<Object, Integer> propertyValuesMap) {
        propertyValuesMap.forEach(
                (value, count) -> System.out.println("Value: " + value + " , Number of Entities: " + count)
        );
    }


    /**
     * Print a simple message.
     */
    public static void printMessage(String text) {
        System.out.println(text);
    }


    /**
     * Prints the current values of the environment variables
     * @param environmentVariableDTOList list of all environment variables in the program
     */
    public static void printEnvironmentVariables(List<EnvironmentVariableDTO> environmentVariableDTOList) {
        printTitle("Environment Variables");

        int counter = 1;
        for (EnvironmentVariableDTO environmentVariableDTO : environmentVariableDTOList) {
            System.out.println(counter++ + ". Name: " + environmentVariableDTO.getName() + " , Current Value: " + environmentVariableDTO.getValue());
        }
    }


    /**
     * Prints simulation end message.
     * @param simulationEndTDO TDO that contains ID and reason of the simulation ended.
     */
    public static void printSimulationEnd(SimulationEndTDO simulationEndTDO) {
        printTitle("Simulation End Results");

        printMessage("Simulation with ID \"" + simulationEndTDO.getSimulationId() + "\"");
        printMessage("Ended with termination rule of \"" + simulationEndTDO.getEndReason() + "\"");
    }


    /**
     * Prints simulation main entities list.
     * @param entityDTOList TDO list that contains EntityDTOs with info on the main entities of the simulation.
     */
    public static void printEntitiesList(List<EntityDTO> entityDTOList) {
        int counter = 1;

        ConsoleOut.printTitle("Entities List");
        for(EntityDTO entityDTO : entityDTOList) {
            System.out.print(counter++ + ". ");
            ConsoleOut.printEntity(entityDTO);
        }

        if(entityDTOList.isEmpty())
            ConsoleOut.printMessage("No entities to display...");
    }


    /**
     * Prints a main entity's properties list.
     * @param propertyDTOList TDO list that contains PropertyDTOs with info on a main entity's properties.
     */
    public static void printPropertiesList(List<PropertyDTO> propertyDTOList) {
        int counter = 1;

        ConsoleOut.printTitle("Properties List");
        for(PropertyDTO propertyDTO : propertyDTOList) {
            System.out.print(counter++ + ". ");
            ConsoleOut.printMessage(propertyDTO.getName());
        }

        if(propertyDTOList.isEmpty())
            ConsoleOut.printMessage("No properties to display...");
    }


    /**
     * Prints error information on a runtime exception.
     * @param e the runtime exception error that was thrown.
     */
    public static void printRuntimeException(RuntimeException e) {
        ConsoleOut.printMessage("Cause: " + e.getMessage());
        ConsoleOut.printMessage("Exception's Class: " + e.getClass().toString());
//        printTrace(e);
    }


    /**
     * Prints the trace of a runtime exception.
     * @param e the runtime exception error that was thrown.
     */
    public static void printTrace(RuntimeException e) {
        ConsoleOut.printMessage("Trace: ");
        for (StackTraceElement stackTraceElement : e.getStackTrace())
            ConsoleOut.printMessage("   " + stackTraceElement.toString());
    }
}






