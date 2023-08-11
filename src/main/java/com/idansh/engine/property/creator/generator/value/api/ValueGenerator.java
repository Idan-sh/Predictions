package com.idansh.engine.property.creator.generator.value.api;

/**
 * Generates a value of the type T, to avoid using generic type in the property class.
 * @param <T> The type of the value to generate, can be int, float, boolean, string
 */
public interface ValueGenerator<T> {
    T generateValue();
}
