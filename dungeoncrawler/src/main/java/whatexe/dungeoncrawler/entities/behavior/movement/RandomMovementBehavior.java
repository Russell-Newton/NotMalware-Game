package whatexe.dungeoncrawler.entities.behavior.movement;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

public class RandomMovementBehavior extends MovementBehavior<Entity> {

    public RandomMovementBehavior(Entity owningEntity) {
        super(owningEntity);
    }

    @Override
    public Vector getMovement() {
        double speed = owningEntity.getEntityStatistics().getModifiedSpeed();
        int dx = (int) (Math.random() * speed) - (int) (Math.random() * speed);
        int dy = (int) (Math.random() * speed) - (int) (Math.random() * speed);
        return new Vector(dx, dy);
    }

}
