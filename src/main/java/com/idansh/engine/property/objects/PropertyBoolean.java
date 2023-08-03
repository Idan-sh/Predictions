package com.idansh.engine.property.objects;


import com.idansh.engine.property.creator.PropertyFactory;

public class PropertyBoolean extends Property {
    Boolean value;

    public PropertyBoolean(Boolean isRandom, Boolean value) {
        super(isRandom);
        this.value = value;
    }
}
