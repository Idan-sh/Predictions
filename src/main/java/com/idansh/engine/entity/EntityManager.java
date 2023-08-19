package com.idansh.engine.entity;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class EntityManager {
    private final Map<String, EntityFactory> entityFactories;   // Each entity factory will define instructions on how to instantiate a single entity with a unique name
    private final List<Entity> population;


    public EntityManager() {
        this.entityFactories = new HashMap<>();
        this.population = new ArrayList<>();
    }


    /**
     * Adds a new EntityFactory to the simulated world, from which entities will be created.
     */
    public void addEntityFactory(EntityFactory entityFactory) {
        if(entityFactories.containsKey(entityFactory.getName()))
            throw new IllegalArgumentException("Error: received entityFactory's name already exists!");

        entityFactories.put(entityFactory.getName(), entityFactory);
    }


    /**
     * Initializes the population of the world with entities using the entity factories.
     */
    public void initEntityPopulation() {
        // Run through all the entity factories, on each factory create populationCount instances.
        entityFactories.forEach(
                (name , factory) -> {
                    for (int i = 0; i < factory.getPopulationCount(); i++) {
                        population.add(factory.createEntity());
                    }
                }
        );
    }


    /**
     * Returns reference to an existing entity factory.
     * @param name The name of the factory to search
     */
    public EntityFactory getEntityFactory(String name) {
        if(!entityFactories.containsKey(name))
            throw new IllegalArgumentException("could not find an entity factory with the name \"" + name + "\"!");

        return entityFactories.get(name);
    }


    /**
     * Returns an entity in the population with the given name.
     */
    public Entity getEntityInPopulation(String name) {
        // Find an entity with the given name in the population
        Optional<Entity> retEntity =
                population.stream().filter(e -> e.getName().equals(name)).findAny();

        if(!retEntity.isPresent())
            throw new IllegalArgumentException("Error: entity name \"" + name + "\" does not exist in the population!");

        return retEntity.get();
    }


    /**
     * Kills a single entity of the given ID from the population.
     * @param entityToKill an entity instance in the population to kill.
     */
    public void killEntity(Entity entityToKill) {
        if(!population.contains(entityToKill))
            throw new IllegalArgumentException("Error: cannot kill entity with name " + entityToKill.getName() + ", this entity does not exist in the population!");

        // Remove the entity from the population and decrease the population counter for the main entity
        entityFactories.get(entityToKill.getName()).decreasePopulationCounter();
        population.remove(entityToKill);
    }

    public List<Entity> getPopulation() {
        return population;
    }

    public Map<String, EntityFactory> getEntityFactories() {
        return entityFactories;
    }
}
