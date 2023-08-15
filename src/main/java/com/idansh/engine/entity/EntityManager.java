package com.idansh.engine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            throw new IllegalArgumentException("Error: could not find the received entity factory!");

        return entityFactories.get(name);
    }


    public Entity getEntityInPopulation(String name) {
        AtomicReference<Entity> retEntity = new AtomicReference<>();

        population.forEach(
                e -> {
                    if(e.getName().equals(name))
                        retEntity.set(e);
                }
        );

        if(retEntity.get() == null)
            throw new IllegalArgumentException("Error: entity name \"" + name + "\" does not exist in the population!");

        return retEntity.get();
    }


    /**
     * Kills a single entity of the given ID from the population.
     * @param id ID of the entity to kill.
     */
    public void killEntity(int id) {
        // todo- implement kill
    }

}
