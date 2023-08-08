package com.idansh.engine.objects;


import com.idansh.engine.helpers.Counter;
import com.idansh.engine.property.instance.Property;

import java.util.Map;

/**
 * Defines an instance of an entity created by an EntityFactory
 */
public class Entity{
    private final String name;        // The name of this entity's type
    private final Counter amount;     // The amount of entities of this type currently in the world
    private final Map<String, Property> properties;  // Properties that define this entity, key is a unique name

    public Entity(String name, Counter amount, Map<String, Property> properties) {
        this.amount = amount;
        this.name = name;
        this.properties = properties;
    }
}
