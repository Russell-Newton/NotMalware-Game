package whatexe.dungeoncrawler.entities.behavior.transformations;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;

public abstract class BehaviorTransformation<T extends Entity>
        implements Comparable<BehaviorTransformation<T>> {

    protected final T owningEntity;
    protected final int priority;

    /**
     * Creates a new BehaviorTransformation.
     *
     * @param owningEntity the entity this BehaviorTransformation is transforming the behavior of.
     * @param priority     the priority of this BehaviorTransformation. A higher number indicates
     *                     this transformation will be applied before transformations of lower
     *                     priority.
     */
    public BehaviorTransformation(T owningEntity, int priority) {
        this.owningEntity = owningEntity;
        this.priority = priority;
    }

    /**
     * If extra actions are desired when this transformation is added to the BehaviorSet
     * transformations, perform them here.
     */
    public abstract void onAdd();

    /**
     * If extra actions are desired when this transformation is removed from the BehaviorSet
     * transformations, perform them here.
     */
    public abstract void onRemove();

    public abstract List<? extends Entity> transformAttack(List<? extends Entity> inputEntities);

    public abstract Vector transformMovement(Vector inputMovement);

    public abstract void postDeath();

    public abstract void postHandleEntityOverlap(Entity otherEntity);

    public abstract void postHandleEntityCollision(Entity otherEntity);

    public abstract void postHandleBoundaryCollision();

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(BehaviorTransformation<T> o) {
        return o.priority - this.priority;
    }
}
