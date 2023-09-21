package com.idansh.engine.helpers;

/**
 * Timer class that can be paused, resumed and stopped on command,
 * from another thread.
 */
public class PausableTimer {
    private long startTime;
    private long elapsedTime;
    private boolean isRunning;
    private boolean isPaused;
    private Thread timerThread;

    public PausableTimer() {
        this.isRunning = false;
        this.isPaused = false;
        this.elapsedTime = 0;
    }

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;
            isRunning = true;
            isPaused = false;
            timerThread = new Thread(this::runTimer);
            timerThread.start();
        }
    }

    public void pause() {
        if (isRunning && !isPaused) {
            isPaused = true;
        }
    }

    public void resume() {
        if (isRunning && isPaused) {
            isPaused = false;
            synchronized (this) {
                this.notify(); // Notify the timer thread to resume
            }
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            if (isPaused) {
                synchronized (this) {
                    this.notify(); // Ensure that a paused timer thread is unblocked
                }
            }
            try {
                if (timerThread != null) {
                    timerThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            elapsedTime = 0;
        }
    }

    private void runTimer() {
        while (isRunning) {
            if (isPaused) {
                try {
                    synchronized (this) {
                        this.wait(); // Wait while the timer is paused
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                long currentTime = System.currentTimeMillis();
                elapsedTime = currentTime - startTime;
                // You can add your custom logic here to handle the elapsed time
            }
        }
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
