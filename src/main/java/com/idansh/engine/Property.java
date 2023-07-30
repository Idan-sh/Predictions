package com.idansh.engine;


/**
 * Property for an entity
 */
public class Property<T> extends HasUniqueName {
    T type = null;
    Range range = null;      // If exists
    Boolean isRandom = null;   // Was the property assigned randomly

    public Property(String name) {
        super(name);
    }
}
