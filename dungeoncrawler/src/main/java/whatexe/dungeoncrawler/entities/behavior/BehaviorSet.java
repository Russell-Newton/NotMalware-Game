package whatexe.dungeoncrawler.entities.behavior;

import javafx.beans.property.SimpleObjectProperty;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.collision.CollisionBehavior;
import whatexe.dungeoncrawler.entities.behavior.death.DeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.MovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.OverlapBehavior;
import whatexe.dungeoncrawler.entities.behavior.transformations.BehaviorTransformation;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.List;
import java.util.PriorityQueue;

public class BehaviorSet {

    private final SimpleObjectProperty<AttackBehavior<? extends Entity>> attackBehavior;
    private final SimpleObjectProperty<MovementBehavior<? extends Entity>> movementBehavior;
    private final SimpleObjectProperty<DeathBehavior<? extends Entity>> deathBehavior;
    private final SimpleObjectProperty<OverlapBehavior<? extends Entity>> overlapBehavior;
    private final SimpleObjectProperty<CollisionBehavior<? extends Entity>> collisionBehavior;
    private final PriorityQueue<BehaviorTransformation<? extends Entity>> behaviorTransformations;

    public BehaviorSet(AttackBehavior<? extends Entity> attackBehavior,
                       MovementBehavior<? extends Entity> movementBehavior,
                       DeathBehavior<? extends Entity> deathBehavior,
                       OverlapBehavior<? extends Entity> overlapBehavior,
                       CollisionBehavior<? extends Entity> collisionBehavior) {
        this.attackBehavior = new SimpleObjectProperty<>(attackBehavior);
        this.movementBehavior = new SimpleObjectProperty<>(movementBehavior);
        this.deathBehavior = new SimpleObjectProperty<>(deathBehavior);
        this.overlapBehavior = new SimpleObjectProperty<>(overlapBehavior);
        this.collisionBehavior = new SimpleObjectProperty<>(collisionBehavior);
        behaviorTransformations = new PriorityQueue<>();
    }

    public AttackBehavior<? extends Entity> getAttackBehavior() {
        return attackBehavior.get();
    }

    public void setAttackBehavior(AttackBehavior<? extends Entity> attackBehavior) {
        this.attackBehavior.set(attackBehavior);
    }

    public SimpleObjectProperty<AttackBehavior<? extends Entity>> attackBehaviorProperty() {
        return attackBehavior;
    }

    public MovementBehavior<? extends Entity> getMovementBehavior() {
        return movementBehavior.get();
    }

    public void setMovementBehavior(MovementBehavior<? extends Entity> movementBehavior) {
        this.movementBehavior.set(movementBehavior);
    }

    public SimpleObjectProperty<MovementBehavior<? extends Entity>> movementBehaviorProperty() {
        return movementBehavior;
    }

    public DeathBehavior<? extends Entity> getDeathBehavior() {
        return deathBehavior.get();
    }

    public void setDeathBehavior(DeathBehavior<? extends Entity> deathBehavior) {
        this.deathBehavior.set(deathBehavior);
    }

    public SimpleObjectProperty<DeathBehavior<? extends Entity>> deathBehaviorProperty() {
        return deathBehavior;
    }

    public OverlapBehavior<? extends Entity> getOverlapBehavior() {
        return overlapBehavior.get();
    }

    public void setOverlapBehavior(OverlapBehavior<? extends Entity> overlapBehavior) {
        this.overlapBehavior.set(overlapBehavior);
    }

    public SimpleObjectProperty<OverlapBehavior<? extends Entity>> overlapBehaviorProperty() {
        return overlapBehavior;
    }

    public CollisionBehavior<? extends Entity> getCollisionBehavior() {
        return collisionBehavior.get();
    }

    public void setCollisionBehavior(CollisionBehavior<? extends Entity> collisionBehavior) {
        this.collisionBehavior.set(collisionBehavior);
    }

    public SimpleObjectProperty<CollisionBehavior<? extends Entity>> collisionBehaviorProperty() {
        return collisionBehavior;
    }

    public void addBehaviorTransformation(BehaviorTransformation<? extends Entity> transformation) {
        behaviorTransformations.add(transformation);
        transformation.onAdd();
    }

    public void removeBehaviorTransformation(
            BehaviorTransformation<? extends Entity> transformation) {
        if (behaviorTransformations.remove(transformation)) {
            transformation.onRemove();
        }
    }

    public PriorityQueue<BehaviorTransformation<? extends Entity>> getBehaviorTransformations() {
        return behaviorTransformations;
    }

    /**
     * @return a list of entities to be added to the room.
     */
    public List<? extends Entity> attack() {
        List<? extends Entity> attackEntities = attackBehavior.get().attack();
        for (BehaviorTransformation<? extends Entity> transformation : behaviorTransformations) {
            attackEntities = transformation.transformAttack(attackEntities);
        }
        return attackEntities;
    }

    public Vector getMovement() {
        Vector movementVector = movementBehavior.get().getMovement();
        for (BehaviorTransformation<? extends Entity> transformation : behaviorTransformations) {
            movementVector = transformation.transformMovement(movementVector);
        }
        return movementVector;
    }

    public void die() {
        deathBehavior.get().die();
        for (BehaviorTransformation<? extends Entity> transformation : behaviorTransformations) {
            transformation.postDeath();
        }
    }

    public void handleEntityOverlap(Entity entity) {
        overlapBehavior.get().handleOverlap(entity);
        for (BehaviorTransformation<? extends Entity> transformation : behaviorTransformations) {
            transformation.postHandleEntityOverlap(entity);
        }
    }

    public void handleEntityCollision(Entity entity) {
        collisionBehavior.get().handleCollisionWithEntity(entity);
        for (BehaviorTransformation<? extends Entity> transformation : behaviorTransformations) {
            transformation.postHandleEntityCollision(entity);
        }
    }

    public void handleBoundaryCollision() {
        collisionBehavior.get().handleCollisionWithBoundary();
        for (BehaviorTransformation<? extends Entity> transformation : behaviorTransformations) {
            transformation.postHandleBoundaryCollision();
        }
    }
}
