package geneticalgorithm;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    private int popSize;
    private final int geneSize;
    public ArrayList<TSPIndividual> individuals;
    public TSPIndividual bestIndividual;
    private ArrayList<Double> rouletteWheel;

    public Population(int geneSize) {
        this.geneSize = geneSize;
    }

    public Population(int popSize, int geneSize, boolean randomInit) {
        this(geneSize);
        this.popSize = popSize;
        individuals = new ArrayList<>(popSize);

        if (randomInit) {
            randomInitialize();
        }
    }

    public void randomInitialize() {
        /**
         * Initialize the population with a random manner
         */

        for (int i = 0; i < popSize; i++) {
            this.individuals.add(new TSPIndividual(geneSize, true));
        }
    }

    public void calculatePopFitness(double[][] weightMatrix) {
        /**
         * Calculate fitness over the entire population
         */
        for (TSPIndividual ind: individuals) {
            ind.calculateFitness(weightMatrix);
        }
    }

    public void buildRouletteWheel() {
        /**
         * Build the roulette wheel for the upcoming evaluation
         */

        int n = getCurrentSize();
        double sumFitness = 0;
        rouletteWheel = new ArrayList<>(n);

        for (TSPIndividual ind: individuals) {
            sumFitness += ind.getFitness();
        }

        double probability = 0;
        for (int i = 0; i < n; i++) {
            probability += individuals.get(i).getFitness() / sumFitness;
            rouletteWheel.add(probability);
        }
    }

    public TSPIndividual rollRoulette() {
        /**
         * Get the chosen individual by rolling the roulette wheel
         */

        TSPIndividual output = null;
        Random rand = new Random();
        double prob = rand.nextDouble();

        int left = 0, right = rouletteWheel.size() - 1;
        while (true) {
            int mid = left + (right - left) / 2;
            if (prob > rouletteWheel.get(mid) && prob <= rouletteWheel.get(mid+1)) {
                output = individuals.get(mid);
                break;
            }

            if (left == right)
                break;

            if (prob <= rouletteWheel.get(mid)) {
                right = mid;
            } else if (prob > rouletteWheel.get(mid+1)) {
                left = mid+1;
            }
        }

        return output;
    }

    public Population evaluatePopulation(double[][] weightMatrix) {
        /**
         * Generate new population after breeding new offsprings
         * Using roulette selection
         */

        Population newPop = new Population(popSize, this.geneSize, false);

        newPop.individuals.add(this.bestIndividual);
        this.calculatePopFitness(weightMatrix);
        buildRouletteWheel();

        for (int i = 0; i < popSize; i++) {
            TSPIndividual ind = rollRoulette();
            if (ind != null) {
                newPop.individuals.add(ind);
            }
        }

        return newPop;
    }

    public void updateBestIndividual() {
        /**
         * Search for the best individual in the population
         */

        int bestIndex = 0;
        double best = individuals.get(0).getFitness();
        for (int i = 0; i < individuals.size(); i++) {
            double f = individuals.get(i).getFitness();
            if (f > best) {
                best = f;
                bestIndex = i;
            }
        }

        this.bestIndividual = individuals.get(bestIndex);
    }

    public int getPopSize() {
        return popSize;
    }

    public int getCurrentSize() {
        return individuals.size();
    }

}
