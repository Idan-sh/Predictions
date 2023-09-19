package com.idansh.engine.entity;


import com.idansh.engine.helpers.Counter;
import com.idansh.engine.property.instance.Property;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines an instance of an entity created by an EntityFactory
 */
public class Entity {
    private final String name;                  // The name of this entity's type (e.g. "Smoker")
    private final Counter populationCounter;    // The amount of entities of this type currently in the world
    private final Map<String, Property> properties;  // Properties that define this entity, key is a unique name
    private boolean isAlive;
    private boolean createAnotherFromScratch;
    private boolean createAnotherDerived;
    private String entityNameToCreate;

    public Entity(String name, Counter populationCounter) {
        this.populationCounter = populationCounter;
        this.name = name;
        this.properties = new HashMap<>();
        this.isAlive = true;
        this.createAnotherFromScratch = false;
        this.createAnotherDerived = false;
    }

    public String getName() {
        return name;
    }


    /**
     * Finds and returns a property of the entity.
     * @param name The name of the property (which is unique to one property)
     * @return Returns the property with the provided name, or null if the property was not found
     */
    public Property getPropertyByName(String name) {
        if(!properties.containsKey(name))
            throw new IllegalArgumentException("Error: the property with the given name \"" + name + "\" does not exist!");

        return properties.get(name);
    }


    /**
     * Adds a new property to the entity factory.
     * @param property Must have a unique name, which isn't one of the existing properties.
     */
    public void addProperty(Property property) {
        properties.put(property.getName(), property);
    }


    /**
     * Sets the entity to be killed.
     * On the next simulation tick this entity needs to be removed from the population.
     */
    public void kill() {
        isAlive = false;
    }


    /**
     * Sets the entity to be killed, and sets that another
     * entity instance will be created from it.
     * @param entityName Name of the entity of which the new instance will be created.
     * @param isFromScratch if true will create a completely new instance of the entity,
     *                      otherwise will create a new instance that is derived from
     *                      this entity with some old properties used in the new entity.
     */
    public void replace(String entityName, boolean isFromScratch) {
        this.isAlive = false; // this entity should be killed
        this.entityNameToCreate = entityName;

        if(isFromScratch)
            this.createAnotherFromScratch = true;
        else
            this.createAnotherDerived = true;
    }


    /**
     * Check if the value has changed in each property of the entity.
     */
    public void checkPropertiesValueChange() {
        for (Property property : properties.values()) {
            property.checkValueChange();
        }
    }


    public boolean isToReplace() {
        return createAnotherDerived || createAnotherFromScratch;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isCreateAnotherFromScratch() {
        return createAnotherFromScratch;
    }

    public boolean isCreateAnotherDerived() {
        return createAnotherDerived;
    }

    public String getEntityNameToCreate() {
        return entityNameToCreate;
    }
}
