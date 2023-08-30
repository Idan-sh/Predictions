package com.idansh.javafx.helpers;

/**
 * Contains static methods to get Text Formatters of various types.
 */
public abstract class TextFormatterHelper {
    /**
     * Creates and returns a String text formatter that only accepts
     * integer numbers input.
     */
    public static javafx.scene.control.TextFormatter<String> getIntTextFormatter() {
        String pattern = "[0-9]*";

        return new javafx.scene.control.TextFormatter<>(
                change -> {
                    if (change.getControlNewText().matches(pattern))
                        return change;
                    return null;
                });
    }


    /**
     * Creates and returns a String text formatter that only accepts
     * float numbers input.
     */
    public static javafx.scene.control.TextFormatter<String> getFloatTextFormatter() {
        String pattern = "[0-9.]*";

        return new javafx.scene.control.TextFormatter<>(
                change -> {
                    if (change.getControlNewText().matches(pattern))
                        return change;
                    return null;
                });
    }


    /**
     * Creates and returns a String text formatter that only accepts
     * lower case letters input.
     */
    public static javafx.scene.control.TextFormatter<String> getLettersTextFormatter() {
        String pattern = "[a-z]*";

        return new javafx.scene.control.TextFormatter<>(
                change -> {
                    if (change.getControlNewText().matches(pattern))
                        return change;
                    return null;
                });
    }


    /**
     * Creates and returns a String text formatter that only accepts
     * letters and some special characters input.
     */
    public static javafx.scene.control.TextFormatter<String> getStringTextFormatter() {
        String pattern = "^[A-Za-z0-9().\\-_,?! ]+$";

        return new javafx.scene.control.TextFormatter<>(
                change -> {
                    if (change.getControlNewText().matches(pattern))
                        return change;
                    return null;
                });
    }
}
