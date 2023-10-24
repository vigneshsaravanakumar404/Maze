public class Key {

    private int x;
    private int y;
    private char doorUnlocked;
    private int keyNumber;

    public Key(int x, int y, int keyNumber) {
        this.x = x;
        this.y = y;
        this.keyNumber = keyNumber;

        switch (keyNumber) {
            case 1:
                doorUnlocked = 'A';
                break;
            case 2:
                doorUnlocked = 'B';
                break;
            case 3:
                doorUnlocked = 'C';
                break;
            case 4:
                doorUnlocked = 'D';
                break;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getDoorUnlocked() {
        return doorUnlocked;
    }

    public int getKeyNumber() {
        return keyNumber;
    }
}
