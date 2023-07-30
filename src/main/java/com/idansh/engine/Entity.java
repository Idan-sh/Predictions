package com.idansh.engine;


import java.util.List;

public class Entity extends HasUniqueName {
    private Integer amount;             // Amount of entities of this type in the environment
    private List<Property<?>> properties;  // Properties that define this entity

    public Entity(String name) {
        super(name);
    }
}
