package com.idansh.engine.property.creator.generator.value.fixed;

import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;

/**
 * Value generator for fixed values.
 * @param <T> The type of the value that will be generated.
 */
public class FixedValueGenerator<T> implements ValueGenerator<T> {
    private final T fixedValue;

    public FixedValueGenerator(T fixedValue) {
        this.fixedValue = fixedValue;
    }

    @Override
    public T generateValue() {
        return fixedValue;
    }
}
