package whatexe.dungeoncrawler.entities.behavior.transformations;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.miscellaneous.Explosion;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;

public class ExplodeOnDeathTransformation extends BehaviorTransformation<Entity> {
    /**
     * Creates a new BehaviorTransformation.
     *
     * @param owningEntity the entity this BehaviorTransformation is transforming the behavior of.
     */
    public ExplodeOnDeathTransformation(Entity owningEntity) {
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
        return inputEntities;
    }

    @Override
    public Vector transformMovement(Vector inputMovement) {
        return inputMovement;
    }

    @Override
    public void postDeath() {
        EntityStatistics statistics
                = new EntityStatistics().copyFrom(owningEntity.getEntityStatistics());

        // Set explosion damage to double the entity's modifified attack damage
        statistics.setAttackDamage(owningEntity.getEntityStatistics().getModifiedAttackDamage());
        statistics.setAttackDamageModifier(1);

        Explosion explosion = new Explosion(owningEntity.getOwningRoom(),
                                            statistics,
                                            10,
                                            (int) (owningEntity.getHitbox().getWidth() * 1.2));
        explosion.setEntityPosition(owningEntity.getDisplayNode().getTranslateX(),
                                    owningEntity.getDisplayNode().getTranslateY());

        owningEntity.getOwningRoom().addEntity(explosion);
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
