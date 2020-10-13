package geneticalgorithm;

public class GAConfig {
    public int popSize;
    public double crossoverRate;
    public double mutationRate;
    public int maxIteration;
    public int patience;

    public GAConfig() {
        popSize = 200;
        crossoverRate = 0.25;
        mutationRate = 0.01;
        maxIteration = 1000;
        patience = 100;
    }
}
