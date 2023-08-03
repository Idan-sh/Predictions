package com.idansh.engine.objects;

import com.idansh.engine.helpers.Counter;
import com.idansh.engine.property.creator.PropertyFactory;
import com.idansh.engine.property.objects.Property;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating instances of entities.
 * Using the Factory Method Design Pattern.
 */
public class EntityFactory {
    private final String name;                        // Unique name for this type of entity creation, e.g. "Smoker"
    private final Counter amount = new Counter();     // Amount of entities of this type in the environment
    private final Map<String, PropertyFactory> propertiesToAssign;   // Properties that define this entity, the value of which will be assigned on instance creation


    /**
     * Constructor that defines the properties of new instances' information.
     * @param name Unique name of the new type of entity
     * @param propertiesToAssign property instructions on how to create new properties
     *                           that will be assigned to the newly created entity.
     */
    public EntityFactory(String name, Map<String, PropertyFactory> propertiesToAssign) {
        this.name = name;
        this.propertiesToAssign = propertiesToAssign;
    }


    /**
     * Factory Method for creating an instance of this entity according to the properties defined.
     * The properties' values will be defined randomly or by the values that were set.
     * @return a new instance of this entity.
     */
    public Entity createEntity() {
        amount.addCount();
        // todo- assign the properties' values - random or constants
        Map<String, Property> assignedProperties = new HashMap<>();

        // Iterate through all the properties to assign
        propertiesToAssign.forEach(
                (key, value) -> assignedProperties.put(key, value.createProperty()));  // Create a property instance from the corresponding property creator

        return new Entity(name, amount, assignedProperties);
    }
}
