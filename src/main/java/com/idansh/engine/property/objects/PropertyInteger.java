package com.idansh.engine.property.objects;

public class PropertyInteger extends Property{
    int value;

    public PropertyInteger(Boolean isRandom, int value) {
        super(isRandom);
        this.value = value;
    }
}
