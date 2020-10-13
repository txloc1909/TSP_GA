package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    protected List<Vertex> vertices = new ArrayList<>();

    public Vertex getFirstVertex() {

        for (Vertex n : vertices) {
            if (n.getID() == 1)
                return n;
        }

        return null;
    }

    public void printAllNodes() {
        for(Vertex n: vertices) {
            System.out.println(n);
        }

//        for (int i = 0; i < vertices.size(); i++) {
//            System.out.println("Index " + i + ": " + vertices.get(i));
//        }
    }
}
