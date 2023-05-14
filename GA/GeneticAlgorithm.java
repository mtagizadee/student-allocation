package GA;

import Entities.Destination;
import Entities.Student;
import Utils.DB;
import Utils.Helpers;

public class GeneticAlgorithm {
    private final Population population;
    private Individual fittest;
    private Individual secondFittest;

    public GeneticAlgorithm() {
        this.population = new Population();
    }

    public void selection() {
        this.fittest = this.population.getFittest();
        this.secondFittest = this.population.getSecondFittest();
    }

    public void crossover() {
        DB db = Helpers.getDb();
        int crossoverPoint = Helpers.rand(0, db.getDESTINATIONS_COUNT() - 1);

        // swap the genes of the two individuals
        for (int i = 0; i < crossoverPoint; i++) {
            Gene temp = this.fittest.getGene(i);
            this.fittest.setGene(i, this.secondFittest.getGene(i));
            this.secondFittest.setGene(i, temp);
        }
    }

    public void mutation() {
        DB db = Helpers.getDb();
        int n = db.getDESTINATIONS_COUNT();

        // for fittest and second-fittest individuals iterate over all genes and swap them with a chance of 2/5
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < n; i++) {
                int chance = Helpers.rand(0, 5);

                if (!toMutate(chance)) continue;
                (j == 0? this.fittest : this.secondFittest).swapGene(i);
            }
        }
    }

    private boolean toMutate(int chance) { // since the chance is between 0 and 5 => 2/5 chance to mutate a gene
        return chance == 0 || chance == 1;
    }

    public void updatePopulation() {
        this.fittest.calculateCost();
        this.secondFittest.calculateCost();

        double fitness1 = this.fittest.getFitness();
        double fitness2 = this.secondFittest.getFitness();

        this.population.replaceLeastFittest(fitness1 > fitness2 ? this.fittest : this.secondFittest);
    }

    public Destination optimize(Student student) {
        DB db = Helpers.getDb();
        int n = db.getSTUDENTS_COUNT();

        int generation = 1;
        while (this.population.getFittestIndex() < n - 1 && generation < 1000) {
            this.population.calculateFitness();

            this.selection();
            this.crossover();
            this.mutation();

            this.updatePopulation();
            generation++;
        }

        return Helpers.result(this.population, student);
    }
}
