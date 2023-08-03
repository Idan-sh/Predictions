package com.idansh.engine.property.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * A property for an entity, base class without the value
 * Extend to add value types.
 * @apiNote Current possible value types: int, float, boolean, string
 */
public abstract class Property {
    /**
     * Enum class for the possible types of properties' values.
     * @apiNote possible types - Integer, Float, Boolean, String
     */
    public enum PropertyType {
        INTEGER, FLOAT, BOOLEAN, STRING;
    }

    Boolean isRandom;    // Was the property randomly assigned

    public Property(Boolean isRandom) {
        this.isRandom = isRandom;
    }
}
