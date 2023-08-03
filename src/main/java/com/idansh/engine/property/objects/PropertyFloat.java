package com.idansh.engine.property.objects;

import com.idansh.engine.helpers.Range;

public class PropertyFloat extends Property{
    Float value;

    public PropertyFloat(Boolean isRandom, Float value) {
        super(isRandom);
        this.value = value;
    }
}
