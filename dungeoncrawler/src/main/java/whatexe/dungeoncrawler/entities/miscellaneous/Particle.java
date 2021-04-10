package whatexe.dungeoncrawler.entities.miscellaneous;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.death.RemoveOnDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.RandomMovementBehavior;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class Particle extends Entity {

    private int lifetime;

    public Particle(Room owningRoom, int width, int lifetime) {
        this(owningRoom, width, Color.hsb(Math.random() * 360, 1, 1), lifetime);
    }

    public Particle(Room owningRoom, int width, Paint fill, int lifetime) {
        super(Sprite.asRectangle(width, width, fill), owningRoom);
        canTick = true;
        this.lifetime = lifetime;

        getEntityStatistics().setSpeed(3);
        behaviorSet.get().setMovementBehavior(new RandomMovementBehavior(this));
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
