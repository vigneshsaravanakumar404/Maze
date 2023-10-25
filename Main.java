// Planning Document: https://docs.google.com/document/d/1dufhUD82mlUIdbwCK6SsGjpbT6wpN2yvbTwExrpknxU/edit#heading=h.w4876d7fbz4z
// Rubric: https://docs.google.com/document/d/1o3fsglowWLcwkwr3aJ1qoKf7fTowimclXj_rP7YnAdg/edit

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.*;
import java.awt.geom.AffineTransform;

public class Main extends JPanel implements KeyListener, ActionListener {

	// Instance variables
	public JFrame frame;
	private final int size = 25;
	private int currentLevel = 0;
	private char[][] maze;
	private Explorer explorer;
	private Location startLocation;
	private final ArrayList<Key> keys = new ArrayList<>();
	private final ArrayList<Door> doors = new ArrayList<>();
	private boolean is3DView = false, showCongratulationsMessage = false;
	private final ArrayList<Monster> monsters = new ArrayList<>();

	// Constructor
	public Main() {
		setBoard(currentLevel);
		frame = new JFrame("A-Mazing Program");
		int width = 1500;
		int height = 1000;
		frame.setSize(width, height);
		frame.add(this);
		frame.addKeyListener(this);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		explorer = new Explorer(startLocation, size, "explorer1.png");
		repaint();

		// Update Monsters
		Timer timer = new Timer(250, this);
		timer.start();
	}

	// Graphics
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (is3DView) {
			drawMaze3D(g);

		} else {
			drawMaze2D(g);
		}

