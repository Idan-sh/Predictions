package com.idansh.ui.display;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariableDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;

import java.util.*;

/**
 * Prints relevant menus and text to the console for the user.
 */
public class ConsoleOut {
    /**
     * Print the main menu of the program.
     */
    public static void printMenu() {
        printTitle("MAIN MENU");
        System.out.println("1. Load simulation from XML file");
        System.out.println("2. Show simulation details");
        System.out.println("3. Run simulation");
        System.out.println("4. Show details of past simulation run");
        System.out.println("5. Exit");
        System.out.print("Please enter the number of command to run: ");
    }

    public static void printPastSimulationsMenu() {
        System.out.println("Enter how to show the past simulation result:");
        System.out.println("1. Number of entities of each type");
        System.out.println("2. Entity's property value");
        System.out.print("Please enter the number of command to run: ");
    }

    /**
     * Print a title in the format:
     * ####### TITLE #######
     * @param text string to print in the title.
     */
    private static void printTitle(String text) {
        System.out.println("\n####### " + text.toUpperCase() + " #######");
    }

    /**
     * Prints an error message to the console.
     * @param text the error message to print.
     */
    public static void printError(String text) {
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
     * Receives a SimulationResultDTO with the data of a past simulation,
     * formats it and prints it.
     * @param simulationResultDTO a simulation result to print its details.
     */
    public static void printPastSimulationDetails(SimulationResultDTO simulationResultDTO) {
        // todo- print simulation result DTO
    }


    /**
     * Prints out the basic details of past simulation received from the engine.
     * @param pastSimulationsResults - an array of SimulationResultDTO containing simulations results.
     */
    public static void printPastSimulationsShortDetails(List<SimulationResultDTO> pastSimulationsResults) {
        // Sort the simulations results by their date time
        pastSimulationsResults.sort(Comparator.comparing(SimulationResultDTO::getDateTime));

        printTitle("past simulations results");
        System.out.printf("%-22s%-22s%-22s\n", "no.", "ID", "Date & Time");

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
        System.out.println(entityDTO.getName() + ": initial amount- " + entityDTO.getInitAmountInPopulation() + ", final amount- " + entityDTO.getCurrAmountInPopulation());
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
     * Prints property's name.
     * @param propertyDTO DTO containing property's details.
     */
    public static void printProperty(PropertyDTO propertyDTO) {
        System.out.println(propertyDTO.getName() + ": " + propertyDTO.getValue());
    }

    public static void printMessage(String text) {
        System.out.println(text);
        System.out.println();
    }


    public static void printEnvironmentVariables(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        printTitle("Environment Variables");
        environmentVariablesListDTO.getEnvironmentVariableInputDTOs().forEach(ConsoleOut::printSingleEnvironmentVariable);
    }

    public static void printSingleEnvironmentVariable(EnvironmentVariableDTO environmentVariableDTO) {
        System.out.println("    Name: " + environmentVariableDTO.getName() + ", Value: " + environmentVariableDTO.getValue());
    }
}






