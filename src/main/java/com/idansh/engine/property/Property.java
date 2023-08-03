package com.idansh.engine.property;

import java.util.LinkedList;
import java.util.List;

/**
 * A property for an entity, base class without the value
 * Extend to add value types.
 * ** Current possible value types: decimal, float, boolean, string
 */
public abstract class Property {
    /**
     * Enum class for the possible types of properties' values.
     * @apiNote possible types - Integer, Float, Boolean, String
     */
    public enum PropertyType {
        INTEGER, FLOAT, BOOLEAN, STRING;
    }

    Boolean isRandom = null;   // Was the property assigned randomly

    public Property() {

    }
}
