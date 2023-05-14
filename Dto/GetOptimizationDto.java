package Dto;

import Entities.Destination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetOptimizationDto implements Serializable {
    public final int studentId;
    public final List<Integer> preferences;
    public final Event event = Event.GetOptimization;

    public GetOptimizationDto(int studentId, ArrayList<Integer> preferences) {
        this.studentId = studentId;
        this.preferences = preferences;
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
