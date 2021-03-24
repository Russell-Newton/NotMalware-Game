package whatexe.dungeoncrawler.entities.motionsupport;

import java.util.Objects;

public class Line {
    private final Vector start;
    private final Vector end;

    public Line(Vector start, Vector end) {
        assert start.size() == end.size() && start.size() == 2;
        this.start = start;
        this.end = end;
    }

    public static Vector getIntersection(Line l1, Line l2) {
        double x1 = l1.start.get(0);
        double x2 = l1.end.get(0);
        double x3 = l2.start.get(0);
        double x4 = l2.end.get(0);
        double y1 = l1.start.get(1);
        double y2 = l1.end.get(1);
        double y3 = l2.start.get(1);
        double y4 = l2.end.get(1);
        double det = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        if (det == 0) {
            // lines are parallel
            return null;
        }

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / det;
        double u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / det;
        Vector point = l1.start.plus(l1.tangent().scaledBy(t));
        if ((0 <= t && t <= 1) && (0 <= u && u <= 1)) {
            return point;
        }
        return null;
    }

    public static double distanceToPoint(Line line, Vector point) {
        assert point.size() == 2;

        Vector dStartEnd = line.tangent();   // end - start
        Vector dStartPoint = point.minus(line.start);    // point - start
        Vector dEndStart = dStartEnd.scaledBy(-1);   // start - end
        Vector dEndPoint = point.minus(line.end);        // point - end

        double aStartToPoint = dStartEnd.angleTo(dStartPoint);
        double aEndToPoint = dEndStart.angleTo(dEndPoint);
        if (aStartToPoint > Math.PI / 2) {
            // past start of the line
            return Math.abs(point.distanceTo(line.start));
        }
        if (aEndToPoint > Math.PI / 2) {
            // past end of the line
            return Math.abs(point.distanceTo(line.end));
        }

        return Math.abs(dStartEnd.get(0) * dStartPoint.get(1)
                                - dStartEnd.get(1) * dStartPoint.get(0))
                / dStartEnd.magnitude();
    }

    public static Vector tangent(Line line) {
        return line.end.minus(line.start);
    }

    public static Vector midpoint(Line line) {
        return line.start.plus(line.end.plus(line.start.scaledBy(-1)).scaledBy(0.5));
    }

    public static Vector projectVector(Line line, Vector vector) {
        Vector lineVector = line.tangent();

        return lineVector.scaledBy(vector.dot(lineVector) / lineVector.dot(lineVector));
    }

    public static Vector projectVector(Vector lineVector, Vector vector) {
        return Line.projectVector(new Line(lineVector, lineVector.scaledBy(1.1)), vector);
    }

    public static Vector normal(Line line) {
        Vector tangent = line.tangent();
        return new Vector(-tangent.get(1), tangent.get(0));
    }

    public static double length(Line line) {
        return line.tangent().magnitude();
    }

    public Vector intersectionWith(Line other) {
        return Line.getIntersection(this, other);
    }

    public double distanceToPoint(Vector point) {
        return Line.distanceToPoint(this, point);
    }

    public Vector tangent() {
        return Line.tangent(this);
    }

    public Vector midpoint() {
        return Line.midpoint(this);
    }

    public Vector projectVector(Vector vector) {
        return Line.projectVector(this, vector);
    }

    public Vector normal() {
        return Line.normal(this);
    }

    public double length() {
        return Line.length(this);
    }

    public Vector getStart() {
        return start;
    }

    public Vector getEnd() {
        return end;
    }

    @Override
    public int hashCode() {

        return Objects.hash(start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(start, line.start) && Objects.equals(end, line.end);
    }

    @Override
    public String toString() {
        return "Line{" + "start=" + start + ", end=" + end + '}';
    }
}
