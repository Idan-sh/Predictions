package com.idansh.engine.objects;

import com.idansh.engine.property.Property;

import java.util.Map;

/**
 * Factory class for creating instances of entities.
 * Using the Factory Method Design Pattern.
 */
public class EntityFactory {
    String name = null;                    // e.g. Smoker
    private int amount = 0;             // Amount of entities of this type in the environment

    // todo- check if properties should be entity-instance specific, or entity-type specific
    private Map<String, Property> properties = null;  // Properties that define this entity, key is a unique name


    /**
     * Constructor that defines the properties of new instances' information.
     * @param name Unique name of the new type of entity
     * @param properties the properties/information of each entity
     */
    public EntityFactory(String name, Map<String, Property> properties) {
        this.name = name;
        this.properties = properties;
    }


    /**
     * Factory Method for creating an instance of this entity according to the properties defined.
     * @return a new instance of this entity.
     */
    public Entity createEntity() {
        amount++;
        return new Entity(name, properties);
    }
}
