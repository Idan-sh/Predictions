package com.idansh.engine.helpers;

public class Counter {
    int count;

    public Counter(int init){
        this.count = init;
    }

    public void increaseCount() {
        count++;
    }

    public void decreaseCount() {
        System.out.println("PREVIOUS COUNTER: " + count);
        count--;
        System.out.println("NEW COUNTER: " + count);
    }

    public int getCount() {
        return count;
    }

    public void resetCount() {
        count = 0;
    }
}
