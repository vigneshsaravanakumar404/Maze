public class Monster extends MazeElement {

    private char direction;
    private char[][] maze;

    public Monster(Location loc, String imageString, char direction, char[][] maze) {
        super(loc, imageString);
        this.direction = direction;
        this.maze = maze;
    }

    // Create a move method so that it moves forward in the direction it is facing,
    // if there is a wall it turns around only
    private boolean canMove(int newRow, int newCol) {
        if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length) {
            if (maze[newRow][newCol] == ' ') {
                return true;
            }
        }
        return false;
    }

    public void move() {
        int row = loc.getX();
        int col = loc.getY();

        if (direction == 'N') {
            if (canMove(row - 1, col)) {
                loc.setX(row - 1);
            } else {
                loc.setX(row + 1); // move in opposite direction
                direction = 'S';
            }
        } else if (direction == 'S') {
            if (canMove(row + 1, col)) {
                loc.setX(row + 1);
            } else {
                loc.setX(row - 1); // move in opposite direction
                direction = 'N';
            }
        } else if (direction == 'E') {
            if (canMove(row, col + 1)) {
                loc.setY(col + 1);
            } else {
                loc.setY(col - 1); // move in opposite direction
                direction = 'W';
            }
        } else if (direction == 'W') {
            if (canMove(row, col - 1)) {
                loc.setY(col - 1);
            } else {
                loc.setY(col + 1); // move in opposite direction
                direction = 'E';
            }
        }
    }
}