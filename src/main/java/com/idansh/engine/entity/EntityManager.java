package com.idansh.engine.entity;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityManager {
    private final Map<String, EntityFactory> entityFactories;   // Each entity factory will define instructions on how to instantiate a single entity with a unique name
    private final List<Entity> population;


    public EntityManager() {
        this.entityFactories = new HashMap<>();
        this.population = new ArrayList<>(); // Note: using a thread-safe collection that can handle concurrent modifications and iterations (we try to kill entity instances while iterating on the population list)
    }

    public EntityManager(EntityManager entityManager) {
        this.entityFactories = new HashMap<>();
        this.population = new CopyOnWriteArrayList<>();

        entityManager.getEntityFactories().forEach(
                (name, entityFactory) -> this.entityFactories.put(name, new EntityFactory(entityFactory))
        );
    }


    /**
     * Adds a new EntityFactory to the simulated world, from which entities will be created.
     */
    public void addEntityFactory(EntityFactory entityFactory) {
        if(entityFactories.containsKey(entityFactory.getName()))
            throw new IllegalArgumentException("received entityFactory's name \"" + entityFactory.getName() + "\" already exists!");

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
     * @param name The name of the entity factory to search.
     * @throws IllegalArgumentException In case an entity factory with the given name doesn't exist.
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

        return retEntity.orElse(null);
    }


    /**
     * Kills a single entity of the given ID from the population.
     * @param entityToKill an entity instance in the population to kill.
     */
    public void killEntity(Entity entityToKill) {
        if(!population.contains(entityToKill))
            return;

        entityToKill.kill();
    }


    /**
     * Creates a new entity and adds it to the population.
     * @param entityToCreate Name of the entity factory of which an instance
     *                       will be created and added into the population.
     * @throws IllegalArgumentException In case an entity factory with the given name doesn't exist.
     */
    public void createEntity(String entityToCreate) {
        population.add(getEntityFactory(entityToCreate).createEntity());
    }

    public List<Entity> getPopulation() {
        return population;
    }

    public Map<String, EntityFactory> getEntityFactories() {
        return entityFactories;
    }

    /**
     * Removes all dead entities from the population,
     * and decreases the population counter for the dead entity instance's main entity.
     */
    public void removeDeadEntitiesFromPopulation() {
        for (Entity entity : population) {
            if (!entity.isAlive()) {
                entityFactories.get(entity.getName()).decreasePopulationCounter();
                population.remove(entity);
            }
        }
    }
}
