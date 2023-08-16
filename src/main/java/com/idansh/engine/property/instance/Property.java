package com.idansh.engine.property.instance;

import com.idansh.engine.helpers.Range;

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
        this.value = value;
        this.range = null;
    }

    public Property(String name, PropertyType type, Object value, Range range) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.range = range;
    }

    public void updateValue(Object value) {
        this.value = value;
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


    /**
     * Checks if the property is of the type FLOAT or INTEGER.
     * @return true if the property factory given is numeric, false otherwise.
     */
    public boolean isNumericProperty() {
        return PropertyType.INTEGER.equals(type) || PropertyType.FLOAT.equals(type);
    }


    /**
     * Sets a new value to the property.
     * @param toAdd the number to add to the property's value.
     */
    public void setValue(Object newValue) {
        // Checks if the newValue is a number, and if so, checks if it's in the range.
        if((newValue instanceof Integer || newValue instanceof Float) && isRangeOverflow(newValue))
            throw new IllegalArgumentException("Error: newValue received in setValue is not in the property's range!");

        if(!isNewValueOfPropertyType(newValue))
            throw new IllegalArgumentException("Error: newValue received in setValue is not in the property's type!");

        // Update the value
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
                if (newValue instanceof Float)
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
     * Adds a number of the type Integer/Float (non-primitive) to the property's value.
     * @param toAdd the number to add to the property's value.
     */
    public void addNumToValue(Object toAdd) {
        if(!this.isNumericProperty())
            throw new IllegalArgumentException("Error: can preform addNumToValue only on numeric properties!");

        if(!(toAdd instanceof Integer) && !(toAdd instanceof Float))
            throw new IllegalArgumentException("Error: can only add a number (non-primitive) to the property's value!");

        // Perform the addition only if it won't exceed the range bounds.
        if(!isRangeOverflow(toAdd)) {
            if(type.equals(PropertyType.INTEGER))
                value = (int) value + (int) toAdd;
            else
                if(type.equals(PropertyType.FLOAT))
                    value = (float) value + (float) toAdd;
        }
    }


    /**
     * Checks if the value reached above the top of the range,
     * or reached below the bottom of the range.
     * If so, returns true, otherwise returns false.
     */
    private boolean isRangeOverflow(Object toAdd) {
        if(range == null)
            return false;

        if(type.equals(PropertyType.INTEGER)) {
            return (int) value + (int) toAdd > range.getTop() || (int) value + (int) toAdd < range.getBottom();
        }
        else if(type.equals(PropertyType.FLOAT)) {
            return (float) value + (float) toAdd > range.getTop() || (float) value + (float) toAdd < range.getBottom();
        }

        throw new IllegalArgumentException("Error: can only perform checkRange on numeric properties!");
    }
}
