public class Explorer extends MazeElement {

    private int moveCount = 0;

    public Explorer(Location loc, int size, String imgString) {
        super(loc, imgString);
        setOrientation('E');
    }

    /* Getters and setters */
    public char getDirection() {
        return getOrientation();
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setLoc(Location loc) {
        super.setLoc(loc);
    }

    /*
     * Moves the explorer forward one space in the direction it is facing
     */
    public void move(int key, char[][] maze) {
        Location previousLoc = getLoc();
        switch (key) {
            case 38: // Up arrow key
            case 87: // W key
                moveForward(maze);
                break;
            case 37: // Left arrow key
            case 65: // A key
                turnLeft();
                break;
            case 39: // Right arrow key
            case 68: // D key
                turnRight();
                break;
        }
        Location newLoc = getLoc();
        if (!previousLoc.equals(newLoc)) {
            moveCount++;
        }
    }

    /*
     * Resets the move count to 0
     */
    public void resetMoveCount() {
        moveCount = 0;
    }
}
