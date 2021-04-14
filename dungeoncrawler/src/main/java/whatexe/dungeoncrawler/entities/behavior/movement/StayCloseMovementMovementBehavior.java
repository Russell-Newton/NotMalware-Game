package whatexe.dungeoncrawler.entities.behavior.movement;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;
import java.util.function.Supplier;

public class StayCloseMovementMovementBehavior extends WanderMovementBehavior {

    private final Supplier<List<Entity>> targetsSupplier;
    private final double closeDistance;

    /**
     * Similar to {@link FollowingMovementBehavior}, but the owningEntity will attempt to stay at
     * least closeDistance away from any of the targets given by the targetsSupplier.
     *
     * @param owningEntity    the entity this behavior belongs to.
     * @param targetsSupplier a Supplier for a list of Entities that the owningEntity should
     *                        attempt to stay close to.
     * @param closeDistance   the closest distance the owningEntity will ever attempt to get to any
     *                        target.
     */
    public StayCloseMovementMovementBehavior(Entity owningEntity,
                                             Supplier<List<Entity>> targetsSupplier,
                                             double closeDistance) {
        super(owningEntity);

        this.targetsSupplier = targetsSupplier;
        this.closeDistance = closeDistance;
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

        double dDistance = toTarget.magnitude() - closeDistance;
        double speed = owningEntity.getEntityStatistics().getModifiedSpeed();
        if (dDistance < 0) {
            return toTarget.unit().scaledBy(-Math.min(-dDistance, speed));
        }

        return toTarget.unit().scaledBy(Math.min(dDistance, speed));
    }
}
