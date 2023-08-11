package com.idansh.engine.property.instance;

import com.idansh.engine.helpers.Range;

import java.util.Optional;

/**
 * A property for an entity, base class without the value,
 * Extend to add value types.
 * @apiNote Current possible value types: int, float, boolean, string
 */
public class Property {
    private final String name;
    private final PropertyType type;
    private final Range range;  // Optional field, can be set for numeric properties.
    private Object value;

    public Property(String name, PropertyType type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.range = null;
    }

    public Property(String name, PropertyType type, Object value, Range range) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.range = range;
    }

    public void updateValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public PropertyType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
