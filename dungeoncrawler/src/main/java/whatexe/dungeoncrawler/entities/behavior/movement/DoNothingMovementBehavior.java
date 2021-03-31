package whatexe.dungeoncrawler.entities.behavior.movement;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

public class DoNothingMovementBehavior extends MovementBehavior<Entity> {
    public DoNothingMovementBehavior(Entity owningEntity) {
        super(owningEntity);
    }

    @Override
    public Vector getMovement() {
        return new Vector(0, 0);
    }
}
