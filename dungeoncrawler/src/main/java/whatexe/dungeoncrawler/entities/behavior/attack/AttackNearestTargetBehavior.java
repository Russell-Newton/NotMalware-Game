package whatexe.dungeoncrawler.entities.behavior.attack;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;
import java.util.function.Supplier;

public abstract class AttackNearestTargetBehavior extends AttackBehavior<Entity> {

    private final Supplier<List<? extends Entity>> targetsSupplier;

    public AttackNearestTargetBehavior(Entity owningEntity,
                                       Supplier<List<? extends Entity>> targetsSupplier) {
        super(owningEntity);

        this.targetsSupplier = targetsSupplier;
    }

    @Override
    public abstract List<? extends Entity> getDefaultAttackEntities();

    @Override
    protected void setAttackDirection() {
        Entity target = owningEntity.getClosestEntityFromList(targetsSupplier.get());
        if (target == null) {
            attackDirection = new Vector(0, 0);
            return;
        }

        attackDirection = owningEntity.vectorToOtherEntity(target);
    }
}
