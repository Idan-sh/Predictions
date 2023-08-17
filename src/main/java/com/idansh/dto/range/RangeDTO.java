package com.idansh.dto.range;

public class RangeDTO {
    private final double from;
    private final double to;

    public RangeDTO(double from, double to) {
        this.from = from;
        this.to = to;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }
}
