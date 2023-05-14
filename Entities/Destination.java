package Entities;

import java.io.Serializable;

public class Destination implements Serializable {
    private final int id;
    private final String name;
    private final int capacity;

    public Destination(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.name + " --> " + this.capacity;
    }

}
