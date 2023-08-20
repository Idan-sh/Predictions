package com.idansh.engine.helpers;

import java.util.TimerTask;

/**
 * Simple timer class that tells when the countdown was reached.
 */
public class Countdown extends TimerTask {
    private boolean isFinished = false;

    @Override
    public void run() {
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
