package com.idansh.engine.objects;

import com.idansh.engine.helpers.Counter;
import com.idansh.engine.property.objects.Property;

import java.util.Map;

/**
 * Factory class for creating instances of entities.
 * Using the Factory Method Design Pattern.
 */
public class EntityFactory {
    private final String name;                        // Unique name for this type of entity creation, e.g. "Smoker"
    private final Counter amount = new Counter();     // Amount of entities of this type in the environment
    private final Map<String, Property> properties;   // Properties that define this entity, the value of which will be assigned on instance creation


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
     * The properties' values will be defined randomly or by the values that were set.
     * @return a new instance of this entity.
     */
    public Entity createEntity() {
        amount.addCount();
        // todo- assign the properties' values - random or constants
        return new Entity(name, amount, properties);
    }
}
