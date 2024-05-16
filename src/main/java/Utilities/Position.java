package Utilities;

public final class Position {
    public int x;
    public int y;
    public Position() {
    }
    public Position(int x, int y) {
        Set(x, y);
    }
    public Position(Position p) {
        Set(p);
    }
    public void Set(int _x, int _y) {
        x = _x;y = _y;
    }
    public void Set(Position p) {
        if (p != null) {
            x = p.x; y = p.y;
        } else {
            x = 0; y = 0;
        }
    }
    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " (" + x + "," + y + ")";
    }
}

