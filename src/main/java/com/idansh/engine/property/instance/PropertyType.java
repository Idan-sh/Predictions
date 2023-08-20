package com.idansh.engine.property.instance;

/**
 * Enum class for the possible types of properties' values.
 * @apiNote possible types - Integer, Float, Boolean, String
 */
public enum PropertyType {
    INTEGER, FLOAT, BOOLEAN, STRING;

    /**
     * Converts a string of decimal/float/boolean/string to
     * its corresponding PropertyType.
     * @param s the property type in string format.
     * @return PropertyType that defines the string received.
     * @throws IllegalArgumentException if the string received is in invalid format (not one of the specified strings).
     */
    public static PropertyType getType(String s) {
        switch(s) {
            case "decimal":
                return INTEGER;

            case "float":
                return FLOAT;

            case "boolean":
                return BOOLEAN;

            case "string":
                return STRING;

            default:
                throw new IllegalArgumentException("Error: invalid property type- \"" + s + "\"");
        }
    }

    /**
     * Converts a PropertyType of integer/float/boolean/string to
     * its corresponding string format.
     * @return string that defines the property type received.
     * @throws IllegalArgumentException if the property type received is in invalid format (not one of the specified types).
     */
    public String getTypeString() {
        switch(this) {
            case INTEGER:
                return "decimal";

            case FLOAT:
                return "float";

            case BOOLEAN:
                return "boolean";

            case STRING:
                return "string";

            default:
                throw new IllegalArgumentException("Error: invalid property type- \"" + this + "\"");
        }
    }


    /**
     * Check if a given property type is of type FLOAT or INTEGER (numeric type).
     * @return true if the property given is of type FLOAT or INTEGER, false otherwise.
     */
    public boolean isNumeric() {
        return this.equals(PropertyType.FLOAT) || this.equals(PropertyType.INTEGER);
    }
}