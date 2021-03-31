package whatexe.dungeoncrawler.entities.behavior.collision;

import whatexe.dungeoncrawler.entities.Entity;

import java.util.List;
import java.util.function.Supplier;

public class KillOnCollisionBehavior extends CollisionBehavior<Entity> {
    private final Supplier<List<? extends Entity>> collisionTargetsSupplier;

    public KillOnCollisionBehavior(Entity owningEntity,
                                   Supplier<List<? extends Entity>> collisionTargetsSupplier) {
        super(owningEntity);
        this.collisionTargetsSupplier = collisionTargetsSupplier;
    }

    @Override
    public void handleCollisionWithEntity(Entity otherEntity) {
        owningEntity.setHealth(-1);
    }

    @Override
    public List<? extends Entity> getPossibleCollisionTargets() {
        return collisionTargetsSupplier.get();
    }

    @Override
    public void handleCollisionWithBoundary() {
        owningEntity.setHealth(-1);
    }
}
