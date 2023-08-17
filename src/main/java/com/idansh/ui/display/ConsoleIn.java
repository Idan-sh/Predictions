package com.idansh.ui.display;

import com.idansh.ui.manager.MenuOptions;
import com.sun.nio.sctp.IllegalReceiveException;

import java.util.Scanner;

/**
 * Contains static methods that receives user input and returns it.
 */
public class ConsoleIn {
    Scanner scanner = new Scanner(System.in);

    public String getInput(){
        return scanner.nextLine();
    }

    /**
     * Tries to get an int input from user.
     * if fails throws exception
     * @throws NumberFormatException if user failed to enter a valid number.
     */
    public int getIntInput() {
        try {
            return Integer.parseInt(getInput());
        } catch(NumberFormatException e) {
            ConsoleOut.printError("invalid input, not a number!");
            throw new NumberFormatException();
        }
    }

    /**
     * Receives user input from the menu options.
     * @return MenuOptions enum which defines the user choice.
     * @throws NumberFormatException in case the input string cannot be converted to int.
     * @throws IllegalReceiveException in case the option number received is not a valid choice.
     */
    public MenuOptions getMenuInput()
    {
        MenuOptions userInput;

        // Try to parse the input to int, if fails throws exception
        int optionNum = Integer.parseInt(getInput()) - 1;

        // Check if the option chosen is within the menu's range
        if(optionNum < 0 || optionNum > MenuOptions.values().length)
            throw new IllegalReceiveException();

        userInput = MenuOptions.values()[optionNum];

        return userInput;
    }
}
