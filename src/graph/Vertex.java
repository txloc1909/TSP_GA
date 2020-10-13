package graph;

public class Vertex {
    private double x, y;
    private int id;

    public Vertex(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public Vertex(double x, double y, int id) {
        this(x, y);
        this.id = id;
    }

    public static double euclidDistance(Vertex vertex1, Vertex vertex2) {
        return Math.sqrt(
                Math.pow(vertex1.x - vertex2.x, 2)
                        + Math.pow(vertex1.y - vertex2.y, 2)
        );
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Vertex))
            return false;

        boolean sameID = this.getID() == ((Vertex) o).getID();
        boolean sameX = this.getX() == ((Vertex) o).getX();
        boolean sameY = this.getY() == ((Vertex) o).getY();

        return sameID && sameX && sameY;
    }

    private void setX(double x) {
        this.x = x;
    }

    private void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getID() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("Vertex %d: (%.2f, %.2f)\n", getID(), getX(), getY());
    }

}
