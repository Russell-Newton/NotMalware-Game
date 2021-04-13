package whatexe.dungeoncrawler.entities.behavior.collision;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class EnemyCollisionBehavior extends CollisionBehavior<Enemy> {
    public EnemyCollisionBehavior(Enemy owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleCollisionWithEntity(Entity otherEntity) {

    }

    @Override
    public List<? extends Entity> getPossibleCollisionTargets() {
        List<Entity> targets = new ArrayList<>(owningEntity.getOwningRoom().getDoors());
        targets.addAll(owningEntity.getOwningRoom().getEnemies());
        targets.remove(owningEntity);
        return targets;
    }

    @Override
    public void handleCollisionWithBoundary() {

    }
}
