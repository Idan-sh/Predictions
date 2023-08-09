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
     * @return The name of the property
     */
    String getName();


    /**
     * @return The type of the property
     */
    PropertyType getType();
}
