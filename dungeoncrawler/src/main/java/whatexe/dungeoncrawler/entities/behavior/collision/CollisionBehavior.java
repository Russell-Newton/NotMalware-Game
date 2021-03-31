package whatexe.dungeoncrawler.entities.behavior.collision;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityBehavior;

import java.util.List;

public abstract class CollisionBehavior<T extends Entity> extends EntityBehavior<T> {
    public CollisionBehavior(T owningEntity) {
        super(owningEntity);
    }

    public abstract void handleCollisionWithEntity(Entity otherEntity);

    public abstract List<? extends Entity> getPossibleCollisionTargets();

    public abstract void handleCollisionWithBoundary();
}
