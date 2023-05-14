package Utils;

import Entities.Student;

public class CostCalculator {

    public static double calculateForPreferred(int rank) {
        return Math.pow(rank - 1, 2);
    }

    public static double calculateForNonPreferred(Student student) {
        int n = student.getPreferences().size();
        return 10 * Math.pow(n, 2);
    }
}
