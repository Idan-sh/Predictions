package com.idansh.engine.property;

import com.idansh.engine.objects.helpers.HasUniqueName;

import java.util.LinkedList;
import java.util.List;

/**
 * A property for an entity, base class without the value
 * Extend to add value types.
 * ** Current possible value types: decimal, float, boolean, string
 */
public abstract class Property extends HasUniqueName {
    static List<String> namesPool = new LinkedList<>();
    Boolean isRandom = null;   // Was the property assigned randomly

    public Property(String name) {
        super(name, namesPool);
    }
}
