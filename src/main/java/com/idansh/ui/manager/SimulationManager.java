package com.idansh.ui.manager;


import com.idansh.ui.display.ConsoleIn;
import com.idansh.ui.display.ConsoleOut;
import com.sun.nio.sctp.IllegalReceiveException;

import static java.lang.System.exit;

/**
 * Handles the simulations.
 * The main loop of the simulation will run through this class.
 */
public class SimulationManager {
    EngineHandler engineHandler;
    ConsoleIn consoleIn;

    public SimulationManager() {
        engineHandler = new EngineHandler();
        consoleIn = new ConsoleIn();
    }


    /**
     * Starts the main loop of the program.
     */
    public void run() {
        try {
            // Run until user chooses to exit
            do {
                ConsoleOut.printMenu();
            } while (handleMenuChoice());   // Handle the user's choice and check if chose to exit.

            ConsoleOut.printGoodbye();
            exit(0);
        } catch (RuntimeException e) { // Catch all unhandled exceptions, display their details and finish the program
            ConsoleOut.printRuntimeException(e);
        }
    }


    /**
     * Receives user's menu choice input and handles it accordingly.
     * @return false if the user chose to exit, and true otherwise.
     * @throws NumberFormatException in case the input string cannot be converted to int.
     * @throws IllegalReceiveException in case the option number received is not a valid choice.
     */
    public boolean handleMenuChoice() {
        MenuOptions menuOption;
        try {
            menuOption = consoleIn.getMenuInput();
        } catch (IllegalArgumentException e) {
            ConsoleOut.printError(e.getMessage());
            return true;
        }

        switch (menuOption) {
            case LOAD_FILE:
                engineHandler.loadSimulationFromFile();
                return true;

            case SHOW_SIMULATION_DETAILS:
                if(!engineHandler.isSimulationLoaded()) {
                    ConsoleOut.printError("no simulation loaded! please load before trying to show details!");
                    return true;
                }
                engineHandler.showCurrentSimulationDetails();
                return true;

            case RUN_SIMULATION:
                engineHandler.runSimulation();
                return true;

            case SHOW_PAST_SIMULATION_DETAILS:
                engineHandler.showPastSimulations();
                return true;

            case EXIT:
                return false;

            default:
                throw new RuntimeException("menuOption type is invalid!"); // This error should not happen on wrong user input, consoleIn.getMenuInput() throws that error
        }
    }
}
