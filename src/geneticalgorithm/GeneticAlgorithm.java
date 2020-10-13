package geneticalgorithm;

import datastructure.Pair;
import graph.FullyConnectedGraph;

import java.util.Random;

public class GeneticAlgorithm {
    private Population population;
    private final FullyConnectedGraph graph;
    final int popSize;
    final double CROSSOVER_RATE;
    final double MUTATION_RATE;
    private final int maxIteration;
    private final int patience;
    private final Random rand;

    public GeneticAlgorithm (GAConfig config, FullyConnectedGraph graph) {
        this.graph = graph;
        this.popSize = config.popSize;
        this.CROSSOVER_RATE = config.crossoverRate;
        this.MUTATION_RATE = config.mutationRate;

        this.maxIteration = config.maxIteration;
        this.patience = config.patience;
        this.rand = new Random();
    }

    private Pair<TSPIndividual, TSPIndividual> doCrossover() {
        int i, j;
        do {
            i = rand.nextInt(population.individuals.size());
            j = rand.nextInt(population.individuals.size());
        } while(i == j);

        Pair<TSPIndividual, TSPIndividual> parents =
                new Pair<>(population.individuals.get(i),
                            population.individuals.get(j));

        if (rand.nextDouble() < CROSSOVER_RATE) {
            return TSPIndividual.crossover(parents);
        } else {
            return null;
        }
    }

    private TSPIndividual doMutation() {
        TSPIndividual ind = population.individuals.get(
                rand.nextInt(population.individuals.size())
        );

        if (rand.nextDouble() < MUTATION_RATE) {
            return TSPIndividual.mutate(ind);
        } else {
            return null;
        }
    }

    private void breedOffsprings() {
        /**
         * Creating offsprings and add to population
         */

        for (int i = 0; i < population.getPopSize(); i++) {
            Pair<TSPIndividual, TSPIndividual> p = doCrossover();
            if (p != null) {
                population.individuals.add(p.getFirst());
                population.individuals.add(p.getSecond());
            }

            TSPIndividual m = doMutation();
            if (m != null) {
                population.individuals.add(m);
            }
        }
    }

    public void search() {
        double[][] weightMatrix = graph.weightMatrix;
        int nUnimproved = 0; // number of consecutive unimproved generations
        population = new Population(popSize, graph.getNumberOfNodes(), true);
        population.calculatePopFitness(weightMatrix);
        population.updateBestIndividual();
        double currentBestFitness = getBestFitness();

        // GA's main loop
        int generation;
        for (generation = 0;
             generation < maxIteration && nUnimproved < patience;
             generation++) {

            breedOffsprings();
            population = population.evaluatePopulation(weightMatrix);
            population.calculatePopFitness(weightMatrix);
            population.updateBestIndividual();

            //System.out.println("Generation " + generation + ":");
            //System.out.println("Best Fitness: " + getBestFitness());
            //System.out.println("Best length: " + getBestLength());
            //System.out.print("\n");

            if (getBestFitness() > currentBestFitness) {
                nUnimproved = 0;
            } else {
                nUnimproved++;
            }

            currentBestFitness = getBestFitness();
        }
    }

    public TSPIndividual getBestIndividual() {
        return this.population.bestIndividual;
    }

    public double getBestFitness() {
        return this.getBestIndividual().getFitness();
    }

    public double getBestLength() {
        return this.getBestIndividual().getTourLength();
    }
}
