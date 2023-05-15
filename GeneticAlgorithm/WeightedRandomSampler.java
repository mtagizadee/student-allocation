package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class WeightedRandomSampler<T> {
    private final List<Double> cumulativeWeights;
    private final List<T> elements;

    public WeightedRandomSampler(List<T> elements, List<Double> weights) {
        if (elements.size() != weights.size()) {
            throw new IllegalArgumentException("Elements and weights must have the same size");
        }

        this.elements = elements;
        this.cumulativeWeights = new ArrayList<>();

        double cumulativeWeight = 0.0;
        for (Double weight : weights) {
            cumulativeWeight += weight;
            this.cumulativeWeights.add(cumulativeWeight);
        }
    }

    public T getRandomElement() {
        double randomValue = ThreadLocalRandom.current().nextDouble()
                * cumulativeWeights.get(cumulativeWeights.size() - 1);

        int index = Collections.binarySearch(cumulativeWeights, randomValue);
        if (index < 0) {
            // Convert negative insertion point to array index.
            index = -index - 1;
        }

        return elements.get(index);
    }
}
