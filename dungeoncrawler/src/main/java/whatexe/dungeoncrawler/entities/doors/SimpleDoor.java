package whatexe.dungeoncrawler.entities.doors;

import javafx.geometry.Rectangle2D;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class SimpleDoor extends Door {

    public SimpleDoor(Level owningLevel,
                      Direction direction,
                      Rectangle2D hitbox,
                      Room owningRoom) {
        super(owningLevel, direction, hitbox, owningRoom);
        canTick = true;
    }

    public SimpleDoor(Level owningLevel,
                      Direction direction,
                      Room owningRoom) {
        super(owningLevel, direction, owningRoom);
        canTick = true;
    }

}
