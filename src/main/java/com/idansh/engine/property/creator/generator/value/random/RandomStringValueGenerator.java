package com.idansh.engine.property.creator.generator.value.random;

import com.idansh.engine.helpers.RandomValue;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;

/**
 * Value generator for random integer values.
 */
public class RandomStringValueGenerator implements ValueGenerator<String> {
    @Override
    public String generateValue() {
        return RandomValue.getRandomString();
    }
}
