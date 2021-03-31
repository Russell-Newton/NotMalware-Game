package whatexe.dungeoncrawler.entities.behavior.movement;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

public class SimpleMovementBehavior extends MovementBehavior<Entity> {

    private final Vector movementVector;

    public SimpleMovementBehavior(Entity owningEntity, Vector movementVector) {
        super(owningEntity);

        this.movementVector = movementVector;
    }

    @Override
    public Vector getMovement() {
        return movementVector.unit()
                             .scaledBy(owningEntity.getEntityStatistics().getModifiedSpeed());
    }

}
