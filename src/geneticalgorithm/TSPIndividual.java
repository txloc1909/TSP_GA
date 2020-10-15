package geneticalgorithm;

import datastructure.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TSPIndividual {
    /**
     * Implement Individual object for TSP solved by GA
     * Using TSP path representation
     */

    private final int geneSize;
    private final ArrayList<Integer> genotype;
    private double fitness;
    private double tourLength;

    public TSPIndividual (int geneSize, boolean randomInit) {
        this.geneSize = geneSize;
        genotype = new ArrayList<>(geneSize);

        if (randomInit) {
            for (int i = 1; i <= this.geneSize; i++) {
                this.genotype.add(i);
            }
            Collections.shuffle(this.genotype);
        }
    }

    public TSPIndividual (ArrayList<Integer> gene) {
        this.geneSize = gene.size();
        this.genotype = new ArrayList<>(gene);
    }

    public TSPIndividual copy() {
        return new TSPIndividual(new ArrayList<>(this.genotype));
    }

    public void calculateFitness(double[][] weightMatrix) {
        double length = 0;
        for(int i = 1; i < genotype.size(); i++) {
            length += weightMatrix[genotype.get(i) - 1][genotype.get(i-1) - 1];
        }
        length += weightMatrix[genotype.get(geneSize-1) - 1][genotype.get(0) - 1];

        this.fitness = 1000 * geneSize / length; // x1000 to scale up the fitness
        this.tourLength = length;
    }

    public static Pair<TSPIndividual, TSPIndividual> crossover(Pair<TSPIndividual, TSPIndividual> parents) {
        /**
         * OX Crossover operator
         * Implement: Ts. Nguyễn Đình Thúc, "Trí tuệ nhân tạo - Lập trình tiến hóa", p.192
         */

        Random rand = new Random();
        TSPIndividual parent1 = parents.getFirst();
        TSPIndividual parent2 = parents.getSecond();
        int size = parent1.getGeneSize();
        TSPIndividual offspring1 = new TSPIndividual(size, false);
        TSPIndividual offspring2 = new TSPIndividual(size, false);

        int cutPoint1, cutPoint2;
        do {
            cutPoint1 = rand.nextInt(size);
            cutPoint2 = rand.nextInt(size);
        } while (cutPoint1 >= cutPoint2); // make sure cutPoint1 < cutPoint2

        boolean[] marked1 = new boolean[size];
        boolean[] marked2 = new boolean[size];

        for(int i = cutPoint1; i < cutPoint2; i++) {
            marked1[parent1.genotype.get(i) - 1] = true;
            marked2[parent2.genotype.get(i) - 1] = true;
        }

        for(int i = cutPoint1; i < cutPoint2; i++) {
            offspring1.genotype.add(parent1.genotype.get(i));
            offspring2.genotype.add(parent2.genotype.get(i));
        }

        int i1 = cutPoint2, i2 = cutPoint2;
        while(offspring1.genotype.size() < size || offspring2.genotype.size() < size) {
            int allele1 = parent1.genotype.get(i1);
            int allele2 = parent2.genotype.get(i2);

            if (!marked2[allele1 - 1] && offspring2.genotype.size() < size) {
                offspring2.genotype.add(allele1);
            }

            if (!marked1[allele2 - 1] && offspring1.genotype.size() < size) {
                offspring1.genotype.add(allele2);
            }

            if ((++i1) == size) {
                i1 = 0;
            }

            if ((++i2) == size) {
                i2 = 0;
            }
        }

        return new Pair<>(offspring1, offspring2);
    }

    public static TSPIndividual mutate(TSPIndividual individual) {
        /**
         * Mutation operator
         *
         * Swap 2 random alleles in the genotype
         */
        Random rand = new Random();
        int swapPoint1, swapPoint2;
        do {
            swapPoint1 = rand.nextInt(individual.geneSize);
            swapPoint2 = rand.nextInt(individual.geneSize);
        } while (swapPoint1 == swapPoint2); // make sure 2 swap points are different


        TSPIndividual output = individual.copy();

        // swapping
        int tmp = output.genotype.get(swapPoint1);
        output.genotype.set(swapPoint1, output.genotype.get(swapPoint2));
        output.genotype.set(swapPoint2, tmp);

        return output;
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("(");
        for (int i : genotype) {
            s.append(i);
            s.append(", ");
        }
        s.append(")");
        return s.toString();
    }

    public double getTourLength() {
        return this.tourLength;
    }

    public double getFitness() {
        return this.fitness;
    }

    public int getGeneSize() {
        return this.geneSize;
    }
}
