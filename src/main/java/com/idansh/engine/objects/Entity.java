package com.idansh.engine.objects;


import com.idansh.engine.property.Property;

import java.util.Map;

/**
 * Defines an instance of an entity created by an EntityFactory
 */
public class Entity{
    String name;
    private Map<String, Property> properties;  // Properties that define this entity, key is a unique name

    public Entity(String name, Map<String, Property> properties) {
        this.name = name;
        this.properties = properties;
    }
}
