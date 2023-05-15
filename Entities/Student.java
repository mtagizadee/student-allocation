package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private final int id;
    private final String studentId;
    private final List<Destination> preferences;

    public Student(int id, String studentId) {
        this.id = id;
        this.studentId = studentId;
        this.preferences = new ArrayList<Destination>();
    }

    public List<Destination> getPreferences() {
        return this.preferences;
    }

    public void setPreferences(List<Destination> preferences) {
        this.preferences.clear();
        this.preferences.addAll(preferences);
    }

    public String toString() {
        return this.studentId;
    }

    public int getId() {
        return this.id;
    }
}
