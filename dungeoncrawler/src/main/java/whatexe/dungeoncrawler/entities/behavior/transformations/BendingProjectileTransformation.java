package whatexe.dungeoncrawler.entities.behavior.transformations;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.movement.GravitationalPullingBehavior;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;

import java.util.List;

public class BendingProjectileTransformation extends BehaviorTransformation<Entity> {
    public BendingProjectileTransformation(Entity owningEntity) {
        super(owningEntity, -1);
    }

    @Override
    public void onAdd() {

    }

    @Override
    public void onRemove() {

    }

    @Override
    public List<? extends Entity> transformAttack(List<? extends Entity> inputEntities) {
        for (Entity entity : inputEntities) {
            if (entity instanceof Projectile) {
                Projectile casted = (Projectile) entity;
                casted.getBehaviorSet().setMovementBehavior(new GravitationalPullingBehavior(
                        entity,
                        casted.getBehaviorSet()
                              .getCollisionBehavior()
                                ::getPossibleCollisionTargets,
                        80,
                        casted.getBehaviorSet().getMovementBehavior().getMovement()
                ));
            }
        }
        return inputEntities;
    }

    @Override
    public Vector transformMovement(Vector inputMovement) {
        return inputMovement;
    }

    @Override
    public void postDeath() {

    }

    @Override
    public void postHandleEntityOverlap(Entity otherEntity) {

    }

    @Override
    public void postHandleEntityCollision(Entity otherEntity) {

    }

    @Override
    public void postHandleBoundaryCollision() {

    }
}
