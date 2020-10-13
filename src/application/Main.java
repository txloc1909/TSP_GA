package application;

import geneticalgorithm.GAConfig;
import geneticalgorithm.GeneticAlgorithm;
import graph.FullyConnectedGraph;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        String filename = System.getProperty("user.dir") + "\\src\\sampledata\\dj38.tsp";

        try {
            FullyConnectedGraph g = new FullyConnectedGraph(filename);
            System.out.println("Graph: " + g.getGraphName());

            GAConfig config = new GAConfig();
            GeneticAlgorithm ga = new GeneticAlgorithm(config, g);

            ArrayList<Double> result = new ArrayList<>(30);

            for (int i = 0; i < 30; i++) {
                ga.search();
                result.add(ga.getBestLength());
            }

            printStat(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void printStat(ArrayList<Double> result) {
        double sum = 0;
        double stdDeviation = 0;
        for(double l: result) {
            sum += l;
        }

        double average = sum / result.size();
        for(double l: result) {
            stdDeviation += Math.pow(l - average, 2);
        }
        stdDeviation = Math.sqrt(stdDeviation) / result.size();

        System.out.println("Average: " + average);
        System.out.println("Max: " + Collections.max(result));
        System.out.println("Min: " + Collections.min(result));
        System.out.println("Std deviation: " + stdDeviation);
    }
}
