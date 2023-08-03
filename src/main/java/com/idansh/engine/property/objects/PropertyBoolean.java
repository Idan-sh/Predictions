package com.idansh.engine.property.objects;

public class PropertyBoolean extends Property {
    Boolean value;

    public PropertyBoolean(Boolean isRandom, Boolean value) {
        super(isRandom);
        this.value = value;
    }
}
