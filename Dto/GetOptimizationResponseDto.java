package Dto;

import java.io.Serializable;
import java.util.Map;

public class GetOptimizationResponseDto implements Serializable {
    public final Map<Integer, Integer> studentToDestination;
    public Event event = Event.GetOptimization;

    public GetOptimizationResponseDto(Map<Integer, Integer> studentToDestination) {
        this.studentToDestination = studentToDestination;
    }
}
