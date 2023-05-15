package Utils;

import Entities.Destination;
import Entities.Student;

import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < this.STUDENTS_COUNT; i++) {
            Student student = new Student(i,"Entities.Student " + (i + 1));

//            for (int j = 0; j < capacity; j++) {
//                int randIndex = Helpers.rand(0, this.DESTINATIONS_COUNT - 1);
//
//                //check if user already has this destination in his preferences
//                if (student.getPreferences().contains(this.destinations.get(randIndex))) {
//                    j--;
//                    continue;
//                }
//
//                student.addPreference(this.destinations.get(randIndex));
//            }

            this.students.add(student);
        }
    }

    public Student getStudent(int studentId) {
        return this.students
                .stream()
                .findFirst()
                .filter(student -> student.getId() == studentId)
                .orElse(null);
    }

    public Destination getDestination(int destinationId) {
        System.out.println(destinationId);
        // display all destinations
        for (Destination destination : this.destinations) {
            System.out.println(destination);
        }
        return this.destinations
                .stream()
                .findFirst()
                .filter(destination -> destination.getId() == destinationId)
                .orElse(null);
    }


    public List<Destination> getDestinations() {
        return this.destinations;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public int getSTUDENTS_COUNT() {
        return this.STUDENTS_COUNT;
    }

    public int getDESTINATIONS_COUNT() {
        return this.DESTINATIONS_COUNT;
    }

    public void saveStudent(Student student) {
        // find the student with the same id and replace it
        for (int i = 0; i < this.STUDENTS_COUNT; i++) {
            if (this.students.get(i).getId() == student.getId()) {
                this.students.set(i, student);
                break;
            }
        }
    }

    public void addStudentToOptimization(Student student) {
        this.studentsInOptimization.add(student);
    }

    public OptimizationData getOptimizationData() {
        return new OptimizationData(this.studentsInOptimization, this.destinations);
    }

    public void saveDestination(Destination destination) {
        // find the destination with the same id and replace it
        for (int i = 0; i < this.DESTINATIONS_COUNT; i++) {
            if (this.destinations.get(i).getId() == destination.getId()) {
                this.destinations.set(i, destination);
                break;
            }
        }
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
