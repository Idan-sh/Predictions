package com.idansh.engine.property.instance;

import com.idansh.engine.helpers.Range;

import java.util.List;

/**
 * A property for an entity, base class without the value,
 * Extend to add value types.
 * @apiNote Current possible value types: int, float, boolean, string
 */
public class Property {
    private final String name;
    private final PropertyType type;
    private final Range range;  // Optional field, can be set for numeric properties.
    private Object value;

    public Property(String name, PropertyType type, Object value) {
        this.name = name;
        this.type = type;

        if(value instanceof Integer)
            this.value = new Float((Integer) value);
        else
            this.value = value;

        this.range = null;
    }

    public Property(String name, PropertyType type, Object value, Range range) {
        this.name = name;
        this.type = type;

        if(value instanceof Integer)
            this.value = new Float((Integer) value);
        else
            this.value = value;

        this.range = range;
    }

    public String getName() {
        return name;
    }

    public PropertyType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public Range getRange() {
        return range;
    }

    /**
     * Checks if the property is of the type FLOAT or INTEGER.
     * @return true if the property factory given is numeric, false otherwise.
     */
    public boolean isNumericProperty() {
        return PropertyType.FLOAT.equals(type);
    }


    /**
     * Sets a new value to the property.
     * @param newValue the number to set as the property's value.
     */
    public void setValue(Object newValue) {
        // Check that the type of the new value is of the property
        if(!isNewValueOfPropertyType(newValue))
            throw new IllegalArgumentException("value received in setValue is not of the property's type! new value received is of type " + newValue.getClass() + ", while the property's type is " + type.getTypeString());

        // Checks if the newValue is a number, and if so, checks if it's in the range.
        if((newValue instanceof Integer || newValue instanceof Float) && isRangeOverflow(newValue))
            throw new IllegalArgumentException("newValue received in setValue is not within the property's range! new value received is " + newValue + ", while the property's range is (" + range.getBottom() + ", " + range.getTop() + ")");

        // Update the value
        if(newValue instanceof Integer)
            this.value = new Float((Integer) newValue);
        else
            this.value = newValue;
    }


    /**
     * Checks if the received newValue is of the type of the property.
     * if so returns true, otherwise returns false.
     */
    private boolean isNewValueOfPropertyType(Object newValue) {
        switch(type) {
            case INTEGER:
                if (newValue instanceof Integer)
                    return true;
                break;

            case FLOAT:
                if (newValue instanceof Float || newValue instanceof Integer)
                    return true;
                break;

            case BOOLEAN:
                if (newValue instanceof Boolean)
                    return true;
                break;

            case STRING:
                if (newValue instanceof String)
                    return true;
                break;
        }
        return false;
    }


    /**
     * Adds a number of the type Float to the property's value.
     * If the addition exceeds the range bounds, continue without updating the property's value.
     * @param toAdd the number to add to the property's value.
     */
    public void addNumToValue(Object toAdd) {
        if(!isNumericProperty())
            throw new IllegalArgumentException("can preform addNumToValue only on numeric properties! the property's type is \"" + type.getTypeString() + "\".");

        if(!(toAdd instanceof Integer) && !(toAdd instanceof Float))
            throw new IllegalArgumentException("can only add a number to the property's value! got value of type \" + toAdd.getClass()");

        // Check if exceeded the range, if so then continue without updating (without throwing en exception)
        if(isRangeOverflowAfterAddition(toAdd))
            return;

        // Perform the addition
        if(toAdd.getClass().equals(Integer.class)) {
            value = (float) value + (int) toAdd;
        }
        else {
            value = (float) value + (float) toAdd;
        }
    }


    /**
     * Checks if the value of the property after an addition has reached above the top of the range,
     * or reached below the bottom of the range.
     * Also checks for non-compatible property type or value to add type.
     * @param toAdd a number of the type of the property's value to add to it.
     * @return true if reached outside the range, false otherwise.
     * @throws IllegalArgumentException in case type of the value to add or the property are not numeric.
     */
    private boolean isRangeOverflowAfterAddition(Object toAdd) {
        if(range == null)
            return false;

        if(!type.isNumeric())
            throw new IllegalArgumentException("Can only check range overflow on numeric properties! the property is of type \"" + type + "\"");

        if(!(toAdd instanceof Integer || toAdd instanceof Float))
            throw new IllegalArgumentException("Can only check range overflow with a numeric addition, got value to add of type \"" + toAdd.getClass() + "\"");

        if(type.equals(PropertyType.INTEGER)) {
            float newVal = (int) value + (int) toAdd;
            return newVal > range.getTop() || newVal < range.getBottom();
        }

        if(type.equals(PropertyType.FLOAT)) {
            float newVal;
            if(toAdd instanceof Integer) {
                newVal = (float) value + (int) toAdd;
            }
            else {
                newVal = (float) value + (float) toAdd;
            }
            return newVal > range.getTop() || newVal < range.getBottom();
        }

        throw new RuntimeException("Range overflow check failed!");
    }


    /**
     * Checks if the value reached above the top of the range,
     * or reached below the bottom of the range.
     * @param valueToSet a number of the type of the property's value to set to.
     * @return true if reached outside the range, false otherwise.
     */
    private boolean isRangeOverflow(Object valueToSet) {
        if(range == null)
            return false;

        if(!type.isNumeric())
            throw new IllegalArgumentException("Can only check range overflow on numeric properties! the property is of type \"" + type + "\"");

        if(!(valueToSet.getClass().equals(Integer.class) || valueToSet.getClass().equals(Float.class)))
            throw new IllegalArgumentException("Can only check range overflow with a numeric addition, got value to add of type \"" + valueToSet.getClass() + "\"");

        if(type.equals(PropertyType.INTEGER)) {
            float newVal = (int) valueToSet;
            return newVal > range.getTop() || newVal < range.getBottom();
        }

        if(type.equals(PropertyType.FLOAT)) {
            float newVal;
            if(valueToSet.getClass().equals(Integer.class)) {
                newVal = (int) valueToSet;
            }
            else {
                newVal = (float) valueToSet;
            }
            return newVal > range.getTop() || newVal < range.getBottom();
        }

        throw new RuntimeException("Range overflow check failed!");
    }
}
