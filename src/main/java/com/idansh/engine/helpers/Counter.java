package com.idansh.engine.helpers;

public class Counter {
    int count;

    public Counter(int init){
        this.count = init;
    }

    public void addCount() {
        count++;
    }
    public int getCount() {
        return count;
    }
    public void resetCount() {
        count = 0;
    }
}
