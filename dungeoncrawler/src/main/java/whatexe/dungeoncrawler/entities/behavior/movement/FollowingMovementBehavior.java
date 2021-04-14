package whatexe.dungeoncrawler.entities.behavior.movement;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;
import java.util.function.Supplier;

public class FollowingMovementBehavior extends WanderMovementBehavior {

    private final Supplier<List<Entity>> targetsSupplier;

    /**
     * This behavior makes owningEntity attempt to reach the closest target in the list supplied
     * by the targetsSupplier. If no target is available, it will wander instead.
     *
     * @param owningEntity    the Entity this behavior belongs to.
     * @param targetsSupplier the Supplier that gets a list of possible targets to approach.
     */
    public FollowingMovementBehavior(Entity owningEntity,
                                     Supplier<List<Entity>> targetsSupplier) {
        super(owningEntity);

        this.targetsSupplier = targetsSupplier;
    }

    @Override
    public Vector getMovement() {
        Entity target = owningEntity.getClosestEntityFromList(targetsSupplier.get());
        if (target == null) {
            return super.getMovement();
        }
        Vector toTarget = owningEntity.vectorToOtherEntity(target);
        if (toTarget.isZero()) {
            return toTarget;
        }
        return toTarget.unit().scaledBy(owningEntity.getEntityStatistics().getModifiedSpeed());
    }
}
