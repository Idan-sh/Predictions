package com.idansh.engine;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Basic engine component.
 *
 */
public abstract class HasUniqueName {
    String name;

    public HasUniqueName(String name, List<String> namesPool) {
        if(namesPool.contains(name))
            throw new RuntimeException("Invalid Name: name already exists");

        this.name = name;
        namesPool.add(name);
    }

    public String getName() {
        return name;
    }
}
