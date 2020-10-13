package graph;

import java.util.*;

public class Cycle extends LinkedList<Vertex> {
    protected Vertex headVertex;
    protected Graph graph;
    protected boolean lengthChanged;
    protected double length;

    public Cycle() {
        super();
    }

    public Cycle (FullyConnectedGraph graph) {
        super(graph.vertices);
        this.graph = graph;
        this.headVertex = graph.getFirstVertex();
        this.lengthChanged = true;
    }

    public double getLength() {
        if (!lengthChanged)
            return length;

        length = 0;
        ListIterator<Vertex> iter = this.listIterator();

        Vertex firstVertex = iter.next();
        Vertex currentVertex = firstVertex;
        Vertex nextVertex;
        while (iter.hasNext()){
            nextVertex = iter.next();
            length += Vertex.euclidDistance(currentVertex, nextVertex);
            currentVertex = nextVertex;
        }
        length += Vertex.euclidDistance(currentVertex, firstVertex);
        lengthChanged = false;

        return length;
    }

    private int linearIndexSearch(Vertex vertex) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(vertex))
                return i;
        }

        return -1;
    }

    private void swap(Vertex vertex1, Vertex vertex2) {
        int index1 = linearIndexSearch(vertex1);
        int index2 = linearIndexSearch(vertex2);

        if (index1 == -1 || index2 == -1) {
            System.out.println("Swap " + vertex1 + " and " + vertex2 + " failed!");
            return;
        }

        Vertex temp;
        temp = this.get(index1);
        this.set(index1, this.get(index2));
        this.set(index2, temp);
        lengthChanged = true;
    }

//    public boolean isNextTo(Node node1, Node node2) {
//        return true;
//    }
}
