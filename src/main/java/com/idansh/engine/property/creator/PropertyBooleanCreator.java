package com.idansh.engine.property.creator;

import com.idansh.engine.property.objects.Property;
import com.idansh.engine.property.objects.PropertyBoolean;

import static com.idansh.engine.helpers.RandomValue.getRandomBoolean;

public class PropertyBooleanCreator implements PropertyFactory{
    Boolean isRandom;
    Boolean value;


    /**
     * Constructor that builds a new property creator.
     * @param value constant value, will be ignored if isRandom param is true.
     * @param isRandom true if the value should be randomized.
     */
    public PropertyBooleanCreator(Boolean isRandom, Boolean value) {
        this.isRandom = isRandom;

        if(!isRandom)
            this.value = value;
        else
            this.value = null;
    }

    /**
     * Factory Method to create property instance.
     * @return a new instance of a boolean property.
     */
    @Override
    public Property createProperty() {
        // Means that isRandom is true
        if(value == null)
            value = getRandomBoolean(); // Assign random boolean value

        return new PropertyBoolean(isRandom, value);
    }
}
