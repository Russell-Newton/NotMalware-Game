package whatexe.tileengine;

import whatexe.tileengine.fromtmx.ObjectLayer;

import java.util.ArrayList;
import java.util.List;

public class ObjectGroup {

    private final ObjectLayer fromTmx;
    private final List<MapObject> objects;

    ObjectGroup(ObjectLayer fromTmx) {
        this.fromTmx = fromTmx;
        objects = new ArrayList<>();

        build();
    }

    private void build() {
        for (whatexe.tileengine.fromtmx.MapObject mapObject : fromTmx.getObjects()) {
            objects.add(MapObject.buildMapObject(mapObject));
        }
    }

    public List<MapObject> getObjects() {
        return objects;
    }
}
