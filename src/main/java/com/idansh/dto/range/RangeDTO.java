package com.idansh.dto.range;

public class RangeDTO {
    private final int from;
    private final int to;

    public RangeDTO(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
