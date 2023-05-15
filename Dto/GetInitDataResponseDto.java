package Dto;

import Entities.Destination;
import Entities.Student;

import java.io.Serializable;
import java.util.List;

public class GetInitDataResponseDto extends Dto implements Serializable {
    public List<Student> students;
    public List<Destination> destinations;

    public GetInitDataResponseDto(List<Student> students, List<Destination> destinations) {
        super(Event.GetInitData);
        this.destinations = destinations;
        this.students = students;
    }

}
