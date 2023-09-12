package com.idansh.engine.jaxb.unmarshal.converter;

import com.idansh.engine.actions.*;
import com.idansh.engine.actions.condition.ConditionAction;
import com.idansh.engine.actions.condition.MultiConditionAction;
import com.idansh.engine.actions.condition.SingleConditionAction;
import com.idansh.engine.actions.condition.ThenOrElseActions;
import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.jaxb.schema.generated.*;
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

import java.util.List;

/**
 * An abstract class with static methods to convert generated data from the XML scheme to the simulation's objects.
 */
public abstract class Converter {
    /**
     * Converts a PRDWorld object that was read from the XML file
     * into a World Object.
     * @param prdWorld PRDWorld object that was read from the XML file.
     * @return World object with the data of the PRDWorld received.
     */
    public static World worldConvert(PRDWorld prdWorld) {
        World retWorld = new World();

        // Iterates over all PRDEnvironmentProperties, converts each property and adds it to the world
        prdWorld.getPRDEnvironment().getPRDEnvProperty().forEach(
                p -> retWorld.addEnvironmentVariableFactory(environmentVariableConvert(p))
        );

        retWorld.initEnvironmentVariables();

        // Iterates over all PRDEntities, converts each entity and adds it to the world
        prdWorld.getPRDEntities().getPRDEntity().forEach(
                e -> retWorld.entityManager.addEntityFactory(entityConvert(e))
        );

//        retWorld.entityManager.initEntityPopulation();

        // Iterates over all PRDRules, converts each rule and adds it to the world
        prdWorld.getPRDRules().getPRDRule().forEach(
                r -> retWorld.addRule(ruleConvert(r, retWorld))
        );

        // Iterate over all PRDTerminations, converts each termination rule and adds it to the world
        prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks().forEach(
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
        EntityFactory retEntityFactory = new EntityFactory(prdEntity.getName());

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
        Range range = rangeConvert(prdProperty.getPRDRange());

        if(!isRandom && (prdProperty.getPRDValue().getInit() == null))
            throw new IllegalArgumentException("random-initialize field is false, but no init field provided!");

        // Create the property according to its type:
        // PropertyType.getType handles errors with the prdProperty's type
        switch(PropertyType.getType(prdProperty.getType())) {
            case FLOAT:
                retPropertyFactory = new PropertyCreator<>(
                        prdProperty.getPRDName(),
                        PropertyType.FLOAT,
                        isRandom ? new RandomFloatValueGenerator(range) : new FixedValueGenerator<>(Float.parseFloat(prdProperty.getPRDValue().getInit())),
                        range
                );
                break;

            case BOOLEAN:
                retPropertyFactory = new PropertyCreator<>(
                        prdProperty.getPRDName(),
                        PropertyType.BOOLEAN,
                        isRandom ? new RandomBooleanValueGenerator() : new FixedValueGenerator<>(Boolean.parseBoolean(prdProperty.getPRDValue().getInit()))
                );
                break;

            case STRING:
                retPropertyFactory = new PropertyCreator<>(
                        prdProperty.getPRDName(),
                        PropertyType.STRING,
                        isRandom ? new RandomStringValueGenerator() : new FixedValueGenerator<>(prdProperty.getPRDValue().getInit())
                );
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
            throw new IllegalArgumentException("prdTermination received is not of type" +
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
        PropertyFactory retEnvironmentVariable = null;
        PropertyType envVarType = PropertyType.getType(prdEnvProperty.getType());
        ValueGenerator<?> valueGenerator;
        Range range;

        // Create value generator and environment variable result according to the environment variable's type
        switch(envVarType) {
            case FLOAT:
                range = rangeConvert(prdEnvProperty.getPRDRange());
                valueGenerator = new RandomFloatValueGenerator(range);
                retEnvironmentVariable = new PropertyCreator<>(
                        prdEnvProperty.getPRDName(),
                        envVarType,
                        valueGenerator,
                        range
                );
                break;

            case BOOLEAN:
                valueGenerator = new RandomBooleanValueGenerator();
                retEnvironmentVariable = new PropertyCreator<>(
                        prdEnvProperty.getPRDName(),
                        envVarType,
                        valueGenerator
                );
                break;

            case STRING:
                valueGenerator = new RandomStringValueGenerator();
                retEnvironmentVariable = new PropertyCreator<>(
                        prdEnvProperty.getPRDName(),
                        envVarType,
                        valueGenerator
                );
                break;
        }

        return retEnvironmentVariable;
    }


    /**
     * Converts a PRDRange object that was read from the XML file
     * into a Range Object.
     * @param prdRange PRDRange object that was read from the XML file.
     * @return Range object with the data of the PRDRange received.
     */
    private static Range rangeConvert(PRDRange prdRange) {
        if(prdRange == null)
            return null;

        return new Range(prdRange.getFrom(), prdRange.getTo());
    }


    /**
     * Converts a PRDRule object that was read from the XML file
     * into a Rule Object.
     * @param prdRule PRDRule object that was read from the XML file.
     * @return Rule object with the data of the PRDRule received.
     */
    private static Rule ruleConvert(PRDRule prdRule, World worldContext) {
        Rule retRule;

        // Check if the PRDActivation field exists
        if(prdRule.getPRDActivation() != null) {
            retRule = new Rule(
                    prdRule.getName(),
                    activationConvert(prdRule.getPRDActivation()));
        } else {
            retRule = new Rule(
                    prdRule.getName(),
                    new RuleActivation());
        }

        // Iterates over all prdActions, converts them to actions and adds them to the rule
        prdRule.getPRDActions().getPRDAction().forEach(
                a -> retRule.addAction(actionConvert(a, worldContext))
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
        // Only ticks available
        if(prdActivation.getProbability() == null)
            return new RuleActivation(prdActivation.getTicks());

        // Only probability available
        if(prdActivation.getTicks() == null)
            return new RuleActivation(prdActivation.getProbability());

        // both ticks and probability available
        return new RuleActivation(prdActivation.getTicks(), prdActivation.getProbability());
    }


    /**
     * Converts a PRDAction object that was read from the XML file
     * into an Action Object.
     * @param prdAction PRDAction object that was read from the XML file.
     * @return Action object with the data of the PRDAction received.
     */
    private static Action actionConvert(PRDAction prdAction, World worldContext) {
        Action retAction = null;
        ExpressionConverter expressionConverter = new ExpressionConverter(worldContext);

        switch (Action.Type.getType(prdAction.getType())) {
            case INCREASE:
                retAction = new IncreaseAction(
                        worldContext,
                        prdAction.getEntity(),
                        prdAction.getProperty(),
                        expressionConverter.convertExpression("increase", prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy())
                );
                break;

            case DECREASE:
                retAction = new DecreaseAction(
                        worldContext,
                        prdAction.getEntity(),
                        prdAction.getProperty(),
                        expressionConverter.convertExpression("decrease", prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy())
                );
                break;

            case CALCULATION:
                retAction = calculationActionConvert(
                        worldContext,
                        prdAction,
                        expressionConverter
                );
                break;

            case CONDITION:
                retAction = conditionActionConvert(
                        prdAction,
                        worldContext,
                        expressionConverter
                );
                break;

            case SET:
                retAction = new SetAction(
                        worldContext,
                        prdAction.getEntity(),
                        prdAction.getProperty(),
                        expressionConverter.convertExpression("set", prdAction.getEntity(), prdAction.getProperty(), prdAction.getValue())
                );
                break;

            case KILL:
                retAction = new KillAction(
                        worldContext,
                        prdAction.getEntity()
                );

            case REPLACE:
                break;

            case PROXIMITY:
                break;
            }

        return retAction;
    }


    /**
     * Converts a PRDAction of calculation that was read from the XML file
     * into a CalculationAction Object.
     * @param prdAction PRDAction object that was read from the XML file.
     * @return CalculationAction object with the data of the PRDAction received.
     */
    private static CalculationAction calculationActionConvert(World worldContext, PRDAction prdAction, ExpressionConverter expressionConverter){
        CalculationAction retCalculationAction;
        PRDMultiply prdMultiply = prdAction.getPRDMultiply();
        PRDDivide prdDivide = prdAction.getPRDDivide();

        if(prdMultiply != null){
            retCalculationAction = new CalculationAction(
                    worldContext,
                    prdAction.getEntity(),
                    prdAction.getResultProp(),
                    expressionConverter.convertExpression("calculation", prdAction.getEntity(), prdAction.getResultProp(), prdMultiply.getArg1()),
                    expressionConverter.convertExpression("calculation", prdAction.getEntity(), prdAction.getResultProp(), prdMultiply.getArg2()),
                    CalculationAction.Type.MULTIPLY);
        } else if (prdDivide != null) {
            retCalculationAction = new CalculationAction(
                    worldContext,
                    prdAction.getEntity(),
                    prdAction.getResultProp(),
                    expressionConverter.convertExpression("calculation", prdAction.getEntity(), prdAction.getResultProp(), prdDivide.getArg1()),
                    expressionConverter.convertExpression("calculation", prdAction.getEntity(), prdAction.getResultProp(), prdDivide.getArg2()),
                    CalculationAction.Type.DIVIDE);
        }
        else {
            throw new RuntimeException("invalid calculation action received from XML, both prdMultiply and prdDivide fields do not exist! please add them and try again...");
        }

        return retCalculationAction;
    }


    /**
     * Converts a PRDAction of single or multiple condition that was read from the XML file
     * into a ConditionAction Object.
     * @param prdAction PRDAction object that was read from the XML file.
     * @param worldContext the simulated world in which the conditions are set.
     * @param expressionConverter used for converting expressions into values.
     * @return ConditionAction object with the data of the PRDAction received.
     */
    private static ConditionAction conditionActionConvert(PRDAction prdAction, World worldContext, ExpressionConverter expressionConverter){
        PRDCondition prdCondition = prdAction.getPRDCondition();
        final ThenOrElseActions thenActions = new ThenOrElseActions(), elseActions = new ThenOrElseActions();

        // Convert Then/Else action sets
        thenOrElseConvert(prdAction, worldContext, thenActions, elseActions);

        // Create main condition action depending on its type
        if (prdCondition.getSingularity().equals("single")) {
            return new SingleConditionAction(
                    worldContext,
                    prdAction.getEntity(),
                    prdCondition.getProperty(),
                    prdCondition.getOperator(),
                    expressionConverter.convertExpression("condition", prdAction.getEntity(), prdCondition.getProperty(), prdCondition.getValue()),
                    thenActions,
                    elseActions,
                    true
            );
        } else if (prdCondition.getSingularity().equals("multiple")) {
            MultiConditionAction retMultiConditionAction = new MultiConditionAction(
                    worldContext,
                    prdAction.getEntity(),
                    prdCondition.getLogical(),
                    thenActions,
                    elseActions,
                    true
            );
            // Only in case of multiple conditions, convert the inner conditions and add them to the main condition action "retMultiConditionAction"
            convertInnerConditions(prdAction, prdAction.getPRDCondition().getPRDCondition(), worldContext, expressionConverter, retMultiConditionAction);

            return retMultiConditionAction;
        } else {
            throw new RuntimeException("invalid condition action received from XML, singularity field's value is not \"single\" or \"multiple\"!");
        }
    }


    /**
     * Converts recursively all inner multi-conditions of a multi-condition.
     * @param prdAction the main action in which the conditions are set.
     * @param prdConditionList list of all inner conditions to convert.
     * @param worldContext the simulated world in which the conditions are set.
     * @param expressionConverter used for converting expressions into values.
     * @param mainConditionAction the main multi-action in which we search for inner conditions. these inner conditions will be added to the main condition action's list.
     */
    private static void convertInnerConditions(PRDAction prdAction, List<PRDCondition> prdConditionList, World worldContext, ExpressionConverter expressionConverter, MultiConditionAction mainConditionAction) {
        // Convert each condition action in the list of the main condition action
        prdConditionList.forEach(
                prdCondition -> {
                    // Create condition action depending on its type
                    if (prdCondition.getSingularity().equals("single")) {
                        mainConditionAction.addInnerCondition(new SingleConditionAction(
                                worldContext,
                                prdCondition.getEntity(),
                                prdCondition.getProperty(),
                                prdCondition.getOperator(),
                                expressionConverter.convertExpression("condition", prdCondition.getEntity(), prdCondition.getProperty(), prdCondition.getValue()),
                                null,
                                null,
                                false
                        ));
                    } else if (prdCondition.getSingularity().equals("multiple")) {
                        MultiConditionAction multiConditionAction = new MultiConditionAction(
                                worldContext,
                                prdAction.getEntity(),
                                prdCondition.getLogical(),
                                null,
                                null,
                                false
                        );
                        // Find and convert all inner conditions of the multi-condition action
                        convertInnerConditions(prdAction, prdCondition.getPRDCondition(), worldContext, expressionConverter, multiConditionAction);
                        mainConditionAction.addInnerCondition(multiConditionAction);    // Add finished multi-condition action with inner conditions to the main condition action.
                    } else {
                        throw new RuntimeException("invalid condition action received from XML, singularity field's value is not \"single\" or \"multiple\"!");
                    }
                }
        );
    }


    /**
     * Convert Then and Else action sets from XML file, adds them into the proper action sets.
     * @param prdAction PRDAction object that was read from the XML file.
     * @param thenActions ThenElseActions action set for the Then actions.
     * @param elseActions ThenElseActions action set for the Else actions.
     */
    private static void thenOrElseConvert(PRDAction prdAction, World worldContext, final ThenOrElseActions thenActions, final ThenOrElseActions elseActions){
        prdAction.getPRDThen().getPRDAction().forEach(
                a -> thenActions.addAction(actionConvert(a, worldContext))
        );

        // Check if the then actions block contains no actions
        if(thenActions.isEmpty())
            throw new RuntimeException("then actions set received from XML is empty! add at least one action to the then actions set...");

        // Check if there is an else actions block
        if(prdAction.getPRDElse() != null)
            prdAction.getPRDElse().getPRDAction().forEach(
                a -> elseActions.addAction(actionConvert(a, worldContext))
        );
    }
}