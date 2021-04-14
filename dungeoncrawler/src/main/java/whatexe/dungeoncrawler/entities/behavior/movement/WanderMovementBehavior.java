package whatexe.dungeoncrawler.entities.behavior.movement;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

public class WanderMovementBehavior extends MovementBehavior<Entity> {

    private static final int DEFAULT_WANDER_ONE_DIRECTION_TICKS = 200;

    private final int wanderOneDirectionTicks;

    private int wanderCurrentTicks;
    private Vector wanderCurrentVector;

    public WanderMovementBehavior(Entity owningEntity, int wanderOneDirectionTicks) {
        super(owningEntity);
        this.wanderOneDirectionTicks = wanderOneDirectionTicks;
    }

    public WanderMovementBehavior(Entity owningEntity) {
        this(owningEntity, DEFAULT_WANDER_ONE_DIRECTION_TICKS);
    }

    @Override
    public Vector getMovement() {
        if (wanderCurrentTicks > 0) {
            wanderCurrentTicks--;
            return wanderCurrentVector;
        }
        wanderCurrentTicks = wanderOneDirectionTicks;
        wanderCurrentVector = new Vector(1, 0)
                .rotatedBy(Math.random() * Math.PI * 2)
                .scaledBy(owningEntity.getEntityStatistics().getModifiedSpeed());
        return wanderCurrentVector;
    }
}
