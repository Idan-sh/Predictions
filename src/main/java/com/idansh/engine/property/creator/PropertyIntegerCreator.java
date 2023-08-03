package com.idansh.engine.property.creator;

import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.objects.Property;
import com.idansh.engine.property.objects.PropertyFloat;
import com.idansh.engine.property.objects.PropertyInteger;

import static com.idansh.engine.helpers.RandomValue.getRandomFloat;
import static com.idansh.engine.helpers.RandomValue.getRandomInt;

public class PropertyIntegerCreator implements PropertyFactory {
    Boolean isRandom;
    Integer value;
    Range range = null;      // If exists

    /**
     * Constructor that builds a new property creator.
     * @param value constant value, will be ignored if isRandom param is true.
     * @param isRandom true if the value should be randomized.
     */
    public PropertyIntegerCreator(Boolean isRandom, Integer value) {
        this.isRandom = isRandom;

        if(!isRandom)
            this.value = value;
        else
            this.value = null;
    }

    /**
     * Factory Method to create property instance.
     * @return a new instance of a integer property.
     */
    @Override
    public Property createProperty() {
        // Means that isRandom is true
        if(value == null)
            value = getRandomInt(range.getBottom(), range.getTop()); // Assign random float value

        return new PropertyInteger(isRandom, value);
    }
}
