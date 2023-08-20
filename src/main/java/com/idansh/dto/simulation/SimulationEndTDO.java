package com.idansh.dto.simulation;

public class SimulationEndTDO {
    private final int simulationId;
    private final String endReason;

    public SimulationEndTDO(int simulationId, String endReason) {
        this.simulationId = simulationId;
        this.endReason = endReason;
    }

    public int getSimulationId() {
        return simulationId;
    }

    public String getEndReason() {
        return endReason;
    }
}
