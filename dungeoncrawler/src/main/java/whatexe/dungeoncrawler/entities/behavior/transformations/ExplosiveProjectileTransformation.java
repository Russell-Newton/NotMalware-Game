package whatexe.dungeoncrawler.entities.behavior.transformations;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;

public class ExplosiveProjectileTransformation extends BehaviorTransformation<Entity> {

    private static final double DELAY_RESCALE = 0.5;

    public ExplosiveProjectileTransformation(Entity owningEntity) {
        super(owningEntity, -5);
    }

    @Override
    public void onAdd() {
        EntityStatistics statistics = owningEntity.getEntityStatistics();
        statistics.setAttackSpeedModifier(statistics.getAttackSpeedModifier() * DELAY_RESCALE);
    }

    @Override
    public void onRemove() {
        EntityStatistics statistics = owningEntity.getEntityStatistics();
        statistics.setAttackSpeedModifier(statistics.getAttackSpeedModifier() / DELAY_RESCALE);
    }

    @Override
    public List<? extends Entity> transformAttack(List<? extends Entity> inputEntities) {
        for (Entity entity : inputEntities) {
            entity.getBehaviorSet()
                  .getBehaviorTransformations().add(new ExplodeOnDeathTransformation(entity));
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
