/**
 * A simple class representing a location in 2D space.
 */
public final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + getX() + "," + getY() + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point) other).getX() == this.getX() && ((Point) other).getY() == this.getY();
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + getX();
        result = result * 31 + getY();
        return result;
    }
    public int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.getX() - p2.getX();
        int deltaY = p1.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
