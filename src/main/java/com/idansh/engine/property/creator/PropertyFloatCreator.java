package com.idansh.engine.property.creator;

import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.objects.Property;
import com.idansh.engine.property.objects.PropertyFloat;

import static com.idansh.engine.helpers.RandomValue.getRandomFloat;

public class PropertyFloatCreator implements PropertyFactory {
    Boolean isRandom;
    Float value;
    Range range = null;      // If exists

    /**
     * Constructor that builds a new property creator.
     * @param value constant value, will be ignored if isRandom param is true.
     * @param isRandom true if the value should be randomized.
     */
    public PropertyFloatCreator(Boolean isRandom, Float value) {
        this.isRandom = isRandom;

        if(!isRandom)
            this.value = value;
        else
            this.value = null;
    }

    /**
     * Factory Method to create property instance.
     * @return a new instance of a float property.
     */
    @Override
    public Property createProperty() {
        // Means that isRandom is true
        if(value == null)
            value = getRandomFloat(range.getBottom(), range.getTop()); // Assign random float value

        return new PropertyFloat(isRandom, value);
    }
}
