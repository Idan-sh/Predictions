package com.idansh.jaxb.unmarshal.converter;

import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.creator.factory.PropertyCreator;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;
import com.idansh.engine.property.creator.generator.value.fixed.FixedValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomBooleanValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomFloatValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomIntegerValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomStringValueGenerator;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.rule.Rule;
import com.idansh.engine.rule.TerminationRule;
import com.idansh.engine.world.World;
import com.idansh.jaxb.schema.generated.*;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    public static World worldConvert(PRDWorld prdWorld) {
        World retWorld = new World();

        // Iterates over all PRDEnvironmentProperties, converts each property and adds it to the world
        prdWorld.getPRDEvironment().getPRDEnvProperty().forEach(
                p -> retWorld.addEnvironmentVariableFactory(environmentVariableConvert(p))
        );

        // Iterates over all PRDEntities, converts each entity and adds it to the world
        prdWorld.getPRDEntities().getPRDEntity().forEach(
                e -> retWorld.entityManager.addEntityFactory(entityConvert(e))
        );

        // Iterates over all PRDRules, converts each rule and adds it to the world
        prdWorld.getPRDRules().getPRDRule().forEach(
                r -> retWorld.addRule(ruleConvert(r))
        );

        // Iterate over all PRDTerminations, converts each termination rule and adds it to the world
        prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond().forEach(
                t -> retWorld.addTerminationRule(terminationRuleConvert(t))
        );

        return retWorld;
    }


    /**
     * Converts a PRDEntity object that was read from the XML file
     * into an EntityFactory Object.
     * @param prdEntity PRDEntity object that was read from the XML file.
     * @return EntityFactory object with the data of the PRDEntity received.
     */
    private static EntityFactory entityConvert(PRDEntity prdEntity) {
        EntityFactory retEntityFactory = new EntityFactory(prdEntity.getName(), prdEntity.getPRDPopulation());

        prdEntity.getPRDProperties().getPRDProperty().forEach(
                prdProperty -> retEntityFactory.addProperty(propertyConvert(prdProperty))
        );

        return retEntityFactory;
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


    /**
     * Converts a PRDBySecond/PRDByTicks object that was read from the XML file
     * into a TerminationRule Object.
     * @param prdTermination PRDBySecond/PRDByTicks object that was read from the XML file.
     * @return TerminationRule object with the data of the PRDBySecond/PRDByTicks received.
     */
    private static TerminationRule terminationRuleConvert(Object prdTermination) {
        TerminationRule retTerminationRule;

        if(prdTermination.getClass().equals(PRDByTicks.class))
            retTerminationRule = new TerminationRule(TerminationRule.Type.TICKS, ((PRDByTicks) prdTermination).getCount());
        else if (prdTermination.getClass().equals(PRDBySecond.class))
            retTerminationRule = new TerminationRule(TerminationRule.Type.SECONDS, ((PRDBySecond) prdTermination).getCount());
        else
            throw new IllegalArgumentException("Error: prdTermination received is not of type" +
                    "PRDByTicks or PRDBySecond! (the type is- " + prdTermination.getClass() + ")");

        return retTerminationRule;
    }


    /**
     * Converts a PRDRule object that was read from the XML file
     * into a Rule Object.
     * @param prdRule PRDRule object that was read from the XML file.
     * @return Rule object with the data of the PRDRule received.
     */
    private static Rule ruleConvert(PRDRule prdRule) {
        Rule retRule;

        // todo- implement ruleConvert

        return retRule;
    }


    /**
     * Converts a PRDEnvProperty object that was read from the XML file
     * into a PropertyFactory Object.
     * @param prdEnvProperty PRDEnvProperty object that was read from the XML file.
     * @return PropertyFactory object with the data of the PRDEnvProperty received.
     */
    private static PropertyFactory environmentVariableConvert(PRDEnvProperty prdEnvProperty) {
        PropertyFactory retPropertyFactory;
        PropertyType envVarType = PropertyType.getType(prdEnvProperty.getType());
        ValueGenerator<?> valueGenerator = null;

        // Create value generator according to the environment variable's type
        switch(envVarType) {
            case INTEGER:
                valueGenerator = new RandomIntegerValueGenerator(rangeConvert(prdEnvProperty.getPRDRange()));
                break;

            case FLOAT:
                valueGenerator = new RandomFloatValueGenerator(rangeConvert(prdEnvProperty.getPRDRange()));
                break;

            case BOOLEAN:
                valueGenerator = new RandomBooleanValueGenerator();
                break;

            case STRING:
                valueGenerator = new RandomStringValueGenerator();
                break;
        }

        retPropertyFactory = new PropertyCreator<>(
                prdEnvProperty.getPRDName(),
                envVarType,
                valueGenerator);

        return retPropertyFactory;
    }


    /**
     * Converts a PRDRange object that was read from the XML file
     * into a Range Object.
     * @param prdRange PRDRange object that was read from the XML file.
     * @return Range object with the data of the PRDRange received.
     */
    private static Range rangeConvert(PRDRange prdRange) {
        return new Range(prdRange.getFrom(), prdRange.getTo());
    }
}