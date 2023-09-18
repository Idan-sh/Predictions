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
                        population.add(factory.createEntityFromScratch());
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
     * @param name Name of the entity factory to check if exists.
     * @return true if exists, false otherwise.
     */
    public boolean isEntityFactoryValid(String name) {
        if(name == null) return true;

        return entityFactories.containsKey(name);
    }


    /**
     * Returns an entity in the population with the given name.
     */
    public Entity getEntityInPopulationByName(String name) {
        // Find an entity with the given name in the population
        Optional<Entity> retEntity =
                population.stream().filter(e -> e.getName().equals(name)).findAny();

        return retEntity.orElse(null);
    }


    /**
     * Returns an entity in the population in the given index.
     */
    public Entity getEntityInPopulationByIndex(int ind) {
        if(ind < 0 || ind > population.size() - 1)
            throw new IndexOutOfBoundsException("Out of bounds index given, cannot get entity in population in index of \"" + ind + "\".");

        return population.get(ind);
    }


    /**
     * Kills a single entity from the population.
     * @param entityToKill an entity instance in the population to kill.
     */
    public void killEntity(Entity entityToKill) {
        if(!population.contains(entityToKill))
            return;

        entityToKill.kill();
    }


    /**
     * Kills and replaces a single entity from the population.
     * @param entityToReplace an entity instance in the population to replace.
     * @param isFromScratch if true will create a completely new instance of the entity,
     *                      otherwise will create a new instance that is derived from
     *                      this entity with some old properties used in the new entity.
     */
    public void replaceEntity(Entity entityToReplace, String entityName, boolean isFromScratch) {
        if(!population.contains(entityToReplace))
            return;

        entityToReplace.replace(entityName, isFromScratch);
    }


    /**
     * Creates a new entity and adds it to the population.
     * @param entityToCreate Name of the entity factory of which an instance
     *                       will be created and added into the population.
     * @throws IllegalArgumentException In case an entity factory with the given name doesn't exist.
     */
    public void createEntityFromScratch(String entityToCreate) {
        System.out.println("creating from scratch the entity " + entityToCreate);

        population.add(getEntityFactory(entityToCreate).createEntityFromScratch());
    }


    /**
     * Creates a new entity from an existing entity and adds it to the population.
     * @param entityToCreateFrom Entity instance of which the new instance will be created.
     * @param entityToCreate Name of the entity factory of which an instance
     *                       will be created and added into the population.
     * @throws IllegalArgumentException In case an entity factory with the given name doesn't exist.
     */
    public void createEntityDerived(Entity entityToCreateFrom, String entityToCreate) {
        population.add(getEntityFactory(entityToCreate).createEntityDerived(entityToCreateFrom));
    }


    /**
     * Creates a list of all entity instances that live in the population with a given name.
     */
    public List<Entity> getAllEntityInstancesInPopulation(String entityName) {
        List<Entity> retList = new ArrayList<>();

        for (Entity entity : population) {
            if(entity.getName().equals(entityName))
                retList.add(entity);
        }

        return retList;
    }


    /**
     * Removes all dead entities from the population,
     * and decreases the population counter for the dead entity instance's main entity.
     * Also replaces some entities that were set up from replacement.
     */
    public void removeDeadEntitiesFromPopulation() {
        for (Entity entity : population) {
            // Check if the entity is set to be killed
            if (!entity.isAlive()) {
                // Check if the entity is set to be replaced
                if (entity.isToReplace()) {
                    System.out.println("replacing entity");
                    if(entity.isCreateAnotherFromScratch()) {
                        System.out.println("replacing from scratch");
                        createEntityFromScratch(entity.getEntityNameToCreate());
                    }
                    else {
                        System.out.println("replacing derived");
                        createEntityDerived(entity, entity.getEntityNameToCreate());
                    }
                    entityFactories.get(entity.getEntityNameToCreate()).increasePopulationCounter();
                }
                // Kill the entity
                entityFactories.get(entity.getName()).decreasePopulationCounter();
                population.remove(entity);
            }
        }
    }


    public Map<String, EntityFactory> getEntityFactories() {
        return entityFactories;
    }

    public List<Entity> getPopulation() {
        return population;
    }

    public int getPopulationSize() {
        return population.size();
    }
}
