package Dto;

import Entities.Destination;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GetOptimizationDto extends Dto implements Serializable {
    public final int studentId;
    public final int[] preferences;

    public GetOptimizationDto(int studentId, int[] preferences) {
        super(Event.GetOptimization);

        this.studentId = studentId;
        this.preferences = new int[preferences.length];
        for (int i = 0; i < preferences.length; i++) this.preferences[i] = preferences[i];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int destination : this.preferences) {
            sb.append(destination);
            sb.append("\n");
        }

        return this.studentId + " --> " + sb.toString();
    }
}
