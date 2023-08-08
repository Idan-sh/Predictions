package com.idansh.engine.property.creator.factory;

import com.idansh.engine.property.instance.Property;

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
}
