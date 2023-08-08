package com.idansh.engine.entity;


import com.idansh.engine.helpers.Counter;
import com.idansh.engine.property.instance.Property;

import java.util.Map;

/**
 * Defines an instance of an entity created by an EntityFactory
 */
public class Entity{
    private final String name;        // The name of this entity's type
    private final Counter populationCounter;     // The amount of entities of this type currently in the world
    private final Map<String, Property> properties;  // Properties that define this entity, key is a unique name

    public Entity(String name, Counter populationCounter, Map<String, Property> properties) {
        this.populationCounter = populationCounter;
        this.name = name;
        this.properties = properties;
    }
}
