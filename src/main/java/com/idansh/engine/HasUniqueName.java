package com.idansh.engine;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Basic engine component.
 *
 */
public abstract class HasUniqueName {
    static List<String> namesPool;
    String name = null;     // Each engine component class must have a unique name

    static {
        namesPool = new LinkedList<>();
    }

    public HasUniqueName(String name) {
        if(namesPool.contains(name))
            throw new RuntimeException("Invalid Name: name already exists");

        this.name = name;
        namesPool.add(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
