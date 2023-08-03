package com.idansh.engine.property.objects;

public class PropertyString extends Property{
    String value;

    public PropertyString(Boolean isRandom, String value) {
        super(isRandom);
        this.value = value;
    }
}
