package com.idansh.engine.jaxb.unmarshal.converter;

import com.idansh.engine.actions.*;
import com.idansh.engine.actions.condition.*;
import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.jaxb.schema.generated.*;
import com.idansh.engine.property.creator.factory.PropertyCreator;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;
import com.idansh.engine.property.creator.generator.value.fixed.FixedValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomBooleanValueGenerator;
import com.idansh.engine.property.creator.generator.value.random.RandomFloatValueGenerator;
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
        PRDWorld.PRDGrid prdGrid = prdWorld.getPRDGrid();
        World retWorld = new World(prdGrid.getRows(), prdGrid.getColumns());

        retWorld.setThreadCount(prdWorld.getPRDThreadCount());

        // Iterates over all PRDEnvironmentProperties, converts each property and adds it to the world
        prdWorld.getPRDEnvironment().getPRDEnvProperty().forEach(
                p -> retWorld.addEnvironmentVariableFactory(environmentVariableConvert(p))
        );

        retWorld.initEnvironmentVariables();

        // Iterates over all PRDEntities, converts each entity and adds it to the world
        prdWorld.getPRDEntities().getPRDEntity().forEach(
                e -> retWorld.entityManager.addEntityFactory(entityConvert(e))
        );

        // Iterates over all PRDRules, converts each rule and adds it to the world
        prdWorld.getPRDRules().getPRDRule().forEach(
                r -> retWorld.addRule(
                        ruleConvert(r, retWorld))
        );

        if(prdWorld.getPRDTermination().getPRDByUser() != null)
            retWorld.addTerminationRule(
                    new TerminationRule(TerminationRule.Type.USER_DEFINED, null)
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
        if (prdRule.getPRDActivation() != null) {
            retRule = new Rule(
                    prdRule.getName(),
                    activationConvert(prdRule.getPRDActivation()),
                    worldContext
            );
        } else {
            retRule = new Rule(
                    prdRule.getName(),
                    new RuleActivation(),
                    worldContext
            );
        }

        // Iterates over all prdActions, converts them to actions and adds them to the rule
        for (PRDAction action : prdRule.getPRDActions().getPRDAction()) {
            retRule.addAction(
                    actionConvert(
                            worldContext,
                            action.getEntity(),
                            secondEntityConvert(worldContext, action),
                            action
                    )
            );
        }

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
     * @param prdAction PRDAction object that was read from the XML file, defines the most inner action that we are currently converting.
     * @param mainEntityContext Name of the main entity in which the action is defined.
     * @param secondaryEntity The secondary entity in which the action is defined (optional).
     * @return Action object with the data of the PRDAction received.
     */
    private static Action actionConvert(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, PRDAction prdAction) {
        Action retAction;
        ExpressionConverter expressionConverter = new ExpressionConverter(worldContext);

        // Check if entity contexts received are valid
        checkEntityContexts(worldContext, mainEntityContext, secondaryEntity, prdAction);

        String actionEntity = prdAction.getEntity();
        String actionProperty = prdAction.getProperty();

        switch (Action.Type.getType(prdAction.getType())) {
            case INCREASE:
                retAction = new IncreaseAction(
                        worldContext,
                        mainEntityContext,
                        secondaryEntity,
                        actionEntity,
                        actionProperty,
                        expressionConverter.convertExpression(
                                "increase",
                                mainEntityContext,
                                secondaryEntity,
                                prdAction.getProperty(),
                                prdAction.getBy()
                        )
                );
                break;

            case DECREASE:
                retAction = new DecreaseAction(
                        worldContext,
                        mainEntityContext,
                        secondaryEntity,
                        actionEntity,
                        actionProperty,
                        expressionConverter.convertExpression(
                                "decrease",
                                mainEntityContext,
                                secondaryEntity,
                                prdAction.getProperty(),
                                prdAction.getBy()
                        )
                );
                break;

            case CALCULATION:
                retAction = calculationActionConvert(
                        worldContext,
                        mainEntityContext,
                        secondaryEntity,
                        prdAction,
                        expressionConverter
                );
                break;

            case CONDITION:
                retAction = conditionActionConvert(
                        worldContext,
                        mainEntityContext,
                        secondaryEntity,
                        prdAction,
                        prdAction.getPRDCondition(),
                        expressionConverter
                );
                break;

            case SET:
                retAction = new SetAction(
                        worldContext,
                        mainEntityContext,
                        actionEntity,
                        actionProperty,
                        expressionConverter.convertExpression(
                                "set",
                                mainEntityContext,
                                secondaryEntity,
                                prdAction.getProperty(),
                                prdAction.getValue()
                        )
                );
                break;

            case KILL:
                retAction = new KillAction(
                        worldContext,
                        mainEntityContext,
                        secondaryEntity,
                        actionEntity
                );
                break;

            case REPLACE:
                retAction = new ReplaceAction(
                        worldContext,
                        mainEntityContext,
                        secondaryEntity,
                        prdAction.getKill(),
                        prdAction.getCreate(),
                        prdAction.getMode()
                );
                break;

            case PROXIMITY:
                retAction = proximityActionConvert(
                        worldContext,
                        prdAction,
                        expressionConverter
                );
                break;

            default:
                throw new IllegalArgumentException("Cannot convert action of type \"" + prdAction.getType() + "\", no action available of this type.");
            }

        return retAction;
    }

    private static void checkEntityContexts(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, PRDAction prdAction) {
        Action.Type actionType = Action.Type.getType(prdAction.getType());
        String mainContext, secondaryContext;

        if (!actionType.equals(Action.Type.PROXIMITY)) {
            mainContext = mainEntityContext;
            secondaryContext = secondaryEntity != null ? secondaryEntity.getName() : null;
        } else {
            mainContext = prdAction.getPRDBetween().getSourceEntity();
            secondaryContext = prdAction.getPRDBetween().getTargetEntity();
        }

        if (!worldContext.entityManager.isEntityFactoryValid(mainContext))
            throw new IllegalArgumentException("Cannot convert action \"" + prdAction.getType() + "\".\nInvalid main entity context received with name \""
                    + mainContext + "\", no entity of this name was defined...");

        if (!worldContext.entityManager.isEntityFactoryValid(secondaryContext))
            throw new IllegalArgumentException("Cannot convert action \"" + prdAction.getType() + "\".\nInvalid secondary entity context received with name \""
                    + secondaryContext + "\", no entity of this name was defined...");
    }


    /**
     * Converts a PRDAction of proximity condition that was read from the XML file
     * into a ProximityConditionAction Object.
     * @param prdAction PRDAction object that was read from the XML file.
     * @param worldContext the simulated world in which the proximity condition is set.
     * @param expressionConverter used for creating Expression objects from the XML file.
     * @return ProximityConditionAction object with the data of the PRDAction received.
     */
    private static ProximityConditionAction proximityActionConvert(World worldContext, PRDAction prdAction, ExpressionConverter expressionConverter) {
        ThenOrElseActions thenActions = new ThenOrElseActions();
        SecondaryEntity secondaryEntity = new SecondaryEntity(prdAction.getPRDBetween().getTargetEntity());

        // Convert 'then' action sets
        proximityActionsConvert(prdAction, worldContext, secondaryEntity, thenActions);

        // Create and return result Proximity Action
        return new ProximityConditionAction(
                worldContext,
                prdAction.getPRDBetween().getSourceEntity(),
                secondaryEntity,
                prdAction.getPRDBetween().getSourceEntity(),
                prdAction.getPRDBetween().getTargetEntity(),
                thenActions,
                expressionConverter.convertExpression(
                        "proximity",
                        prdAction.getPRDBetween().getSourceEntity(),
                        new SecondaryEntity(prdAction.getPRDBetween().getTargetEntity()),
                        null,
                        prdAction.getPRDEnvDepth().getOf()
                )
        );
    }


    /**
     * Converts a PRDAction of calculation that was read from the XML file
     * into a CalculationAction Object.
     * @param prdAction PRDAction object in which the PRDCondition is set.
     * @param worldContext the simulated world in which the conditions are set.
     * @param expressionConverter used for creating Expression objects from the XML file.
     * @return CalculationAction object with the data of the PRDAction received.
     */
    private static CalculationAction calculationActionConvert(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, PRDAction prdAction, ExpressionConverter expressionConverter) {
        CalculationAction retCalculationAction;
        PRDMultiply prdMultiply = prdAction.getPRDMultiply();
        PRDDivide prdDivide = prdAction.getPRDDivide();

        if(prdMultiply != null || prdDivide != null){
            retCalculationAction = new CalculationAction(
                    worldContext,
                    mainEntityContext,
                    secondaryEntity,
                    prdAction.getEntity(),
                    prdAction.getResultProp(),
                    expressionConverter.convertExpression(
                            "calculation",
                            prdAction.getEntity(),
                            secondaryEntity,
                            prdAction.getResultProp(),
                            prdMultiply != null ? prdMultiply.getArg1() : prdDivide.getArg1()
                    ),
                    expressionConverter.convertExpression(
                            "calculation",
                            prdAction.getEntity(),
                            secondaryEntity,
                            prdAction.getResultProp(),
                            prdMultiply != null ? prdMultiply.getArg2() : prdDivide.getArg2()
                    ),
                    prdMultiply != null ? CalculationAction.Type.MULTIPLY : CalculationAction.Type.DIVIDE
            );
        } else {
            throw new RuntimeException("invalid calculation action received from XML, both prdMultiply and prdDivide fields do not exist! please add them and try again...");
        }

        return retCalculationAction;
    }


    /**
     * Converts a PRDAction of single or multiple condition that was read from the XML file
     * into a ConditionAction Object.
     * @param prdAction PRDAction object in which the PRDCondition is set.
     * @param worldContext the simulated world in which the conditions are set.
     * @param expressionConverter used for creating Expression objects from the XML file.
     * @return ConditionAction object with the data of the PRDAction received.
     */
    private static ConditionAction conditionActionConvert(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, PRDAction prdAction, PRDCondition prdCondition, ExpressionConverter expressionConverter){
        ThenOrElseActions thenActions = new ThenOrElseActions(), elseActions = new ThenOrElseActions();

        // Convert Then/Else action sets
        thenOrElseConvert(worldContext, mainEntityContext, secondaryEntity, prdAction, thenActions, elseActions);

        // Create main condition action depending on its type
        if (prdCondition.getSingularity().equals("single")) {
            return new SingleConditionAction(
                    worldContext,
                    mainEntityContext,
                    secondaryEntity,
                    prdCondition.getEntity(),
                    expressionConverter.convertPropertyExpression(
                            prdCondition.getEntity(),
                            secondaryEntity,
                            prdCondition.getProperty()
                    ),
                    prdCondition.getOperator(),
                    expressionConverter.convertExpression(
                            "condition",
                            mainEntityContext,
                            secondaryEntity,
                            prdCondition.getProperty(),
                            prdCondition.getValue()
                    ),
                    thenActions,
                    elseActions,
                    true
            );
        } else if (prdCondition.getSingularity().equals("multiple")) {
            MultiConditionAction retMultiConditionAction =
                    new MultiConditionAction(
                            worldContext,
                            mainEntityContext,
                            secondaryEntity,
                            mainEntityContext,
                            prdCondition.getLogical(),
                            thenActions,
                            elseActions,
                            true
                    );
            // Only in case of multiple conditions, convert the inner conditions and add them to the main condition action "retMultiConditionAction"
            convertInnerConditions(
                    worldContext,
                    mainEntityContext,
                    secondaryEntity,
                    prdAction,
                    expressionConverter,
                    prdCondition.getPRDCondition(),
                    retMultiConditionAction
            );

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
     * @param expressionConverter used for creating Expression objects from the XML file.
     * @param mainConditionAction the main multi-action in which we search for inner conditions. these inner conditions will be added to the main condition action's list.
     */
    private static void convertInnerConditions(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, PRDAction prdAction, ExpressionConverter expressionConverter, List<PRDCondition> prdConditionList, MultiConditionAction mainConditionAction) {
        // Convert each condition action in the list of the main condition action
        for (PRDCondition prdCondition : prdConditionList)
            // Create condition action depending on its type
            if (prdCondition.getSingularity().equals("single")) {
                mainConditionAction.addInnerCondition(
                        new SingleConditionAction(
                                worldContext,
                                mainEntityContext,
                                secondaryEntity,
                                prdCondition.getEntity(),
                                expressionConverter.convertPropertyExpression(
                                        prdCondition.getEntity(),
                                        secondaryEntity,
                                        prdCondition.getProperty()
                                ),
                                prdCondition.getOperator(),
                                expressionConverter.convertExpression(
                                        "condition",
                                        prdCondition.getEntity(),
                                        secondaryEntity,
                                        prdCondition.getProperty(),
                                        prdCondition.getValue()
                                ),
                                null,
                                null,
                                false
                        ));
            } else if (prdCondition.getSingularity().equals("multiple")) {
                MultiConditionAction multiConditionAction =
                        new MultiConditionAction(
                                worldContext,
                                mainEntityContext,
                                secondaryEntity,
                                prdAction.getEntity(),
                                prdCondition.getLogical(),
                                null,
                                null,
                                false
                        );

                // Find and convert all inner conditions of the multi-condition action
                convertInnerConditions(
                        worldContext,
                        mainEntityContext,
                        secondaryEntity,
                        prdAction,
                        expressionConverter,
                        prdCondition.getPRDCondition(),
                        multiConditionAction
                );
                mainConditionAction.addInnerCondition(multiConditionAction);    // Add finished multi-condition action with inner conditions to the main condition action.
            } else {
                throw new RuntimeException("invalid condition action received from XML, singularity field's value is not \"single\" or \"multiple\"!");
            }
    }


    /**
     * Convert Then and Else action sets from XML file, adds them into the proper action sets.
     * @param worldContext the simulated world in which the conditions are set.
     * @param mainEntityContext Name of the main entity in which the condition is defined.
     * @param secondaryEntity The secondary entity defined within the main entity.
     * @param prdAction PRDAction object that was read from the XML file.
     * @param thenActions ThenElseActions action set for the Then actions.
     * @param elseActions ThenElseActions action set for the Else actions (optional).
     */
    private static void thenOrElseConvert(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, PRDAction prdAction, final ThenOrElseActions thenActions, final ThenOrElseActions elseActions) {
        if(prdAction == null)
            return;

        // Convert the 'then' block of actions
        for (PRDAction action : prdAction.getPRDThen().getPRDAction()) {
            thenActions.addAction(
                    actionConvert(
                            worldContext,
                            mainEntityContext,
                            secondaryEntity,
                            action
                    )
            );
        }

        // Check if the then actions block contains no actions
        if(thenActions.isEmpty())
            throw new RuntimeException("then actions set received from XML is empty! add at least one action to the then actions set...");

        // Check if there is an else actions block
        if(prdAction.getPRDElse() != null) {
            for (PRDAction action : prdAction.getPRDElse().getPRDAction()) {
                elseActions.addAction(
                        actionConvert(
                                worldContext,
                                mainEntityContext,
                                secondaryEntity,
                                action
                        )
                );
            }
        }
    }


    /**
     * Convert action set for proximity action from XML file.
     * @param prdAction PRDAction object that was read from the XML file.
     * @param worldContext the simulated world in which the conditions are set.
     * @param thenActions ThenElseActions action set for the proximity's actions.
     */
    private static void proximityActionsConvert(PRDAction prdAction, World worldContext, SecondaryEntity secondaryEntity, final ThenOrElseActions thenActions){
        for (PRDAction action : prdAction.getPRDActions().getPRDAction()) {
            thenActions.addAction(
                    actionConvert(
                            worldContext,
                            prdAction.getPRDBetween().getSourceEntity(),
                            secondaryEntity,
                            action
                    )
            );
        }

        // Check if the then actions block contains no actions
        if(thenActions.isEmpty())
            throw new RuntimeException("Proximity action's actions set received from XML is empty, add at least one action to the actions set...");
    }


    /**
     * Returns a secondary entity object if one exists in the XML file,
     * Otherwise returns null.
     * @param prdAction a PRDAction in which the secondary entity might reside.
     * @throws IllegalArgumentException in case there is no secondary entity with the name received from the XML file.
     */
    private static SecondaryEntity secondEntityConvert(World worldContext, PRDAction prdAction) {
        PRDAction.PRDSecondaryEntity prdSecondaryEntity = prdAction.getPRDSecondaryEntity();

        if(prdSecondaryEntity != null) {
            // Check if there is an entity with the name of the received secondary entity, if not throw exception
            if(!worldContext.entityManager.isEntityFactoryValid(prdSecondaryEntity.getEntity()))
                throw new IllegalArgumentException("Invalid secondary entity name \"" + prdSecondaryEntity.getEntity()
                        + "\".\nNo entity of this name exist in the simulation...");

            SecondaryEntity secondaryEntity = new SecondaryEntity(
                    prdSecondaryEntity.getEntity(),
                    prdSecondaryEntity.getPRDSelection().getCount()
            );

            secondaryEntity.addNewConditionAction(
                    conditionActionConvert(
                            worldContext,
                            prdAction.getEntity(),
                            new SecondaryEntity(prdSecondaryEntity.getEntity()),
                            null,
                            prdSecondaryEntity.getPRDSelection().getPRDCondition(),
                            new ExpressionConverter(worldContext)
                    ));

            return secondaryEntity;
        }

        return null;
    }
}