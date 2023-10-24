import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class MazeElement {

	// Instance variables
	protected Location loc;
	private BufferedImage img;
	private char orientation; // 'N' for North, 'S' for South, 'E' for East, 'W' for West

	/*
	 * Constructor
	 */
	public MazeElement(Location loc, String imgString) {
		this.loc = loc;
		this.orientation = 'N'; // Initially set to North
		try {
			img = ImageIO.read(new File(imgString));
		} catch (IOException e) {
			System.out.println("Image ->[" + imgString + "] not loaded");
		}
	}

	/*
	 * Getters and setters
	 */
	public BufferedImage getImg() {
		return img;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public char getOrientation() {
		return orientation;
	}

	public void setOrientation(char orientation) {
		this.orientation = orientation;
	}

	/*
	 * Moves the explorer forward one space in the direction it is facing
	 */
	public void moveForward(char[][] maze) {
		int x = loc.getX();
		int y = loc.getY();
		int newX = x, newY = y;

		switch (orientation) {
			case 'N':
				newX = x - 1;
				break;
			case 'S':
				newX = x + 1;
				break;
			case 'E':
				newY = y + 1;
				break;
			case 'W':
				newY = y - 1;
				break;
		}
		Location newLoc = new Location(newX, newY);
		if (maze[newX][newY] != '#' && maze[newX][newY] != 'A' && maze[newX][newY] != 'B' && maze[newX][newY] != 'C'
				&& maze[newX][newY] != 'D') {
			this.loc = newLoc;
		}
	}

	/*
	 * Turns the explorer 90 degrees to the left
	 */
	public void turnLeft() {
		switch (orientation) {
			case 'N':
				orientation = 'W';
				break;
			case 'S':
				orientation = 'E';
				break;
			case 'E':
				orientation = 'N';
				break;
			case 'W':
				orientation = 'S';
				break;
		}
	}

	/*
	 * Turns the explorer 90 degrees to the right
	 */
	public void turnRight() {
		switch (orientation) {
			case 'N':
				orientation = 'E';
				break;
			case 'S':
				orientation = 'W';
				break;
			case 'E':
				orientation = 'S';
				break;
			case 'W':
				orientation = 'N';
				break;
		}
	}
}