package Entities;

import java.io.Serializable;

public class Destination implements Serializable {
    private final int id;
    private final String name;
    private final int capacity;
    private int capacityLeft;

    public Destination(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.capacityLeft = capacity;
    }

    public int getId() {
        return this.id;
    }

    public boolean isFull () {
        return this.capacityLeft == 0;
    }

    public void addStudent() {
        this.capacityLeft--;
    }

    public String toString() {
        return this.name + " --> " + this.capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }
}
