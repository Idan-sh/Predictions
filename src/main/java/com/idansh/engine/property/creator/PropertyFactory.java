package com.idansh.engine.property.creator;

import com.idansh.engine.property.objects.Property;

public interface PropertyFactory {

    /**
     * Factory Method for creating a new property of this type.
     */
    public Property createProperty();
}
