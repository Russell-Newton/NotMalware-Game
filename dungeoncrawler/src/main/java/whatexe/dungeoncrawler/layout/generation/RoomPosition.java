package whatexe.dungeoncrawler.layout.generation;

import whatexe.dungeoncrawler.layout.Direction;

import java.util.Objects;

import static whatexe.dungeoncrawler.layout.Direction.*;

public class RoomPosition {

    private final int x;
    private final int y;

    public RoomPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction getPrimaryDirectionFrom(RoomPosition a, RoomPosition b) {
        int diffX = b.x - a.x;
        int diffY = b.y - a.y;

        if (Math.abs(diffY) > Math.abs(diffX)) {
            if (diffY > 0) {
                return DOWN;
            }
            return UP;
        }
        if (diffX > 0) {
            return RIGHT;
        }
        return LEFT;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public RoomPosition getNeighbor(Direction direction) {
        switch (direction) {
        case UP:
            return getAbove();
        case LEFT:
            return getLeft();
        case RIGHT:
            return getRight();
        case DOWN:
            return getBelow();
        default:
            throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    public RoomPosition getAbove() {
        return new RoomPosition(x, y - 1);
    }

    public RoomPosition getLeft() {
        return new RoomPosition(x - 1, y);
    }

    public RoomPosition getRight() {
        return new RoomPosition(x + 1, y);
    }

    public RoomPosition getBelow() {
        return new RoomPosition(x, y + 1);
    }

    public int distance(RoomPosition other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomPosition that = (RoomPosition) o;
        return x == that.x && y == that.y;
    }

    @Override
    public String toString() {
        return "RoomPosition{" + "x=" + x + ", y=" + y + '}';
    }
}
