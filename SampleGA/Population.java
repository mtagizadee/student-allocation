package SampleGA;

import Entities.Destination;
import Entities.Student;
import Utils.DB;
import Utils.Helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Population {
    private final Individual[] individuals;
    private int fittestIndex;
    private int secondFittestIndex;
    private int leastFittestIndex;

    // we do not need to store the whole array of fitness values, because we can calculate them dynamically
    // and just store the indexes of the fittest and the second-fittest individuals and the least fit individual
    // it is enough to have a image of the population and perform crossover and mutation operations
    public Population() {
        DB db = Helpers.getDb();
        List<Student> students = db.getStudents();
        this.reset();

        int n = db.getSTUDENTS_COUNT();
        this.individuals = new Individual[n];

        // dynamically calculate fitness and find the fittest and the second-fittest individuals
        for (int i = 0; i < n; i++) {
            this.individuals[i] = new Individual(students.get(i));
            this.identifyFittest(this.individuals[i].getFitness(), i);
        }
    }

    private void reset() {
        this.fittestIndex = -1;
        this.secondFittestIndex = -1;
        this.leastFittestIndex = -1;
    }

    private void identifyFittest(double fitness, int targetIndex) {
        // check if new fittest or second-fittest individual is found
        if (this.fittestIndex == -1 || fitness >= this.individuals[this.fittestIndex].getFitness()) {
            this.secondFittestIndex = this.fittestIndex;
            this.fittestIndex = targetIndex;
        } else if (this.secondFittestIndex == -1 || fitness >= this.individuals[this.secondFittestIndex].getFitness()) {
            this.secondFittestIndex = targetIndex;
        }

        // check if new least fit individual is found
        if (this.leastFittestIndex == -1 || fitness <= this.individuals[this.leastFittestIndex].getFitness()) {
            this.leastFittestIndex = targetIndex;
        }
    }

    public void calculateFitness() { // this function should be used only for fitness recalculations
        int n = this.individuals.length;

        // dynamically calculate fitness and find the fittest and the second-fittest individuals
        for (int i = 0; i < n; i++) {
            Individual individual = this.individuals[i];
            individual.calculateCost();

            this.identifyFittest(individual.getFitness(), i);
        }
    }

    public int getFittestIndex() {
        return this.fittestIndex;
    }

    public void replaceLeastFittest(Individual individual) {
        this.individuals[this.leastFittestIndex].copy(individual);
    }

    public Individual getFittest() {
        return this.individuals[this.fittestIndex];
    }

    public Individual getSecondFittest() {
        return this.individuals[this.secondFittestIndex];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Individual individual : this.individuals) {
            sb.append(individual.toString()).append("\n");
        }

        return sb.toString();
    }

    public Map<Student, List<Destination>> convert() {
        DB db = Helpers.getDb();
        List<Student> students = db.getStudents();
        int n = db.getSTUDENTS_COUNT();

        Map<Student, List<Destination>> result = new HashMap<Student, List<Destination>>();
        for (int i = 0; i < n; i++) {
            result.put(students.get(i), this.individuals[i].convert());
        }

        return result;
    }
}
