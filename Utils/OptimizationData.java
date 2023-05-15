package Utils;

import Entities.Destination;
import Entities.Student;

import java.util.ArrayList;
import java.util.List;

public class OptimizationData {

    public List<List<Integer>> studentToDestinationPreference;
    public List<Integer> destinationToCapacity;
    public List<Student> studentsReferences;

    public OptimizationData(List<Student> studentsInOptimization, List<Destination> destinations) {
        this.studentsReferences = new ArrayList<Student>();
        this.studentsReferences.addAll(studentsInOptimization);


        this.destinationToCapacity = destinations.stream().map(Destination::getCapacity).toList();
        this.studentToDestinationPreference = new ArrayList<List<Integer>>();

        // iterate over all students
        for (Student student : studentsReferences) {
            List<Integer> studentPreferences = new ArrayList<Integer>();

            // iterate over all destinations
            for (Destination preference : student.getPreferences()) {
                // find the index of the destination in the student preferences
                int index = destinations.indexOf(preference);
                if (index != -1) studentPreferences.add(index);
            }

            this.studentToDestinationPreference.add(studentPreferences);
        }
    }

    public String toString() {
        // display all students in studentsReference
        System.out.println("Students: ");
        for (Student student : this.studentsReferences) {
            System.out.println(student);
        }

        // display all preferences
        System.out.println("Preferences: ");
        for (List<Integer> preferences : this.studentToDestinationPreference) {
            for (int preference : preferences) {
                System.out.print(preference + " ");
            }
            System.out.println();
        }

        // display all capacities
        System.out.println("Capacities: ");
        for (int capacity : this.destinationToCapacity) {
            System.out.print(capacity + " ");
        } System.out.println();

        Helpers.getDb().getDestinations().forEach(System.out::println);
        return "";
    }


}
