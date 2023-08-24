package com.idansh.javafx.manager;

import com.idansh.dto.range.RangeDTO;

public abstract class InputValidator {

    /**
     * Checks if the given integer value is in the given range.
     * @return true if the value is within the range, false otherwise.
     */
    public static boolean isIntegerInRange(int value, RangeDTO rangeDTO) {
        return (value >= rangeDTO.getFrom() && value <= rangeDTO.getTo());
    }

    /**
     * Checks if the given float value is in the given range.
     * @return true if the value is within the range, false otherwise.
     */
    public static boolean isFloatInRange(float value, RangeDTO rangeDTO) {
        return (value >= rangeDTO.getFrom() && value <= rangeDTO.getTo());
    }


    /**
     * Checks if the given boolean value in string format is valid.
     * @return true if the value is in the correct format, false otherwise.
     */
    public static boolean isBooleanValid(String value) {
        return (value.equals("true") || value.equals("false"));
    }


    /**
     * Checks if the given string value is in a correct format.
     * @return true if the value is in the correct format, false otherwise.
     */
    public static boolean isStringValid(String value) {
        String pattern = "^[A-Za-z0-9().\\-_,?! ]+$";
        return value.matches(pattern);
    }
}