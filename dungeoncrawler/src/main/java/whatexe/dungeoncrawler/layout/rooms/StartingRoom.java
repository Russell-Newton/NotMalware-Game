package whatexe.dungeoncrawler.layout.rooms;

import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.tileengine.TiledMap;

public class StartingRoom extends SimpleRoom {

    public StartingRoom(TiledMap fromTiledMap, Level owningLevel) {
        super(fromTiledMap, owningLevel, Direction.values());
        super.hasVisited = true;
    }

    @Override
    protected void initEntities() {
        // do nothing
    }
}
