package com.idansh.dto.property;

import com.idansh.dto.range.RangeDTO;

/**
 * Contains details/information of a main entity's property.
 */
public class PropertyDTO {
    private final String name;
    private final String type;
    private final RangeDTO rangeDTO; // optional
    private final Object value;
    private final boolean isRandomGenerated;
    private final Float consistency;

    public PropertyDTO(String name, String type, RangeDTO rangeDTO, boolean isRandomGenerated, Object value, float consistency) {
        this.name = name;
        this.type = type;
        this.rangeDTO = rangeDTO;
        this.isRandomGenerated = isRandomGenerated;
        this.value = value;
        this.consistency = consistency;
    }

    public PropertyDTO(String name, String type, RangeDTO rangeDTO, boolean isRandomGenerated, Object value) {
        this.name = name;
        this.type = type;
        this.rangeDTO = rangeDTO;
        this.isRandomGenerated = isRandomGenerated;
        this.value = value;
        this.consistency = null;
    }

    public PropertyDTO(String name, String type, boolean isRandomGenerated, Object value, float consistency) {
        this.rangeDTO = null;
        this.name = name;
        this.type = type;
        this.isRandomGenerated = isRandomGenerated;
        this.value = value;
        this.consistency = consistency;
    }

    public PropertyDTO(String name, String type, boolean isRandomGenerated, Object value) {
        this.rangeDTO = null;
        this.name = name;
        this.type = type;
        this.isRandomGenerated = isRandomGenerated;
        this.value = value;
        this.consistency = null;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public RangeDTO getRangeDTO() {
        return rangeDTO;
    }

    public boolean isRandomGenerated() {
        return isRandomGenerated;
    }

    public Object getValue() {
        return value;
    }

    public Float getConsistency() {
        return consistency;
    }

    @Override
    public String toString() {
        return name;
    }
}
