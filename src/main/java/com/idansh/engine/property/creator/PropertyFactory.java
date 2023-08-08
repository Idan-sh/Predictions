package com.idansh.engine.property.creator;

import com.idansh.engine.property.instance.Property;

/**
 * Interface for the creation process of properties.
 */
public interface PropertyFactory {

    /**
     * Creates a new property according to the instructions provided.
     */
    public Property createProperty();
}
