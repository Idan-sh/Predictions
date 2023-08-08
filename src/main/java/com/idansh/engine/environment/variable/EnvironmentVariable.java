package com.idansh.engine.environment.variable;

import com.idansh.engine.helpers.Range;

public class EnvironmentVariable<T> {
    String name;
    T value;
    Range range;

    public EnvironmentVariable(String name, T value, int from, int to) {
       this.name = name;
       this.value = value;
       this.range = new Range(from, to);
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public Range getRange() {
        return range;
    }
}
