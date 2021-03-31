package whatexe.dungeoncrawler.entities.behavior.collision;

import whatexe.dungeoncrawler.entities.Entity;

import java.util.List;

public class DoNothingCollisionBehavior extends CollisionBehavior<Entity> {
    public DoNothingCollisionBehavior(Entity owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleCollisionWithEntity(Entity otherEntity) {

    }

    @Override
    public List<? extends Entity> getPossibleCollisionTargets() {
        return List.of();
    }

    @Override
    public void handleCollisionWithBoundary() {

    }
}
