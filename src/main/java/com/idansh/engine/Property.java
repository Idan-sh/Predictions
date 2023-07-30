package com.idansh.engine;


import java.util.LinkedList;
import java.util.List;

/**
 * Property for an entity
 * Possible genetic types: decimal, float, boolean, string
 */
public class Property<T> extends HasUniqueName {
    static List<String> namesPool = new LinkedList<>();
    T value = null;
    Range range = null;      // If exists
    Boolean isRandom = null;   // Was the property assigned randomly

    public Property(String name) {
        super(name, namesPool);
    }
}
