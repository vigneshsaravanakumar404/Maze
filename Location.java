public class Location {

    // Instance variables
    int x;
    int y;
    double preciseX;
    double preciseY;

    // Constructors
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        this.preciseX = x;
        this.preciseY = y;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Increment methods
    public void incX(int dx) {
        this.x += dx;
        this.preciseX += dx;
    }

    public void incY(int dy) {
        this.y += dy;
        this.preciseY += dy;
    }

    // Equals method
    public boolean equals(Location other) {
        return this.x == other.x && this.y == other.y;
    }
}
