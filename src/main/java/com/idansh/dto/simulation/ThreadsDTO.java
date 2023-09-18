package com.idansh.dto.simulation;

/**
 * DTO with info on the current load on the thread pool,
 * with the number of queueing, running and finished threads in the thread pool.
 */
public class ThreadsDTO {
    private final int nofQueueThreads;
    private final int nofRunningThreads;
    private final int nofFinishedThreads;

    public ThreadsDTO(int nofQueueThreads, int nofRunningThreads, int nofFinishedThreads) {
        this.nofQueueThreads = nofQueueThreads;
        this.nofRunningThreads = nofRunningThreads;
        this.nofFinishedThreads = nofFinishedThreads;
    }

    public int getNofQueueThreads() {
        return nofQueueThreads;
    }

    public int getNofRunningThreads() {
        return nofRunningThreads;
    }

    public int getNofFinishedThreads() {
        return nofFinishedThreads;
    }
}
