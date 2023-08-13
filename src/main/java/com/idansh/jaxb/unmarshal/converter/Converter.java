package com.idansh.jaxb.unmarshal.converter;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.creator.factory.PropertyCreator;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.creator.generator.value.fixed.FixedValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomBooleanValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomFloatValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomIntegerValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomStringValueGenerator;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.rule.Rule;
import com.idansh.engine.rule.TerminationRule;
import com.idansh.engine.world.World;
import com.idansh.jaxb.schema.generated.PRDEntity;
import com.idansh.jaxb.schema.generated.PRDProperty;
import com.idansh.jaxb.schema.generated.PRDWorld;

import java.util.HashMap;
import java.util.Map;

/**
 * A static class with methods to convert generated data from the XML scheme to the simulation's objects.
 */
public class Converter {

    /**
     * Converts a PRDWorld object that was read from the XML file
     * into a World Object.
     * @param prdWorld PRDWorld object that was read from the XML file.
     * @return World object with the data of the PRDWorld received.
     */
    public static World worldConvert(PRDWorld prdWorld) {
        World retWorld = new World();

        // todo - finish env properties
        // Iterates over all PRDEnvironmentProperties, converts each property and adds it to 'environmentProperties'
        prdWorld.getPRDEvironment().getPRDEnvProperty().forEach(
                p -> environmentProperties.put(p.getPRDName(), PRDEnvProperty2Property(p))
        );

        // Iterates over all PRDEntities, converts each entity and adds it to 'entities'
        prdWorld.getPRDEntities().getPRDEntity().forEach(
                e -> retWorld.entityManager.addEntityFactory(entityConvert(e))
        );

        // todo- finish rules
        // Iterates over all PRDRules, converts each rule and adds it to 'rules'
        prdWorld.getPRDRules().getPRDRule().forEach(r -> rules.put(r.getName(), PRDRule2Rule(r)));

        // todo- finish termination rules
        // some termination rule code

        return retWorld;
    }


    /**
     * Converts a PRDEntity object that was read from the XML file
     * into an EntityFactory Object.
     * @param prdEntity PRDEntity object that was read from the XML file.
     * @return EntityFactory object with the data of the PRDEntity received.
     */
    private static EntityFactory entityConvert(PRDEntity prdEntity) {
        EntityFactory entityFactory = new EntityFactory(prdEntity.getName(), prdEntity.getPRDPopulation());

        prdEntity.getPRDProperties().getPRDProperty().forEach(
                prdProperty -> entityFactory.addProperty(propertyConvert(prdProperty))
        );

        return entityFactory;
    }


    /**
     * Converts a PRDProperty object that was read from the XML file
     * into an PropertyFactory Object.
     * @param prdProperty PRDProperty object that was read from the XML file.
     * @return PropertyFactory object with the data of the PRDProperty received.
     */
    private static PropertyFactory propertyConvert(PRDProperty prdProperty) {
        PropertyFactory retPropertyFactory = null;
        boolean isRandom = prdProperty.getPRDValue().isRandomInitialize();

        // Create the property according to its type:
        // PropertyType.getType handles errors with the prdProperty's type
        switch(PropertyType.getType(prdProperty.getType())) {
            case INTEGER:
                retPropertyFactory = new PropertyCreator<>(prdProperty.getPRDName(), PropertyType.INTEGER,
                        isRandom ? new RandomIntegerValueGenerator(new Range(prdProperty.getPRDRange().getFrom(), prdProperty.getPRDRange().getTo())) : new FixedValueGenerator<>(Integer.parseInt(prdProperty.getPRDValue().getInit())));
                break;

            case FLOAT:
                retPropertyFactory = new PropertyCreator<>(prdProperty.getPRDName(), PropertyType.FLOAT,
                        isRandom ? new RandomFloatValueGenerator(new Range(prdProperty.getPRDRange().getFrom(), prdProperty.getPRDRange().getTo())) : new FixedValueGenerator<>(Float.parseFloat(prdProperty.getPRDValue().getInit())));
                break;

            case BOOLEAN:
                retPropertyFactory = new PropertyCreator<>(prdProperty.getPRDName(), PropertyType.BOOLEAN,
                        isRandom ? new RandomBooleanValueGenerator() : new FixedValueGenerator<>(Boolean.parseBoolean(prdProperty.getPRDValue().getInit())));
                break;

            case STRING:
                retPropertyFactory = new PropertyCreator<>(prdProperty.getPRDName(), PropertyType.STRING,
                        isRandom ? new RandomStringValueGenerator() : new FixedValueGenerator<>(prdProperty.getPRDValue().getInit()));
                break;
        }

        return retPropertyFactory;
    }
}