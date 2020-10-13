package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FullyConnectedGraph extends Graph {
    static final String GRAPH_TYPE = "TSP";
    static final String EDGE_WEIGHT_TYPE = "EUC_2D";
    private String graphName;
    private int numberOfNodes;
    public double[][] weightMatrix;


    public FullyConnectedGraph(String filename) throws Exception{
        initFromFile(filename);
        calculateWeight();
    }

    private void initFromFile(String filename) throws Exception {
        /**
         * Create a fully connected graph from a TSPLIB format file
         */
        FileReader reader = new FileReader(filename);
        BufferedReader buffer = new BufferedReader(reader);

        String line;
        while ((line = buffer.readLine()) != null) {
            String[] splitLine = line.split(":");
            switch (splitLine[0].strip()) {
                case "NAME":
                    this.graphName = splitLine[1].strip();
                    break;

                case "COMMENT":
                    // ignore comments
                    break;

                case "TYPE":
                    if (!splitLine[1].strip().equals(GRAPH_TYPE))
                        throw new Exception("Graph type is not correct!");

                    break;

                case "DIMENSION":
                    this.numberOfNodes = Integer.parseInt(splitLine[1].strip());
                    break;

                case "EDGE_WEIGHT_TYPE":
                    if (!splitLine[1].strip().equals(EDGE_WEIGHT_TYPE))
                        throw new Exception("Edge weight type is not correct!");

                    break;

                case "NODE_COORD_SECTION":
                    this.getCoordinates(buffer);
                    break;

                case "EOF":
                    break;

                default:
                    // do nothing
            }
        }

    }

    private void getCoordinates(BufferedReader buffer) throws IOException {
        String line;
        while((line = buffer.readLine()) != null) {
            if (line.equals("EOF"))
                break;

            String[] splitLine = line.split(" ");
            int id = Integer.parseInt(splitLine[0]);
            double x = Double.parseDouble(splitLine[1]);
            double y = Double.parseDouble(splitLine[2]);
            this.vertices.add(new Vertex(x, y, id));
        }
    }

    private void calculateWeight() {
        int n = this.numberOfNodes;
        this.weightMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                weightMatrix[i][j] = Vertex.euclidDistance(vertices.get(i), vertices.get(j));
            }
        }
    }

    public void printWeightMatrix() {
        for(int i = 0; i < this.numberOfNodes; i++) {
            for (int j = 0; j < this.numberOfNodes; j++) {
                System.out.printf("%10.2f", weightMatrix[i][j]);
            }
            System.out.printf("\n");
        }
    }

    private double getWeight(int id1, int id2) {
        return weightMatrix[id1 - 1][id2 - 1];
    }

    public int getNumberOfNodes() {
        return this.numberOfNodes;
    }

    public String getGraphName() {
        return this.graphName;
    }

}
