package whatexe.tileengine;

public class PointObject extends MapObject {

    PointObject(whatexe.tileengine.fromtmx.MapObject fromTmx) {
        super(fromTmx);
        build();
    }

    @Override
    protected void build() {
        x = getFromTmx().getX();
        y = getFromTmx().getY();
    }
}
