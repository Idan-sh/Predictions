package com.idansh.engine.property.creator;

import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;

/**
 * Created properties by the type defined, using a value generator.
 * @param <T> The type of the property.
 */
public abstract class PropertyCreator<T> implements PropertyFactory{
    private final String name;
    private final PropertyType type;
    private final ValueGenerator<T> valueGenerator;


    /**
     *
     * @param name Unique name
     * @param type The type of the property's value
     * @param valueGenerator Value generator (assigns fixed/random value)
     */
    public PropertyCreator(String name, PropertyType type, ValueGenerator<T> valueGenerator) {
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
        return new Property(name, type, valueGenerator.generateValue());
    }
}
