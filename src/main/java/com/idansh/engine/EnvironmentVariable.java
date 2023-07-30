package com.idansh.engine;

public class EnvironmentVariable<T> extends HasUniqueName{
    Object value;
    T type;
    Range range;

    public EnvironmentVariable(String name) {
        super(name);
    }
}
