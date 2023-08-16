package com.idansh.engine.property.creator.factory;

import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;

/**
 * Interface for the creation process of properties.
 */
public interface PropertyFactory {
    /**
     * Creates a new property according to the instructions provided.
     */
    Property createProperty();


    /**
     * @return The name of the property factory.
     */
    String getName();


    /**
     * @return The type of the property factory.
     */
    PropertyType getType();


    /**
     * @return The value of the property factory.
     */
    Object getValue();


    /**
     * Checks if the property is of the type FLOAT or INTEGER.
     * @return true if the property factory given is numeric, false otherwise.
     */
    boolean isNumericProperty();


    /**
     * Updates the value of the property factory.
     * Has to be of the same type of the property.
     */
    void updateValue(Object newValue);
}
