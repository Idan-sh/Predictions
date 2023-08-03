package com.idansh.engine.property.objects;

public class PropertyFloat extends Property{
    Float value;

    public PropertyFloat(Boolean isRandom, Float value) {
        super(isRandom);
        this.value = value;
    }
}
