package com.idansh.engine.entity;

import com.idansh.engine.helpers.Counter;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.instance.Property;

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
    private Counter populationCounter;     // Amount of entities of this type in the environment
    private int initPopulation;
    private final Map<String, PropertyFactory> propertiesToAssign;   // Properties that define this entity, the value of which will be assigned on instance creation


    /**
     * Constructor that defines the properties of new instances' information.
     * @param name Unique name of the new type of entity.
     */
    public EntityFactory(String name) {
        this.name = name;
        this.propertiesToAssign = new HashMap<>();
    }

    public EntityFactory(EntityFactory entityFactory) {
        this.name = entityFactory.getName();
        this.populationCounter = new Counter(entityFactory.getPopulationCount());
        this.initPopulation = entityFactory.getInitPopulation();
        this.propertiesToAssign = new HashMap<>();

        entityFactory.getPropertiesToAssign().forEach(
                (name, propertyFactory) -> this.propertiesToAssign.put(name, propertyFactory.copy())
        );
    }

    public String getName() {
        return name;
    }


    /**
     * @return the current amount of entity instances of this entity factory.
     */
    public int getPopulationCount() {
        return populationCounter.getCount();
    }


    /**
     * @param name name of a property factory to get.
     * @return a property factory that defines this entity factory.
     */
    public PropertyFactory getPropertyFactory(String name) {
        if(!propertiesToAssign.containsKey(name))
            throw new IllegalArgumentException("the property factory with the given name \"" + name + "\" does not exist!");

        return propertiesToAssign.get(name);
    }


    /**
     * Factory Method for creating a single instance of this entity according to the properties defined.
     * The properties' values will be defined randomly or by the fixed values that were set.
     * @return a newly created instance of this entity.
     */
    public Entity createEntityFromScratch() {
        Entity entityInstance = new Entity(name, populationCounter);

        // Iterate through all the properties to assign
        propertiesToAssign.forEach(
                (key, value) -> entityInstance.addProperty(value.createProperty()));  // Create a property instance from the corresponding property creator

        return entityInstance;
    }


    /**
     * Factory Method for creating a single instance of this entity according to the properties defined,
     * and the properties of the entity to create from.
     * The properties' values will be defined randomly or by the fixed values that were set.
     * @param entityToCreateFrom Entity instance that its properties will be used in the entity creation process.
     *                           Only properties that exist in the entity factory will be used.
     * @return a newly created instance of this entity.
     */
    public Entity createEntityDerived(Entity entityToCreateFrom) {
        Entity entityInstance = new Entity(name, populationCounter);

        // Iterate through all the properties to assign
        propertiesToAssign.forEach(
                (key, value) -> {
                    // Try to get the property with the name in Key.
                    // If exists then add it to the entity instance, otherwise add a property from this entity factory default values
                    try {
                        Property propertyToAdd = entityToCreateFrom.getPropertyByName(key);
                        entityInstance.addProperty(propertyToAdd);
                    } catch (IllegalArgumentException ignored) {
                        // The entity to create from does not have this property,
                        // Create the property instance from the corresponding property creator
                        entityInstance.addProperty(value.createProperty());
                    }
                });

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


    /**
     * Increases the counter for the amount of entity instances in the population of this entity factory.
     */
    public void increasePopulationCounter() {
        populationCounter.increaseCount();
    }


    /**
     * Decreases the counter for the amount of entity instances in the population of this entity factory.
     */
    public void decreasePopulationCounter() {
        populationCounter.decreaseCount();
    }


    /**
     * @return the initial amount of entity instances created in the population of this entity factory.
     */
    public int getInitPopulation() {
        return initPopulation;
    }

    public void setInitPopulation(int initPopulation) {
        this.initPopulation = initPopulation;
        this.populationCounter = new Counter(initPopulation);
    }
}
