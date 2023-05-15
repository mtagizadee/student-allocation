package Utils;

import Entities.Destination;
import Entities.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DB {
    private final int STUDENTS_COUNT = 40;
    private final int DESTINATIONS_COUNT = 10;
    private final List<Student> studentsInOptimization;

    private final List<Student> students;
    private final List<Destination> destinations;

    public DB() {
        this.destinations = new ArrayList<Destination>(this.DESTINATIONS_COUNT);
        this.students = new ArrayList<Student>(this.STUDENTS_COUNT);
        this.studentsInOptimization = new ArrayList<Student>();

        this.initDestinations();
        this.initStudents();
    }

    private void initDestinations() { // init destinations with random preferences at the beginning
        for (int i = 0; i < this.DESTINATIONS_COUNT; i++) {
            this.destinations.add(new Destination(i,"Destination " + (i + 1), Helpers.rand(1, 6)));
        }
    }

    private void initStudents() { // init students with random preferences at the beginning
        // add student with id 22022695
        for (int i = 0; i < 40; i++) {
            String studentId = "22022" + String.format("%03d", i + 1);
            this.students.add(new Student(i, studentId));
        }
    }

    public Student getStudent(int studentId) {

        for (Student student : this.students) {
            if (student.getId() == studentId) {
                return student;
            }
        }

        return null;
    }

    public Destination getDestination(int destinationId) {
        for (Destination destination : this.destinations) {
            if (destination.getId() == destinationId) {
                return destination;
            }
        }

        return null;
    }


    public List<Destination> getDestinations() {
        return this.destinations;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public void addStudentToOptimization(Student student) {
        this.studentsInOptimization.add(student);

        //display students in optimization
        for (Student studentInOptimization : this.studentsInOptimization) {
            System.out.println(studentInOptimization);
        }
    }

    public OptimizationData getOptimizationData() {
        return new OptimizationData(this.studentsInOptimization, this.destinations);
    }

    public void displayStudents() {
        for (Student student : this.students) {
            System.out.println(student);

            // display student's preferences
            for (Destination destination : student.getPreferences()) {
                System.out.println("\t" + destination);
            }
        }
    }
}
