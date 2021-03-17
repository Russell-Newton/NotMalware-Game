package whatexe.tileengine;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class PolygonObject extends MapObject {
    private Vector<Double>[] points;

    PolygonObject(whatexe.tileengine.fromtmx.MapObject fromTmx) {
        super(fromTmx);
        build();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void build() {
        x = getFromTmx().getX();
        y = getFromTmx().getY();

        points = Arrays.stream(getFromTmx().getPolygon().getPoints().split(" "))
                       .map(str -> str.split(","))
                       .map(point -> new Vector<>(List.of(
                               Double.parseDouble(point[0]),
                               Double.parseDouble(point[1]))
                       )).toArray(Vector[]::new);
    }

    public Vector<Double>[] getPoints() {
        return points;
    }
}
