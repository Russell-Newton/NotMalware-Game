package whatexe.dungeoncrawler.entities.behavior.collision;

import whatexe.dungeoncrawler.entities.Entity;

import java.util.List;
import java.util.function.Supplier;

public class ProjectileCollisionBehavior extends KillOnCollisionBehavior {
    public ProjectileCollisionBehavior(Entity owningEntity,
                                       Supplier<List<? extends Entity>> collisionTargetsSupplier) {
        super(owningEntity, collisionTargetsSupplier);
    }

    @Override
    public void handleCollisionWithEntity(Entity otherEntity) {
        Entity.adjustHealthAsAttack(owningEntity,
                                    otherEntity,
                                    (int) -owningEntity.getEntityStatistics()
                                                       .getModifiedAttackDamage());
        super.handleCollisionWithEntity(otherEntity);
    }
}
