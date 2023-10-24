import javax.swing.JComponent;

public class Door {

    private int x;
    private int y;
    private char code;
    private int keyUnlockedBy;

    public Door(int x, int y, char code) {
        this.x = x;
        this.y = y;
        this.code = code;

        switch (code) {
            case 'A':
                keyUnlockedBy = 1;
                break;
            case 'B':
                keyUnlockedBy = 2;
                break;
            case 'C':
                keyUnlockedBy = 3;
                break;
            case 'D':
                keyUnlockedBy = 4;
                break;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getCode() {
        return code;
    }

    public int getKeyUnlockedBy() {
        return keyUnlockedBy;
    }

    public JComponent getKey() {
        return null;
    }

}
