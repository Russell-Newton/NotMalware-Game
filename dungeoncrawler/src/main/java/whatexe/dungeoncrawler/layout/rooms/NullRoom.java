package whatexe.dungeoncrawler.layout.rooms;

import whatexe.dungeoncrawler.layout.Direction;

public class NullRoom extends Room {
    public NullRoom() {
        super(null, null);
    }

    @Override
    protected void initDoors(Direction[] directionsToGenerate) {

    }

    @Override
    protected void initEntities() {

    }

    @Override
    protected void handleOnRoomClear() {

    }
}
