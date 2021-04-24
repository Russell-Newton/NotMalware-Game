package whatexe.dungeoncrawler.entities.enemies;

import javafx.geometry.Rectangle2D;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.presets.DefaultEnemyBehaviorSet;
import whatexe.dungeoncrawler.layout.rooms.Room;

public abstract class Enemy extends Entity {

    public Enemy(Sprite displayNode, int startingHealth, Room owningRoom) {
        super(displayNode, owningRoom, startingHealth);
        canTick = true;

        behaviorSet.set(new DefaultEnemyBehaviorSet(this));
    }

    public Enemy(Sprite displayNode,
                 Room owningRoom,
                 EntityStatistics entityStatistics) {
        super(displayNode, owningRoom, entityStatistics);
        canTick = true;

        behaviorSet.set(new DefaultEnemyBehaviorSet(this));
    }

    public Enemy(Sprite displayNode,
                 Rectangle2D hitbox,
                 Room owningRoom,
                 EntityStatistics entityStatistics) {
        super(displayNode, hitbox, owningRoom, 0, entityStatistics);
        canTick = true;

        behaviorSet.set(new DefaultEnemyBehaviorSet(this));
    }

    public abstract int getDanger();

}
