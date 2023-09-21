package com.idansh.engine.world;

/**
 * The whole history of the simulated world's values.
 * Each tick will save the current world state into the histogram.
 * The world histogram will function as a two-directional list, while enabling
 * to go from a certain state to the previous or next states.
 */
public class WorldHistogram {
    private static class HistogramNode {
        private final World value;
        private HistogramNode prev;
        private HistogramNode next;

        public HistogramNode(World value, HistogramNode prev, HistogramNode next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        public World getValue() {
            return value;
        }

        public HistogramNode getNext() {
            return next;
        }

        public HistogramNode getPrev() {
            return prev;
        }

        public void setNext(HistogramNode next) {
            this.next = next;
        }

        public void setPrev(HistogramNode prev) {
            this.prev = prev;
        }
    }

    HistogramNode firstNode;
    HistogramNode lastNode;

    public WorldHistogram() {
        firstNode = lastNode = null;
    }

    public void addWorldState(World world) {
        if(firstNode == null) {
            firstNode = lastNode = new HistogramNode(world, null, null);
        } else {
            HistogramNode node = lastNode;
            lastNode = new HistogramNode(world, node, null);
            node.setNext(lastNode);
        }
    }


    /**
     * Get a world state that was saved in the histogram.
     * @param tick the tick number of the world state to get. Send -1 to get the last saved world state.
     */
    public World getWorld(int tick) {
        if(tick == 0)
            return firstNode.getValue();
        if(tick == -1)
            return lastNode.getValue();

        HistogramNode currNode = firstNode;
        int counter = 0;

        while (currNode.getNext() != null) {
            if (counter == tick)
                return currNode.getValue();

            currNode = currNode.getNext();
            counter++;
        }

        throw new RuntimeException("Invalid tick received, cannot get world state of tick " + tick + ". \n" +
                "The last tick in the histogram was " + counter + ".");
    }
}
