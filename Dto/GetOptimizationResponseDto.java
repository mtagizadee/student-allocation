package Dto;

import java.io.Serializable;

public class GetOptimizationResponseDto implements Serializable {
    public final int studentId;
    public final int destinationId;
    public Event event = Event.GetOptimization;

    public GetOptimizationResponseDto(int studentId, int destinationId) {
        this.studentId = studentId;
        this.destinationId = destinationId;
    }
}
