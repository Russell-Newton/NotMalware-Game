package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;

public abstract class DamageOnOverlapBehavior<T extends Entity> extends OverlapBehavior<T> {
    public DamageOnOverlapBehavior(T owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleOverlap(Entity otherEntity) {
        Entity.adjustHealthAsAttack(owningEntity,
                                    otherEntity,
                                    (int) -owningEntity.getEntityStatistics()
                                                    .getModifiedAttackDamage());
    }
}
