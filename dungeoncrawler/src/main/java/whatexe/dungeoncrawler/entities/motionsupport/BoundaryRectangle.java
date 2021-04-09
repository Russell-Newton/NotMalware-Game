package whatexe.dungeoncrawler.entities.motionsupport;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class BoundaryRectangle extends Entity {
    public BoundaryRectangle(Room owningRoom, Rectangle2D bounds) {
        super(Sprite.asRectangle((int) bounds.getWidth(),
                                 (int) bounds.getHeight(),
                                 Color.TRANSPARENT),
              new Rectangle2D(bounds.getMinX(),
                              bounds.getMinY(),
                              bounds.getWidth(),
                              bounds.getHeight()),
              owningRoom);

        setEntityPosition(bounds.getMinX(), bounds.getMinY());
    }
}
