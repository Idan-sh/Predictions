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
            ConsoleOut.printError("invalid input, not a valid integer number!");
            throw new IllegalArgumentException();
        }
    }


    /**
     * Tries to get a float input from user.
     * if fails throws exception
     * @throws NumberFormatException if user failed to enter a valid number.
     */
    public float getFloatInput() {
        try {
            return Float.parseFloat(getInput());
        } catch(NumberFormatException e) {
            ConsoleOut.printError("invalid input, not a valid float number!");
            throw new IllegalArgumentException();
        }
    }


    /**
     * Tries to get a boolean input from user.
     * if fails throws exception
     * @throws IllegalArgumentException if user failed to enter a valid boolean- "true"/"false".
     */
    public boolean getBooleanInput() {
        String input = getInput();

        if(input.equalsIgnoreCase("true"))
                return true;
        else
            if (input.equalsIgnoreCase("false"))
                return false;
            else throw new IllegalArgumentException("invalid boolean value received! enter true/false!");
    }


    /**
     * Receives user input from the menu options.
     * @return MenuOptions enum which defines the user choice.
     * @throws NumberFormatException in case the input string cannot be converted to int.
     * @throws IllegalReceiveException in case the option number received is not a valid choice.
     */
    public MenuOptions getMenuInput() throws IllegalArgumentException
    {
        MenuOptions userInput;
        int optionNum;

        // Try to parse the input to int, if fails throws exception
        try {
            optionNum = Integer.parseInt(getInput()) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid input, enter a number for menu choice!");
        }

        // Check if the option chosen is within the menu's range
        if(optionNum < 0 || optionNum > MenuOptions.values().length)
            throw new IllegalArgumentException("invalid menu choice received! try again...");

        userInput = MenuOptions.values()[optionNum];

        return userInput;
    }
}
