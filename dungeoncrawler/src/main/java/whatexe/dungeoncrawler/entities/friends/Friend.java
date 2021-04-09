package whatexe.dungeoncrawler.entities.friends;

import javafx.geometry.Rectangle2D;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.layout.rooms.Room;

public abstract class Friend extends Entity {
    public Friend(Sprite displayNode,
                  Room owningRoom) {
        super(displayNode, owningRoom);
    }

    public Friend(Sprite displayNode,
                  Room owningRoom,
                  EntityStatistics entityStatistics) {
        super(displayNode, owningRoom, entityStatistics);
    }

    public Friend(Sprite displayNode,
                  Room owningRoom, int startingHealth) {
        super(displayNode, owningRoom, startingHealth);
    }

    public Friend(Sprite displayNode,
                  Room owningRoom,
                  int startingHealth,
                  EntityStatistics entityStatistics) {
        super(displayNode, owningRoom, startingHealth, entityStatistics);
    }

    public Friend(Sprite displayNode,
                  Rectangle2D hitbox,
                  Room owningRoom) {
        super(displayNode, hitbox, owningRoom);
    }

    public Friend(Sprite displayNode,
                  Rectangle2D hitbox,
                  Room owningRoom,
                  int startingHealth,
                  EntityStatistics entityStatistics) {
        super(displayNode, hitbox, owningRoom, startingHealth, entityStatistics);
    }
}
