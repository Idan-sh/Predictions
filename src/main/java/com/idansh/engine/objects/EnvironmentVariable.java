package com.idansh.engine.objects;

import com.idansh.engine.objects.helpers.HasUniqueName;
import com.idansh.engine.objects.helpers.Range;

import java.util.LinkedList;
import java.util.List;

public class EnvironmentVariable<T> extends HasUniqueName {
    static List<String> namesPool = new LinkedList<>();
    T value;
    Range range;

    public EnvironmentVariable(String name) {
        super(name, namesPool);
    }
}
