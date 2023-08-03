package com.idansh.engine.objects;


import com.idansh.engine.property.Property;

import java.util.Map;

public class Entity {
    private int amount;             // Amount of entities of this type in the environment
    private Map<String, Property> properties;  // Properties that define this entity, key is a unique name

    public Entity() {
    }
}
