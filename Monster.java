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
        int newRow = row, newCol = col;
        char newDirection = direction;

        switch (direction) {
            case 'N':
                newRow = row - 1;
                newDirection = canMove(newRow, col) ? 'N' : 'S';
                break;
            case 'S':
                newRow = row + 1;
                newDirection = canMove(newRow, col) ? 'S' : 'N';
                break;
            case 'E':
                newCol = col + 1;
                newDirection = canMove(row, newCol) ? 'E' : 'W';
                break;
            case 'W':
                newCol = col - 1;
                newDirection = canMove(row, newCol) ? 'W' : 'E';
                break;
        }

        if (newDirection == direction) {
            loc.setX(newRow);
            loc.setY(newCol);
        } else {
            direction = newDirection;
            loc.setX(row + (newDirection == 'S' ? 1 : newDirection == 'N' ? -1 : 0));
            loc.setY(col + (newDirection == 'E' ? 1 : newDirection == 'W' ? -1 : 0));
        }
    }
}