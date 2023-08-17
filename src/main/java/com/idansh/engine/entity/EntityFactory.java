package com.idansh.engine.entity;

import com.idansh.engine.helpers.Counter;
import com.idansh.engine.property.creator.factory.PropertyFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating instances of entities.
 * Each EntityFactory has a unique name, a counter for the amount of entities of its type and
 * the properties that will be assigned to entity instances that will be created using the factory method.
 * Using the Factory Method Design Pattern.
 */
public class EntityFactory {
    private final String name;        // Unique name for this type of entity creation, e.g. "Smoker"
    private final Counter populationCounter;     // Amount of entities of this type in the environment
    private final Map<String, PropertyFactory> propertiesToAssign;   // Properties that define this entity, the value of which will be assigned on instance creation


    /**
     * Constructor that defines the properties of new instances' information.
     * @param name Unique name of the new type of entity.
     * @param initPopulation The initial population amount of this entity.
     */
    public EntityFactory(String name, int initPopulation) {
        this.name = name;
        this.propertiesToAssign = new HashMap<>();
        this.populationCounter = new Counter(initPopulation);
    }

    public String getName() {
        return name;
    }

    public int getPopulationCount() {
        return populationCounter.getCount();
    }


    public PropertyFactory getPropertyFactory(String name) {
        if(!propertiesToAssign.containsKey(name))
            throw new IllegalArgumentException("Error: the property factory with the given name does not exist!");

        return propertiesToAssign.get(name);
    }


    /**
     * Factory Method for creating a single instance of this entity according to the properties defined.
     * The properties' values will be defined randomly or by the fixed values that were set.
     * @return a new instance of this entity.
     */
    public Entity createEntity() {
        Entity entityInstance = new Entity(name, populationCounter);

        // Iterate through all the properties to assign
        propertiesToAssign.forEach(
                (key, value) -> entityInstance.addProperty(value.createProperty()));  // Create a property instance from the corresponding property creator

        return entityInstance;
    }


    /**
     * Adds a new property factory to the map of properties to assign.
     * @param propertyFactory property factory to add.
     */
    public void addProperty(PropertyFactory propertyFactory) {
        propertiesToAssign.put(propertyFactory.getName(), propertyFactory);
    }

    public Map<String, PropertyFactory> getPropertiesToAssign() {
        return propertiesToAssign;
    }
}
