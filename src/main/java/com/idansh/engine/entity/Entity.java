package com.idansh.engine.entity;


import com.idansh.engine.helpers.Counter;
import com.idansh.engine.property.instance.Property;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines an instance of an entity created by an EntityFactory
 */
public class Entity {
    private final String name;        // The name of this entity's type
    private final Counter populationCounter;     // The amount of entities of this type currently in the world
    private final Map<String, Property> properties;  // Properties that define this entity, key is a unique name

    public Entity(String name, Counter populationCounter) {
        this.populationCounter = populationCounter;
        this.name = name;
        this.properties = new HashMap<>();
    }

    /**
     * Finds and returns a property of the entity.
     * @param name The name of the property (which is unique to one property)
     * @return Returns the property with the provided name, or null if the property was not found
     */
    public Property getPropertyByName(String name) {
        return properties.get(name);
    }


    /**
     * Adds a new property to the entity factory.
     * @param property Must to have a unique name, which isn't one of the existing properties.
     */
    public void addProperty(Property property) {
        properties.put(property.getName(), property);
    }
}
