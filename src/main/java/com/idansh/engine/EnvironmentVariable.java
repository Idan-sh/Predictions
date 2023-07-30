package com.idansh.engine;

import java.util.LinkedList;
import java.util.List;

public class EnvironmentVariable<T> extends HasUniqueName{
    static List<String> namesPool = new LinkedList<>();
    T value;
    Range range;

    public EnvironmentVariable(String name) {
        super(name, namesPool);
    }
}
