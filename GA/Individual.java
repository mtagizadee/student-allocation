package GA;

import Entities.Destination;
import Entities.Student;
import Utils.CostCalculator;
import Utils.DB;
import Utils.Helpers;

import java.util.ArrayList;
import java.util.List;

enum Gene { Negative, Positive } // a chromosome contains 0 and 1 values, 1 stands for positive, 0 stands for negative

public class Individual {
    private final Gene[] chromosome;
    private double cost;
    private final Student student;

    public Individual(Student student) {
        DB db = Helpers.getDb();
        List<Destination> destinations = db.getDestinations();

        this.student = student;
        this.chromosome = new Gene[db.getDESTINATIONS_COUNT()];

        // dynamically calculate cost and fill the chromosome
        for (Destination destination : destinations) {
            int preferredIndex = student.isPreferred(destination); // find the index of the destination in the student's preferences
            int destinationId = destination.getId();

            if (preferredIndex == -1) { // -1 means that the destination is not in the student's preferences
                this.chromosome[destinationId] = Gene.Negative;
                cost += CostCalculator.calculateForNonPreferred(this.student);
                continue; // skip the rest of the loop
            }

            this.chromosome[destinationId] = Gene.Positive;
            cost += CostCalculator.calculateForPreferred(preferredIndex);
        }
    }

    public void copy(Individual individual) {
        int n = this.chromosome.length;
        System.arraycopy(individual.chromosome, 0, this.chromosome, 0, n);
        this.cost = individual.cost;
    }

    public void calculateCost() { // this function should be used only for cost recalculations
        int n = this.chromosome.length;
        this.cost = 0;

        for (int i = 0; i < n; i++) {
            if (this.chromosome[i] == Gene.Negative) {
                this.cost += CostCalculator.calculateForNonPreferred(this.student);
                continue;
            }

            int rank = this.student.isPreferred(i) + 1; // rank is the place in the student's preferences top
            this.cost += CostCalculator.calculateForPreferred(rank);
        }
    }

    public double getFitness() {
        return 1 / this.cost;
    }

    public void setGene(int index, Gene gene) {
        this.chromosome[index] = gene;
    }

    public Gene getGene(int index) {
        return this.chromosome[index];
    }

    public void swapGene(int index) {
        this.chromosome[index] = this.chromosome[index] == Gene.Negative ? Gene.Positive : Gene.Negative;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Gene gene : this.chromosome) {
            sb.append(gene == Gene.Negative ? 0 : 1);
        }

        return sb.append(" ").append(this.getFitness()).toString();
    }

    public List<Destination> convert() {
        DB db = Helpers.getDb();
        List<Destination> destinations = db.getDestinations();
        int n = db.getDESTINATIONS_COUNT();

        List<Destination> result = new ArrayList<Destination>();
        for (int i = 0; i < n; i++) {
            Gene gene = chromosome[i];

            if (gene == Gene.Negative) continue;
            result.add(destinations.get(i));
        }

        return result;
    }
}