package com.idansh.engine.property.creator;

import com.idansh.engine.property.objects.Property;
import com.idansh.engine.property.objects.PropertyBoolean;
import com.idansh.engine.random.RandomBoolean;

import static com.idansh.engine.random.RandomBoolean.getRandomBoolean;

public class PropertyBooleanCreator implements PropertyFactory{
    Boolean value;
    Boolean isRandom;

    /**
     * Constructor that builds a new property creator.
     * @param value constant value, will be ignored if isRandom param is true.
     * @param isRandom true if the value should be randomized.
     */
    public PropertyBooleanCreator(Boolean value, Boolean isRandom) {
        this.isRandom = isRandom;

        if(!isRandom)
            this.value = value;
        else
            this.value = getRandomBoolean(); // Assign random boolean value
    }

    @Override
    public Property createProperty() {
        return new PropertyBoolean(value, isRandom);
    }
}
