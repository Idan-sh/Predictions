package com.idansh.engine.property.creator.factory;

import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.creator.generator.value.fixed.FixedValueGenerator;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;

/**
 * Creates properties by the value type defined, using a value generator.
 * @param <T> The type of the property.
 */
public class PropertyCreator<T> implements PropertyFactory{
    private final String name;
    private final PropertyType type;
    private ValueGenerator<T> valueGenerator;
    private final Range range;


    /**
     * @param name Unique name
     * @param type The type of the property's value
     * @param valueGenerator Value generator (assigns fixed/random value)
     */
    public PropertyCreator(String name, PropertyType type, ValueGenerator<T> valueGenerator) {
        this.name = name;
        this.type = type;
        this.valueGenerator = valueGenerator;
        this.range = null;
    }

    /**
     * Constructor only for numeric properties.
     * @param name Unique name.
     * @param type The type of the property's value.
     * @param valueGenerator Value generator (assigns fixed/random value),
     * @param range range for the numeric property's value.
     */
    public PropertyCreator(String name, PropertyType type, ValueGenerator<T> valueGenerator, Range range) {
        this.type = type;

        if(!this.isNumericProperty())
            throw new IllegalArgumentException("range can be set only for numeric properties! the type of this property creator is \"" + type.toString() + "\"!");

        this.range = range;
        this.name = name;
        this.valueGenerator = valueGenerator;
    }

    /**
     * Factory Method to create a property instance.
     * @return a new instance of a boolean property.
     */
    @Override
    public Property createProperty() {
        if(type.equals(PropertyType.INTEGER) || type.equals(PropertyType.FLOAT))
            return new Property(name, type, valueGenerator.generateValue(), range);
        return new Property(name, type, valueGenerator.generateValue());
    }

    @Override
    public void updateValue(Object newValue) {
        if(!valueGenerator.generateValue().getClass().equals(newValue.getClass()))
            throw new IllegalArgumentException("value received in updateValue is not of the property factory's type!");

        valueGenerator = new FixedValueGenerator<>((T) newValue);
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public PropertyType getType() {
        return type;
    }

    @Override
    public Object getValue() {
        return valueGenerator.generateValue();
    }

    @Override
    public boolean isNumericProperty() {
        return PropertyType.INTEGER.equals(type) || PropertyType.FLOAT.equals(type);
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public boolean isRandomGenerated() {
        return !(valueGenerator instanceof FixedValueGenerator);
    }
}
