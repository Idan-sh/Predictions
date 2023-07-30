package com.idansh.engine.enviornment;

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

    @Override
    public String toString() {
        return "Expression{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HasUniqueName that = (HasUniqueName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
