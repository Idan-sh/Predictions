package com.idansh.ui.display;

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
}

//
//    /**
//     * Receives a string of simulation's details, formats it and prints it.
//     */
//    public static void showSimulationDetails(String simDetails) {
//        // TODO: Implement this
//    }
//
//
//    /**
//     * Prints out the basic details of all past simulation saved in the system.
//     *
//     * @param pastSimulationsData - an array containing all result data from the currently loaded simulation
//     *                            up to this point.
//     */
//    public static void showShortDetailsOfAllPastSimulations(ResultData[] pastSimulationsData) {
//
//        // Sort the Result data by date time
//        Arrays.sort(pastSimulationsData, (r1, r2)-> {
//            return r1.getDateTime().compareTo(r2.getDateTime());
//        });
//
//        printTitle("PREVIOUS SIMULATION RUNS");
//        System.out.println();
//        System.out.printf("%-22s%-22s%-22s\n", "no.", "ID", "Date & Time");
//        int counter = 1;
//        for (ResultData r : pastSimulationsData
//        ) {
//            System.out.printf("#%-21s%-22s%-22s\n", counter++, r.getId(), r.getDateTimeString());
//        }
//    }
//
//
//
//    /**
//     * Shows a short representation of a simulation.
//     * Short representation includes: id, name, date.
//     *
//     * @param simDetails
//     */
//    public static void printSimulationDetailsShort(String simDetails) {
//        // TODO: Implement this
//    }
//
//    //TODO: Implement this after we have the structure of a simulation's result data
//    public static void printResultData(ResultData resultData)
//    {
//    }
