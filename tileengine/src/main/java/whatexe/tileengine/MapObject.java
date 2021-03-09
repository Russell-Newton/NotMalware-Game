package whatexe.tileengine;

public abstract class MapObject {

    private final whatexe.tileengine.fromtmx.MapObject fromTmx;
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    MapObject(whatexe.tileengine.fromtmx.MapObject fromTmx) {
        this.fromTmx = fromTmx;

        build();
    }

    protected abstract void build();

    whatexe.tileengine.fromtmx.MapObject getFromTmx() {
        return fromTmx;
    }

    static MapObject buildMapObject(whatexe.tileengine.fromtmx.MapObject fromTmx) {
        if (fromTmx.getEllipse() != null) {
            return new EllipseObject(fromTmx);
        }
        if (fromTmx.getPoint() != null) {
            return new PointObject(fromTmx);
        }
        if (fromTmx.getPolygon() != null) {
            return new PolygonObject(fromTmx);
        }
        if (fromTmx.getText() != null) {
            return new TextObject(fromTmx);
        }
        return new RectangleObject(fromTmx);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

}
