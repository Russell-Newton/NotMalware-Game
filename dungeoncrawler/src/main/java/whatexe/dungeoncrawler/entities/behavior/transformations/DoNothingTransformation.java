package whatexe.dungeoncrawler.entities.behavior.transformations;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;

public class DoNothingTransformation extends BehaviorTransformation<Entity> {
    public DoNothingTransformation(Entity owningEntity) {
        super(owningEntity, Integer.MIN_VALUE);
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
