package GeneticAlgorithm;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class GeneticAlgorithm {
    private List<List<Integer>> studentToDestinationPreference;
    private List<Integer> destinationToCapacity;
    private int populationSize = 50;
    private int numberOfGenerations = 30000;
    private double mutationProbability = 0.1;
    private int noImprovementLimit = 5000;
    private double fitnessThreshold = 0.99;

    private List<Genome> population;
    private int currentGeneration;
    private int noImprovementCount;
    private double previousBestFitness;
    private Genome best;
    private double bestFitness = Double.NEGATIVE_INFINITY;

    public GeneticAlgorithm(List<List<Integer>> studentToDestinationPreference, List<Integer> destinationToCapacity) {
        this.studentToDestinationPreference = studentToDestinationPreference;
        this.destinationToCapacity = destinationToCapacity;
    }

    private ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    public int getGenomeLength() {
        return studentToDestinationPreference.size();
    }

    public int getNumberOfDestinations() {
        return destinationToCapacity.size();
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setNoImprovementLimit(int noImprovementLimit) {
        this.noImprovementLimit = noImprovementLimit;
    }

    public void setFitnessThreshold(double fitnessThreshold) {
        this.fitnessThreshold = fitnessThreshold;
    }

    public void setNumberOfGenerations(int numberOfGenerations) {
        this.numberOfGenerations = numberOfGenerations;
    }

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public void initialize() {
        population = generatePopulation();
        currentGeneration = 0;
        noImprovementCount = 0;
        previousBestFitness = 0.0;
        best = null;
        bestFitness = Double.NEGATIVE_INFINITY;
    }

    public boolean isEvolutionFinished() {
        return currentGeneration >= numberOfGenerations
                || noImprovementCount >= noImprovementLimit
                || bestFitness >= fitnessThreshold;
    }

    public List<Integer> getOptimizedResult() {
        if (!isEvolutionFinished() || best == null) {
            throw new IllegalStateException("The evolution is not finished yet or no solution found");
        }
        return best.studentToDestination;
    }

    public List<Integer> optimize() {
        initialize();
        while (!isEvolutionFinished())
            evolve();
        return best.studentToDestination;
    }

    public void evolve() {
        if (isEvolutionFinished()) {
            return;
        }

        Function<Genome, Double> fitnessFunc = this::calculateFitness;
        List<Genome> newPopulation = new ArrayList<>();
        for (int j = 0; j < populationSize / 2; j++) {
            List<Genome> parents = selectionPair(population, fitnessFunc);
            List<Genome> children = singlePointCrossover(parents.get(0), parents.get(1));
            newPopulation.add(mutation(children.get(0)));
            newPopulation.add(mutation(children.get(1)));
        }

        // Elitism
        if (best != null) {
            newPopulation.add(best);
        }

        population = newPopulation;

        // Find the best individual
        for (Genome genome : population) {
            double fitness = fitnessFunc.apply(genome);
            if (fitness > bestFitness) {
                best = genome;
                bestFitness = fitness;
            }
        }

        // Check for no improvement
        if (Math.abs(bestFitness - previousBestFitness) < 0.0001) {
            noImprovementCount++;
        } else {
            noImprovementCount = 0;
        }

        previousBestFitness = bestFitness;

        currentGeneration++;
    }

    private double calculateFitness(Genome genome) {
        // Check if students are not exceeding the capacity of the destination
        List<Integer> destinationToCapacityCopy = new ArrayList<>(destinationToCapacity);
        for (int destination : genome.studentToDestination) {
            int newCapacity = destinationToCapacityCopy.get(destination) - 1;
            if (newCapacity < 0)
                return 0.0;
            destinationToCapacityCopy.set(destination, newCapacity);
        }

        return 1.0 / (1 + genome.cost());
    }

    class Genome {
        List<Integer> studentToDestination;
        public Genome(List<Integer> studentToDestination) {
            this.studentToDestination = studentToDestination;
        }

        public int cost() {
            int cost = 0;
            for (int student = 0; student < studentToDestination.size(); student++) {
                int destination = studentToDestination.get(student);
                int preference = studentToDestinationPreference.get(student).indexOf(destination);
                if (preference != -1) {
                    cost += Math.pow(preference, 2);
                } else {
                    cost += 10 * Math.pow(studentToDestinationPreference.get(student).size(), 2);

                }
            }
            return cost;
        }
    }

    public Genome generateGenome() {
        List<Integer> studentToDestination = IntStream.range(0, getGenomeLength())
                .mapToObj(i -> getRandom().nextInt(getNumberOfDestinations()))
                .collect(Collectors.toList());
        return new Genome(studentToDestination);
    }

    public List<Genome> generatePopulation() {
        return IntStream.range(0, populationSize).mapToObj(i -> generateGenome()).collect(Collectors.toList());
    }

    public Genome mutation(Genome genome) {
        List<Integer> studentToDestination = new ArrayList<>(genome.studentToDestination);

        for (int i = 0; i < studentToDestination.size(); i++) {
            if (getRandom().nextDouble() < mutationProbability) {
                int oldDestination = studentToDestination.get(i);
                int newDestination;
                do {
                    newDestination = getRandom().nextInt(getNumberOfDestinations());
                } while (newDestination == oldDestination);
                studentToDestination.set(i, newDestination);
            }
        }

        return new Genome(studentToDestination);
    }

    public List<Genome> singlePointCrossover(Genome a, Genome b) {
        if (a.studentToDestination.size() != b.studentToDestination.size()) {
            throw new IllegalArgumentException("Genomes a and b must be of same length");
        }

        int length = a.studentToDestination.size();
        if (length < 2) {
            return Arrays.asList(a, b);
        }

        int p = getRandom().nextInt(length - 1) + 1;
        List<Integer> newStudentToDestination1 = new ArrayList<>(a.studentToDestination.subList(0, p));
        newStudentToDestination1.addAll(b.studentToDestination.subList(p, b.studentToDestination.size()));

        List<Integer> newStudentToDestination2 = new ArrayList<>(b.studentToDestination.subList(0, p));
        newStudentToDestination2.addAll(a.studentToDestination.subList(p, a.studentToDestination.size()));

        return Arrays.asList(new Genome(newStudentToDestination1), new Genome(newStudentToDestination2));
    }

    public List<Genome> selectionPair(List<Genome> population, Function<Genome, Double> fitnessFunc) {
        List<Double> weights = population.stream().map(fitnessFunc).collect(Collectors.toList());
        WeightedRandomSampler<Genome> sampler = new WeightedRandomSampler<>(population, weights);

        Genome parent1 = sampler.getRandomElement();
        Genome parent2 = sampler.getRandomElement();

        return Arrays.asList(parent1, parent2);
    }

}
