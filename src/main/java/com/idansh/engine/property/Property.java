package com.idansh.engine.property;


import com.idansh.engine.HasUniqueName;
import com.idansh.engine.Range;

import java.util.LinkedList;
import java.util.List;

/**
 * Property for an entity
 * Possible value types: decimal, float, boolean, string
 */
public abstract class Property extends HasUniqueName {
    static List<String> namesPool = new LinkedList<>();
    Range range = null;      // If exists
    Boolean isRandom = null;   // Was the property assigned randomly

    public Property(String name) {
        super(name, namesPool);
    }
}
