package Dto;

import Entities.Destination;
import Entities.Student;

import java.io.Serializable;
import java.util.List;

public class GetInitDataResponseDto implements Serializable {
    public List<Student> students;
    public List<Destination> destinations;
    public final Event event = Event.GetInitData;

    public GetInitDataResponseDto(List<Student> students, List<Destination> destinations) {
        this.destinations = destinations;
        this.students = students;
    }

}
