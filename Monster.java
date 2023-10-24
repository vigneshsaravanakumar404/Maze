public class Monster extends MazeElement {

    private double x;
    private double y;
    private double velocity;
    private char axis;
    private boolean goingPositive;

    public Monster(Location loc, int size, String imgString, double velocity, char axis) {
        super(loc, imgString);
        this.x = loc.getX();
        this.y = loc.getY();
        this.velocity = velocity;
        this.axis = axis;
        this.goingPositive = true;
    }

    // Override or create methods to move the monster
    public void move(char[][] maze) {
        if (axis == 'X') {
            // Check for walls
            if (goingPositive && maze[(int) Math.ceil(x + velocity)][(int) y] == '#') {
                goingPositive = false; // Turn around
            } else if (!goingPositive && maze[(int) Math.floor(x - velocity)][(int) y] == '#') {
                goingPositive = true; // Turn around
            }

            // Move
            if (goingPositive) {
                x += velocity;
            } else {
                x -= velocity;
            }
        } else if (axis == 'Y') {
            // Check for walls
            if (goingPositive && maze[(int) x][(int) Math.ceil(y + velocity)] == '#') {
                goingPositive = false; // Turn around
            } else if (!goingPositive && maze[(int) x][(int) Math.floor(y - velocity)] == '#') {
                goingPositive = true; // Turn around
            }

            // Move
            if (goingPositive) {
                y += velocity;
            } else {
                y -= velocity;
            }
        }
        System.out.println("x: " + x + ", y: " + y + ", Maze: " + maze[(int) x][(int) y]);

    }

    public void moveBasedOnTimer(char[][] maze) {
        if (axis == 'X') {
            double nextX = goingPositive ? x + velocity : x - velocity;
            if (maze[(int) Math.round(nextX)][(int) Math.round(y)] == '#') {
                goingPositive = !goingPositive; // Reverse direction
            } else {
                x = nextX;
            }
        } else if (axis == 'Y') {
            double nextY = goingPositive ? y + velocity : y - velocity;
            if (maze[(int) Math.round(x)][(int) Math.round(nextY)] == '#') {
                goingPositive = !goingPositive; // Reverse direction
            } else {
                y = nextY;
            }
        }
        System.out.println("x: " + x + ", y: " + y + ", Maze: " + maze[(int) x][(int) y]);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}