package Dto;

import java.io.Serializable;
import java.util.Map;

public class GetOptimizationResponseDto extends Dto implements Serializable {
    public final Map<Integer, Integer> studentToDestination;

    public GetOptimizationResponseDto(Map<Integer, Integer> studentToDestination) {
        super(Event.GetOptimization);
        this.studentToDestination = studentToDestination;
    }
}
