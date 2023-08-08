package com.idansh.engine.property.instance;

/**
 * A property for an entity, base class without the value,
 * Extend to add value types.
 * @apiNote Current possible value types: int, float, boolean, string
 */
public class Property {
    private final String name;
    private final PropertyType valueType;
    private Object value;

    public Property(String name, PropertyType valueType, Object value) {
        this.name = name;
        this.valueType = valueType;
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public PropertyType getValueType() {
        return valueType;
    }

    public Object getValue() {
        return value;
    }
}
