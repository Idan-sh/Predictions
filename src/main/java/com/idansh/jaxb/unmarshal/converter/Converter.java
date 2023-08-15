package com.idansh.jaxb.unmarshal.converter;

import com.idansh.engine.actions.Action;
import com.idansh.engine.actions.DecreaseAction;
import com.idansh.engine.actions.IncreaseAction;
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
import com.idansh.engine.rule.RuleActivation;
import com.idansh.engine.rule.TerminationRule;
import com.idansh.engine.world.World;
import com.idansh.jaxb.schema.generated.*;
import org.jetbrains.annotations.NotNull;

import static com.sun.xml.internal.bind.v2.runtime.output.Encoded.entities;

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


    /**
     * Converts a PRDRule object that was read from the XML file
     * into a Rule Object.
     * @param prdRule PRDRule object that was read from the XML file.
     * @return Rule object with the data of the PRDRule received.
     */
    private static Rule ruleConvert(PRDRule prdRule) {
        Rule retRule = new Rule(
                prdRule.getName(),
                activationConvert(prdRule.getPRDActivation()));

        // Iterates over all prdActions, converts them to actions and adds them to the rule
        prdRule.getPRDActions().getPRDAction().forEach(
                a -> retRule.addAction(actionConvert(a))
        );

        return retRule;
    }


    /**
     * Converts a PRDActivation object that was read from the XML file
     * into a RuleActivation Object.
     * @param prdActivation PRDActivation object that was read from the XML file.
     * @return RuleActivation object with the data of the PRDActivation received.
     */
    private static RuleActivation activationConvert(PRDActivation prdActivation) {
        return new RuleActivation(prdActivation.getTicks(), prdActivation.getProbability());
    }


    // todo- ------------------------  finish from here!!! --------------------------

    /**
     * Converts a PRDAction object that was read from the XML file
     * into an Action Object.
     * @param prdAction PRDAction object that was read from the XML file.
     * @return Action object with the data of the PRDAction received.
     */
    private static Action actionConvert(PRDAction prdAction, World worldContext) {
        Action retAction = null;
        ExpressionConverterAndValidator expressionConverterAndValidator = new ExpressionConverterAndValidator(environmentProperties, entities);

        switch (Action.Type.valueOf(prdAction.getType())) {
            case INCREASE:
                retAction = new IncreaseAction(worldContext, prdAction.getEntity(), prdAction.getProperty(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getBy()));

            case DECREASE:
                retAction = new DecreaseAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getBy()));

            case CALCULATION:
                retAction = getMulOrDiv(prdAction, expressionConverterAndValidator);

            case CONDITION:
                retAction = getSingleOrMultiple(prdAction, expressionConverterAndValidator);

            case SET:
                retAction = new SetAction(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction,prdAction.getValue()));

            case KILL:
                retAction = new KillAction(prdAction.getProperty(), prdAction.getEntity());

//            case REPLACE:
//                break;

//            case PROXIMITY:
//                break;
            }

        return retAction;
    }


    /**
     * Converts the given PRDAction to multiply or divide calculation action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return a CalculationAction representation of the given PRDActivation.
     */
    private CalculationAction getMulOrDiv(PRDAction prdAction, ExpressionConverterAndValidator expressionConverterAndValidator){
        CalculationAction ret = null;
        PRDMultiply mul = prdAction.getPRDMultiply();
        PRDDivide div = prdAction.getPRDDivide();

        // Without loss of generality, if mul equals null - the calculation action is not a multiply action.
        if(mul != null){
            ret = new CalculationAction(prdAction.getProperty(),prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, mul.getArg1()),
                    expressionConverterAndValidator.analyzeAndGetValue(prdAction, mul.getArg2()), ClaculationType.MULTIPLY);
        } else if (div != null) {
            ret = new CalculationAction(prdAction.getProperty(),prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, div.getArg1()),
                    expressionConverterAndValidator.analyzeAndGetValue(prdAction, div.getArg2()), ClaculationType.DIVIDE);
        }
        else {
            // Throw exception.
        }
        return ret;
    }

    /**
     * Converts the given PRDAction to single or multiple condition action.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @return an AbstractConditionAction representation of the given PRDActivation.
     */
    private AbstractConditionAction getSingleOrMultiple(PRDAction prdAction, ExpressionConverterAndValidator expressionConverterAndValidator){
        AbstractConditionAction ret = null;
        PRDCondition prdCondition = prdAction.getPRDCondition();
        ThenOrElse thenActions = null, elseActions = null;
        // Then and else objects are created in this method.
        getAndCreateThenOrElse(prdAction, thenActions, elseActions);

        if(prdCondition.getSingularity().equals("single")){
            ret = new SingleCondition(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getValue()), thenActions, elseActions, prdCondition.getOperator());
        } else if (prdCondition.getSingularity().equals("multiple")) {
            ret = new MultipleCondition(prdAction.getProperty(), prdAction.getEntity(), expressionConverterAndValidator.analyzeAndGetValue(prdAction, prdAction.getValue()), thenActions, elseActions, prdCondition.getLogical());
        }
        else {
            // Throw exception.
        }

        return ret;
    }

    /**
     * Converts the given PRDAction to Then and Else objects which contain a set of actions to invoke.
     * According to the XML file, if one of them has no actions to invoke, the object remains null.
     *
     * @param prdAction the given PRDAction generated from reading the XML file
     * @param thenActions empty ThenOrElse object to be created.
     * @param elseActions empty ThenOrElse object to be created.
     */
    private void getAndCreateThenOrElse(PRDAction prdAction, ThenOrElse thenActions, ThenOrElse elseActions){
        // 'getThenOrElseActionSet' creates the Set of Actions for them both.
        // Because 'PRDThen' and 'PRDElse' are different objects, when we want to create the set for 'Then'
        // we send null for 'prdElse', same for Else.
        Set<Action> thenActionsSet = getThenOrElseActionSet(prdAction.getPRDThen(), null);
        Set<Action> elseActionsSet = getThenOrElseActionSet(null, prdAction.getPRDElse());

        if(!thenActionsSet.isEmpty()){
            thenActions = new ThenOrElse(thenActionsSet);
        }

        if(!elseActionsSet.isEmpty()){
            elseActions = new ThenOrElse(elseActionsSet);
        }
    }


    /**
     * Converts the given PRDThen or PRDElse to The set of actions to invoke.
     * For example: if prdThen equals null, the method creates the set from the prdElse.
     *
     * @param prdThen the given PRDThen generated from reading the XML file
     * @param prdElse the given PRDElse generated from reading the XML file
     * @return a Set of actions representation of the given PRDThen or PRDElse.
     */
    private Set<Action> getThenOrElseActionSet(PRDThen prdThen, PRDElse prdElse){
        Set<Action> ret = new HashSet<>();

        if(prdThen != null){
            prdThen.getPRDAction().forEach(a-> ret.add(PRDAction2Action(a)));
        } else if (prdElse != null) {
            prdElse.getPRDAction().forEach(a-> ret.add(PRDAction2Action(a)));
        }

        return ret;
    }
}