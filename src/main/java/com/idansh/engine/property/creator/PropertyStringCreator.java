package com.idansh.engine.property.creator;

import com.idansh.engine.property.objects.Property;
import com.idansh.engine.property.objects.PropertyString;

import static com.idansh.engine.helpers.RandomValue.getRandomString;

public class PropertyStringCreator implements PropertyFactory {
    Boolean isRandom;
    String value;


    /**
     * Constructor that builds a new property creator.
     * @param value constant value, will be ignored if isRandom param is true.
     * @param isRandom true if the value should be randomized.
     */
    public PropertyStringCreator(Boolean isRandom, String value) {
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
            value = getRandomString(); // Assign random float value

        return new PropertyString(isRandom, value);
    }
}
