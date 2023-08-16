package com.idansh.engine.property.creator.factory;

import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * Creates properties by the value type defined, using a value generator.
 * @param <T> The type of the property.
 */
public class PropertyCreator<T> implements PropertyFactory{
    private final String name;
    private final PropertyType type;
    private final ValueGenerator<T> valueGenerator;
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
    public PropertyCreator(String name, PropertyType type, ValueGenerator<T> valueGenerator, @NotNull Range range) {
        if(!this.isNumericProperty())
            throw new IllegalArgumentException("Error: range can be set only for numeric properties!");
        this.range = range;
        this.name = name;
        this.type = type;
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
    public String getName() {
        return name;
    }


    public PropertyType getType() {
        return type;
    }


    @Override
    public boolean isNumericProperty() {
        return PropertyType.INTEGER.equals(type) || PropertyType.FLOAT.equals(type);
    }
}
