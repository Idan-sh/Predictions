package com.idansh.engine.property.creator.generator.value.random;

import com.idansh.engine.helpers.RandomValue;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;

/**
 * Value generator for random boolean values.
 */
public class RandomBooleanValueGenerator implements ValueGenerator<Boolean> {
    @Override
    public Boolean generateValue() {
        return RandomValue.getRandomBoolean();
    }
}
