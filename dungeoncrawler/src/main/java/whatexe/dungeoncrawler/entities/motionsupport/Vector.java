package whatexe.dungeoncrawler.entities.motionsupport;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class Vector {

    private static final double EPSILON = 1e-6;

    private final double[] v;

    public Vector(Vector other) {
        this(other.v);
    }

    public Vector(double... v) {
        this.v = v;
    }

    public static Vector scalarProduct(Vector vector, double scalar) {
        double[] product = new double[vector.size()];
        operate(vector.size(), i -> product[i] = vector.get(i) * scalar);

        return new Vector(product);
    }

    public static Vector unit(Vector vector) {
        assert !vector.isZero();

        return vector.scaledBy(1 / vector.magnitude());
    }

    public static double dotProduct(Vector u1, Vector u2) {
        assertSameLengths(u1, u2);

        double[] sum = new double[1];
        operate(u1.size(), i -> sum[0] += u1.get(i) * u2.get(i));

        return sum[0];
    }

    public static double angleBetween(Vector u1, Vector u2) {
        assertSameLengths(u1, u2);

        double val = Vector.dotProduct(u1, u2) / (u1.magnitude() * u2.magnitude());

        return Math.acos(val);
    }

    public static Vector sum(Vector u1, Vector u2) {
        assertSameLengths(u1, u2);

        double[] sum = new double[u1.size()];
        operate(u1.size(), i -> sum[i] = u1.get(i) + u2.get(i));

        return new Vector(sum);
    }

    private static void assertSameLengths(Vector... vectors) {
        operate(vectors.length, i -> {
            assert vectors[i].size() == vectors[0].size();
        });
    }

    private static void operate(int count, IntConsumer operator) {
        IntStream.range(0, count).forEach(operator);
    }

    public static boolean containsNaN(Vector vector) {
        boolean[] containsNaN = {false};
        operate(vector.size(), i -> containsNaN[0] |= Double.isNaN(vector.get(i)));

        return containsNaN[0];
    }

    public static double pnorm(Vector vector, double p) {
        return Math.pow(powerSum(vector, p), 1 / p);
    }

    public static double powerSum(Vector vector, double p) {
        assert p >= 1;

        double[] sum = new double[1];
        operate(vector.size(), i -> sum[0] += Math.pow(Math.abs(vector.get(i)), p));

        return sum[0];
    }

    public static Vector entrywiseProduct(Vector u1, Vector u2) {
        assertSameLengths(u1, u2);

        double[] product = new double[u1.size()];
        operate(product.length, i -> product[i] = u1.get(i) * u2.get(i));

        return new Vector(product);
    }

    public static double distanceTo(Vector u1, Vector u2) {
        Vector difference = u2.minus(u1);

        return difference.magnitude();
    }

    public static Vector difference(Vector u1, Vector u2) {
        assertSameLengths(u1, u2);

        double[] difference = new double[u1.size()];
        operate(u1.size(), i -> difference[i] = u1.get(i) - u2.get(i));

        return new Vector(difference);
    }

    public static boolean areCloseEnough(Vector u1, Vector u2) {
        return distanceTo(u1, u2) < EPSILON;
    }

    public static Vector rotateBy(Vector vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector(vector.get(0) * cos - vector.get(1) * sin,
                          vector.get(0) * sin + vector.get(1) * cos);
    }

    public double[] getV() {
        return v;
    }

    public Vector plus(Vector other) {
        return Vector.sum(this, other);
    }

    public int size() {
        return v.length;
    }

    public double get(int pos) {
        return v[pos];
    }

    public Vector scaledBy(double scalar) {
        return Vector.scalarProduct(this, scalar);
    }

    public double dot(Vector other) {
        return Vector.dotProduct(this, other);
    }

    public boolean isZero() {
        return magnitude() < EPSILON;
    }

    public boolean containsNaN() {
        return Vector.containsNaN(this);
    }

    public Vector unit() {
        return Vector.unit(this);
    }

    public double angleTo(Vector other) {
        return angleBetween(this, other);
    }

    public double pnorm(double p) {
        return Vector.pnorm(this, p);
    }

    public Vector entrywiseProduct(Vector other) {
        return Vector.entrywiseProduct(this, other);
    }

    public double distanceTo(Vector other) {
        return Vector.distanceTo(this, other);
    }

    public Vector minus(Vector other) {
        return Vector.difference(this, other);
    }

    public double magnitude() {
        return Vector.pnorm(this, 2);
    }

    public boolean isAlmost(Vector other) {
        return Vector.areCloseEnough(this, other);
    }

    public Vector rotatedBy(double angle) {
        return Vector.rotateBy(this, angle);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(v);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vector vector = (Vector) o;
        return Arrays.equals(v, vector.v);
    }

    @Override
    public String toString() {
        return "Vector" + Arrays.toString(v);
    }
}
