package whatexe.tileengine;

public class RectangleObject  extends MapObject {
    private double rotation;

    RectangleObject(whatexe.tileengine.fromtmx.MapObject fromTmx) {
        super(fromTmx);
        build();
    }

    @Override
    protected void build() {
        x = getFromTmx().getX();
        y = getFromTmx().getY();
        width = getFromTmx().getWidth();
        height = getFromTmx().getHeight();
        rotation = getFromTmx().getRotation();
    }

    public double getRotation() {
        return rotation;
    }
}