		if (showCongratulationsMessage) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Font font = new Font("Arial", Font.BOLD, 100);
			g2.setFont(font);
			FontMetrics fm = g2.getFontMetrics();
			int textWidth = fm.stringWidth("Congratulations!");
			int textHeight = fm.getHeight();
			int x = (getWidth() - textWidth) / 2;
			int y = (getHeight() - textHeight) / 2 + fm.getAscent();
			g2.setColor(Color.GRAY);
			g2.drawString("Congratulations!", x + 3, y + 3);
			g2.setColor(Color.PINK);
			g2.drawString("Congratulations!", x, y);
		}

	}

	// KeyListener methods
	public void keyPressed(KeyEvent e) {

		// Move the explorer
		int key = e.getKeyCode();
		explorer.move(key, maze);
		Location newLoc = explorer.getLoc();

		// Toggle 3D view
		if (key == KeyEvent.VK_SPACE) {
			is3DView = !is3DView;
		}

		// Check if explorer lands on the end point ('E')
		if (maze[newLoc.getX()][newLoc.getY()] == 'E') {
			explorer.setLoc(newLoc);
			currentLevel++;
			showCongratulationsMessage = true;
			repaint();

			Timer timer = new Timer(2000, evt -> {
				showCongratulationsMessage = false;
				monsters.clear();
				setBoard(currentLevel);
				frame.setTitle("A-Mazing Program - Level " + (currentLevel + 1));
				((Timer) evt.getSource()).stop();
				repaint();
			});
			timer.setRepeats(false);
			timer.start();
		}

		// Check if you landed on a key
		else if (maze[newLoc.getX()][newLoc.getY()] >= '1' && maze[newLoc.getX()][newLoc.getY()] <= '4') {
			for (int i = 0; i < keys.size(); i++) {
				if (keys.get(i).getX() == newLoc.getX() && keys.get(i).getY() == newLoc.getY()) {
					char doorToUnlock = keys.get(i).getDoorUnlocked();
					maze[newLoc.getX()][newLoc.getY()] = ' ';
					keys.remove(i);

					for (int j = 0; j < doors.size(); j++) {
						if (doors.get(j).getCode() == doorToUnlock) {
							maze[doors.get(j).getX()][doors.get(j).getY()] = ' ';
							doors.remove(j);
							break;
						}
					}
					break;
				}
			}
		} else {
			repaint();
		}

	}

	public void keyReleased(KeyEvent e) {

	}

	// Exit
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 27) {
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent e) {
		// Update the movement of all the monsters
		for (Monster monster : monsters) {
			monster.move();
			repaint();
		}

		// Check if the explorer is on the same square as a monster
		for (Monster monster : monsters) {
			if (monster.getLoc().equals(explorer.getLoc())) {
				// Reset the level
				monsters.clear();
				setBoard(currentLevel);
				frame.setTitle("A-Mazing Program - Level " + (currentLevel + 1));
				repaint();
			}
		}
	}

	// Reads the maze file and stores it as a 2D array
	public void setBoard(int level) {
		String fileName = "maze" + level + ".txt";
		try {
			// Read the file
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			ArrayList<String> lines = new ArrayList<>();
			String line;

			while ((line = br.readLine()) != null) {
				lines.add(line);
			}

			// Store as a 2D array
			int rows = lines.size();
			int cols = lines.get(0).length();
			maze = new char[rows][cols];

			// Identify the points of interest
			int startX = -1, startY = -1;
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					maze[r][c] = lines.get(r).charAt(c);

					if (maze[r][c] == 'S') {
						startX = r;
						startY = c;
						startLocation = new Location(startX, startY);
					} else if (maze[r][c] == '1' || maze[r][c] == '2' || maze[r][c] == '3' || maze[r][c] == '4') {
						keys.add(new Key(r, c, Integer.parseInt(maze[r][c] + "")));
					} else if (maze[r][c] == 'A' || maze[r][c] == 'B' || maze[r][c] == 'C' || maze[r][c] == 'D') {
						doors.add(new Door(r, c, maze[r][c]));
					} else if (maze[r][c] == '^') {
						monsters.add(new Monster(new Location(r, c), "Up-removebg-preview.png", 'N', maze));
					} else if (maze[r][c] == 'v') {
						monsters.add(new Monster(new Location(r, c), "Down-removebg-preview.png", 'S', maze));
					} else if (maze[r][c] == '>') {
						monsters.add(new Monster(new Location(r, c), "Left-removebg-preview.png", 'E', maze));
					} else if (maze[r][c] == '<') {
						monsters.add(new Monster(new Location(r, c), "Right-removebg-preview.png", 'W', maze));
					}

				}
			}

			br.close();

			// Initialize the explorer at the starting point
			if (startX != -1) {
				explorer = new Explorer(new Location(startX, startY), size, "explorer1.png");
			} else {
				throw new IllegalArgumentException("No starting point ('S') found in the maze file.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			System.exit(0);
		} catch (IOException e) {
			System.out.println("An error occurred while reading the file.");
			System.exit(0);
		}
	}

	// Draws the maze in 2D
	public void drawMaze2D(Graphics g) {

		// Draw the maze
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		BufferedImage keyImage = null;
		try {
			keyImage = ImageIO.read(new File("key.png"));
		} catch (IOException e) {
			System.out.println("Image ->[key.png] not loaded");
		}

		// Draw Maze Elements
		for (int r = 0; r < maze.length; r++) {
			for (int c = 0; c < maze[0].length; c++) {
				if (maze[r][c] == '#') {
					g2.setColor(Color.GRAY);
					g2.fillRect(c * size, r * size, size, size); // Wall
				} else if (maze[r][c] == 'S') {
					g2.setColor(Color.decode("#66FF66"));
					g2.fillRect(c * size, r * size, size, size); // Start
				} else if (maze[r][c] == 'E') { // End
					g2.setColor(Color.decode("#FFFF66"));
					g2.fillRect(c * size, r * size, size, size); // End
				} else if (maze[r][c] == '1' || maze[r][c] == '2' || maze[r][c] == '3' || maze[r][c] == '4') {
					if (keyImage != null) {
						g2.drawImage(keyImage, c * size, r * size, size, size, null);
					} else {
						g2.setColor(Color.decode("#FFD700"));
						g2.fillRect(c * size, r * size, size, size); // Key
					}
				} else if (maze[r][c] == 'A' || maze[r][c] == 'B' || maze[r][c] == 'C' || maze[r][c] == 'D') {
					g2.setColor(Color.GRAY);
					g2.fillRect(c * size, r * size, size, size); // Door
				}
				// Draw gridlines
				g2.setColor(Color.gray);
				g2.drawRect(c * size, r * size, size, size);
			}
		}

		// Draw the explorer
		if (explorer != null && explorer.getImg() != null) {
			BufferedImage img = explorer.getImg();
			Location loc = explorer.getLoc();

			char orientation = explorer.getOrientation(); // getOrientation() should return 'N', 'S', 'E', or 'W'

			// Rotate the image based on orientation
			double angle = 0.0;
			switch (orientation) {
				case 'E': // East as the 0 point
					angle = 0.0; // No rotation
					break;
				case 'W': // Opposite of East
					angle = Math.PI; // 180 degrees
					break;
				case 'N': // North
					angle = -Math.PI / 2; // 90 degrees counterclockwise
					break;
				case 'S': // South
					angle = Math.PI / 2; // 90 degrees clockwise
					break;
			}

			// Rotate the image
			AffineTransform txRotate = AffineTransform.getRotateInstance(angle, img.getWidth() / 2.0,
					img.getHeight() / 2.0);
			AffineTransformOp opRotate = new AffineTransformOp(txRotate, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = opRotate.filter(img, null);

			// Scale the image to 0.5 of its original size
			AffineTransform txScale = AffineTransform.getScaleInstance(0.5, 0.5);
			AffineTransformOp opScale = new AffineTransformOp(txScale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = opScale.filter(img, null);

			int drawX = loc.getY() * size;
			int drawY = loc.getX() * size;

			// Adjust to center the scaled image on the grid
			int scaledSize = (int) (size * 0.85);
			int adjustedX = drawX + (size / 2) - (scaledSize / 2);
			int adjustedY = drawY + (size / 2) - (scaledSize / 2);

			// Draw the image
			g2.drawImage(img, adjustedX, adjustedY, scaledSize, scaledSize, null);
		}

		// Draw the monsters
		for (Monster monster : monsters) {
			BufferedImage img = monster.getImg();
			Location loc = monster.getLoc();

			// Rotate the image based on orientation
			double angle = 0.0;

			// Rotate the image
			AffineTransform txRotate = AffineTransform.getRotateInstance(angle, img.getWidth() / 2.0,
					img.getHeight() / 2.0);
			AffineTransformOp opRotate = new AffineTransformOp(txRotate, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = opRotate.filter(img, null);

			// Scale the image to 0.5 of its original size
			AffineTransform txScale = AffineTransform.getScaleInstance(0.5, 0.5);
			AffineTransformOp opScale = new AffineTransformOp(txScale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = opScale.filter(img, null);

			int drawX = loc.getY() * size;
			int drawY = loc.getX() * size;

			// Adjust to center the scaled image on the grid
			int scaledSize = (int) (size * 0.85);
			int adjustedX = drawX + (size / 2) - (scaledSize / 2);
			int adjustedY = drawY + (size / 2) - (scaledSize / 2);

			// Draw the image
			g2.drawImage(img, adjustedX, adjustedY, scaledSize, scaledSize, null);
		}

		// Display move count at bottom of page
		int vert = maze.length * size + 2 * size - 10;
		g2.setFont(new Font("Arial", Font.BOLD, 20));
		g2.setColor(Color.PINK);
		g2.drawString("Moves: " + explorer.getMoveCount(), size, vert); // Using explorer.getMoveCount()
	}

	// Draws the maze in 3D
	public void drawMaze3D(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());

		// Find the number of grid spaces from exlorer to wall in front of the explorer
		int x = explorer.getLoc().getX();
		int y = explorer.getLoc().getY();
		int count = 0;
		while (maze[x][y] != '#') {
			count++;
			switch (explorer.getOrientation()) {
				case 'N':
					x--;
					break;
				case 'S':
					x++;
					break;
				case 'E':
					y++;
					break;
				case 'W':
					y--;
					break;
			}
		}
		if (count > 5) {
			count = 5;
		}

		if (true) {
			int size3D = 600, backWall = 350;
			int ULC = 100, LRC = ULC + size3D;
			int shrink = (size3D - backWall) / 5;

			// Back wall
			g2.setColor(Color.GRAY);
			g2.fillRect(ULC, ULC, size3D, size3D);

			// left wall
			for (int n = 0; n < count; n++) {
				int checkX = explorer.getLoc().getX();
				int checkY = explorer.getLoc().getY();
				boolean isOpenSpace = false;

				char direction = explorer.getOrientation();
				switch (direction) {
					case 'N':
						// Check that n squares north is within the maze boundaries
						if (checkX - n >= 0) {
							checkY -= 1;
							checkX -= n;
						}
						break;
					case 'S':
						// Check that n squares south is within the maze boundaries
						if (checkX + n < maze.length) {
							checkY += 1;
							checkX += n;
						}
						break;
					case 'E':
						// Check that n squares east is within the maze boundaries
						if (checkY + n < maze[0].length) {
							checkX -= 1;
							checkY += n;
						}
						break;
					case 'W':
						// Check that n squares west is within the maze boundaries
						if (checkY - n >= 0) {
							checkX += 1;
							checkY -= n;
						}
						break;
				}

				// Check if the adjusted position is within the maze boundaries
				if (checkX >= 0 && checkX < maze.length && checkY >= 0 && checkY < maze[0].length) {
					isOpenSpace = (maze[checkX][checkY] == ' ') || (maze[checkX][checkY] == '1')
							|| (maze[checkX][checkY] == '2') || (maze[checkX][checkY] == '3')
							|| (maze[checkX][checkY] == '4') || (maze[checkX][checkY] == 'E');

				}

				// Check if this cell is an open space
				if (!isOpenSpace) {
					// Logic to draw the wall segment here
					int[] xLocs = { ULC + shrink * n, ULC + shrink * (n + 1), ULC + shrink * (n + 1),
							ULC + shrink * n };
					int[] yLocs = { ULC + shrink * n, ULC + shrink * (n + 1), LRC - shrink * (n + 1),
							LRC - shrink * n };
					int grayValue = 200 - n * 8; // Change intensity based on n

					Polygon leftWall = new Polygon(xLocs, yLocs, xLocs.length);
					g2.setColor(new Color(grayValue, grayValue, grayValue));
					g2.fill(leftWall);
					g2.setColor(Color.BLACK);
					g2.draw(leftWall);
				} else {

					// Draw a black wall segment
					int[] xLocs = { ULC + shrink * n, ULC + shrink * (n + 1), ULC + shrink * (n + 1),
							ULC + shrink * n };
					int[] yLocs = { ULC + shrink * n, ULC + shrink * (n + 1), LRC - shrink * (n + 1),
							LRC - shrink * n };
					Polygon leftWall = new Polygon(xLocs, yLocs, xLocs.length);
					g2.setColor(Color.BLACK);
					g2.fill(leftWall);
					g2.setColor(Color.BLACK);
					g2.draw(leftWall);

				}
			}

			// Right Wall
			for (int n = 0; n < count; n++) {
				int checkX = explorer.getLoc().getX();
				int checkY = explorer.getLoc().getY();
				boolean isOpenSpace = false;

				char direction = explorer.getOrientation();
				switch (direction) {
					case 'N':
						// Check that n squares north is within the maze boundaries
						if (checkX - n >= 0) {
							checkY += 1;
							checkX -= n;
						}
						break;
					case 'S':
						// Check that n squares south is within the maze boundaries
						if (checkX + n < maze.length) {
							checkY -= 1;
							checkX += n;
						}
						break;
					case 'E':
						// Check that n squares east is within the maze boundaries
						if (checkY + n < maze[0].length) {
							checkX += 1;
							checkY += n;
						}
						break;
					case 'W':
						// Check that n squares west is within the maze boundaries
						if (checkY - n >= 0) {
							checkX -= 1;
							checkY -= n;
						}
						break;
				}

				// Check if the adjusted position is within the maze boundaries
				if (checkX >= 0 && checkX < maze.length && checkY >= 0 && checkY < maze[0].length) {
					isOpenSpace = maze[checkX][checkY] == ' ';
				}

				// Coordinates for drawing the right wall trapezoid
				int[] xLocs = { LRC - shrink * n, LRC - shrink * (n + 1), LRC - shrink * (n + 1), LRC - shrink * n };
				int[] yLocs = { LRC - shrink * n, LRC - shrink * (n + 1), ULC + shrink * (n + 1), ULC + shrink * n };

				// Creating a Polygon object for the right wall
				Polygon rightWall = new Polygon(xLocs, yLocs, xLocs.length);

				if (!isOpenSpace) {
					// Logic to draw the wall segment here if it's not an open space
					int grayValue = 200 - n * 8; // Change intensity based on n
					g2.setColor(new Color(grayValue, grayValue, grayValue));
				} else {
					// Draw a black wall segment if it's an open space
					g2.setColor(Color.BLACK);
				}

				// Filling and drawing the polygon
				g2.fill(rightWall);
				g2.setColor(Color.BLACK);
				g2.draw(rightWall);
			}

			// bottom
			for (int n = 0; n < count; n++) {
				int[] yLocs = { LRC - shrink * n, LRC - shrink * (n + 1), LRC - shrink * (n + 1), LRC - shrink * n };
				int[] xLocs = { LRC - shrink * n, LRC - shrink * (n + 1), ULC + shrink * (n + 1), ULC + shrink * n };

				Polygon leftWall = new Polygon(xLocs, yLocs, xLocs.length);
				g2.setColor(Color.WHITE);
				g2.fill(leftWall);
				g2.setColor(Color.BLACK);
				g2.draw(leftWall);

			}

			// upper wall
			for (int n = 0; n < count; n++) {
				int[] yLocs = { ULC + shrink * n, ULC + shrink * (n + 1), ULC + shrink * (n + 1), ULC + shrink * n };
				int[] xLocs = { ULC + shrink * n, ULC + shrink * (n + 1), LRC - shrink * (n + 1), LRC - shrink * n };

				Polygon leftWall = new Polygon(xLocs, yLocs, xLocs.length);
				g2.setColor(Color.WHITE);
				g2.fill(leftWall);
				g2.setColor(Color.BLACK);
				g2.draw(leftWall);
			}

		}

	}

	public static void main(String[] args) {
		Main app = new Main();
		app.setFocusable(true);
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		device.setFullScreenWindow(app.frame);
	}
}