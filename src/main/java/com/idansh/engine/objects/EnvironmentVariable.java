package com.idansh.engine.objects;

import com.idansh.engine.helpers.Range;

public class EnvironmentVariable<T> {
    T value;
    Range range;

    public EnvironmentVariable(T value, int from, int to) {
       this.value = value;
       this.range = new Range(from, to);
    }

    public T getValue() {
        return value;
    }

    public Range getRange() {
        return range;
    }
}
