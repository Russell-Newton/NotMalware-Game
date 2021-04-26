package whatexe.dungeoncrawler.entities.miscellaneous;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.death.RemoveOnDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.ExplosionOverlapBehavior;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class Explosion extends Entity {

    private int lifetime;

    public Explosion(Room owningRoom,
                     EntityStatistics entityStatistics,
                     int lifetime,
                     int size) {
        super(Sprite.asRectangle(size, size, Color.ORANGERED), owningRoom, entityStatistics);
        canTick = true;
        this.lifetime = lifetime;

        behaviorSet.get().setOverlapBehavior(new ExplosionOverlapBehavior(this));
        behaviorSet.get().setDeathBehavior(new RemoveOnDeathBehavior(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (lifetime > 0) {
            lifetime--;
        }
        if (lifetime == 0) {
            setHealth(-1);
        }
    }
}
