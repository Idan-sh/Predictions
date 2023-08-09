package com.idansh.engine.property.instance;

/**
 * A property for an entity, base class without the value,
 * Extend to add value types.
 * @apiNote Current possible value types: int, float, boolean, string
 */
public class Property {
    private final String name;
    private final PropertyType type;
    private Object value;

    public Property(String name, PropertyType type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
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
