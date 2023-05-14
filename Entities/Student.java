package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private final int id;
    private final String name;
    private final List<Destination> preferences;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.preferences = new ArrayList<Destination>();
    }

    public List<Destination> getPreferences() {
        return this.preferences;
    }

    public int isPreferred(Destination destination) {
        int n = this.preferences.size();
        for (int i = 0; i < n; i++) {
            if (this.preferences.get(i).getId() == destination.getId()) return i;
        }

        return -1;
    }

    public int isPreferred(int destinationId) {
        int n = this.preferences.size();
        for (int i = 0; i < n; i++) {
            if (this.preferences.get(i).getId() == destinationId) return i;
        }

        return -1;
    }

    public void setPreferences(List<Destination> preferences) {
        this.preferences.clear();
        this.preferences.addAll(preferences);
    }

    public String toString() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public void addPreference(Destination destination) {
        this.preferences.add(destination);
    }

}
