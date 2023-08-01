package com.idansh.engine.property;

import com.idansh.engine.objects.helpers.Range;

public class PropertyDecimal extends Property{
    int value;
    Range range = null;      // If exists

    public PropertyDecimal(String name) {
        super(name);
    }
}
