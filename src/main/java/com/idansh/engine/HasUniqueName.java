package com.idansh.engine;

import java.util.Objects;

/**
 * Basic engine component.
 *
 */
public abstract class HasUniqueName {
    String name = null;     // Each engine component class must have a unique name

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
