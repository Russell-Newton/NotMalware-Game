package whatexe.dungeoncrawler.entities.behavior.overlap;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;

import java.util.List;
import java.util.function.Supplier;

public class ProjectileOverlapBehavior extends DamageOnOverlapBehavior<Projectile> {
    private final Supplier<List<? extends Entity>> overlapTargetsSupplier;
    public ProjectileOverlapBehavior(Projectile owningEntity,
                                     Supplier<List<? extends Entity>> overlapTargetsSupplier) {
        super(owningEntity);
        this.overlapTargetsSupplier = overlapTargetsSupplier;
    }

    @Override
    public void handleOverlap(Entity otherEntity) {
        super.handleOverlap(otherEntity);
        owningEntity.setHealth(-1);
    }

    @Override
    public List<? extends Entity> getPossibleOverlapTargets() {
        return overlapTargetsSupplier.get();
    }
}
