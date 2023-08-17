package com.idansh.ui.display;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.property.PropertyDTO;
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
        // TODO: Implement this
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
}






