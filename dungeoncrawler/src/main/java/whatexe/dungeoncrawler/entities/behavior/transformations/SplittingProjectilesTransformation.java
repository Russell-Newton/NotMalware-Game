package whatexe.dungeoncrawler.entities.behavior.transformations;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;

import java.util.List;

public class SplittingProjectilesTransformation extends BehaviorTransformation<Entity> {
    /**
     * Creates a new BehaviorTransformation.
     *
     * @param owningEntity the entity this BehaviorTransformation is transforming the behavior of.
     */
    public SplittingProjectilesTransformation(Entity owningEntity) {
        super(owningEntity, -5);
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
                entity.getBehaviorSet()
                      .getBehaviorTransformations()
                      .add(new SplitOnDeathTransformation((Projectile) entity));
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
