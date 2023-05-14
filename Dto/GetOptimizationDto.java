package Dto;

import Entities.Destination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetOptimizationDto implements Serializable {
    public final int studentId;
    public final List<Destination> preferences;

    public GetOptimizationDto(int studentId, ArrayList<Destination> preferences) {
        this.studentId = studentId;
        this.preferences = preferences;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Destination destination : this.preferences) {
            sb.append(destination.toString());
            sb.append("\n");
        }

        return this.studentId + " --> " + sb.toString();
    }
}
