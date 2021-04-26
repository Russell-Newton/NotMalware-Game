package whatexe.dungeoncrawler.entities.behavior.transformations;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class SplitOnDeathTransformation extends BehaviorTransformation<Projectile> {
    private static final List<Class<? extends BehaviorTransformation<? extends Entity>>>
            ILLEGAL_INHERIT_TRANSFORMS = List.of(SplitOnDeathTransformation.class);

    /**
     * Creates a new BehaviorTransformation.
     *
     * @param owningEntity the entity this BehaviorTransformation is transforming the behavior of.
     */
    public SplitOnDeathTransformation(Projectile owningEntity) {
        super(owningEntity, -5);
    }

    private static boolean isValidInheritedTransform(
            BehaviorTransformation<? extends Entity> transform) {
        for (Class<? extends BehaviorTransformation<? extends Entity>> transformClass
                : ILLEGAL_INHERIT_TRANSFORMS) {
            if (transformClass.isAssignableFrom(transform.getClass())) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static BehaviorTransformation<? extends Entity> tryMakeTransformFromInherited(
            BehaviorTransformation<? extends Entity> inheritedTransform,
            Entity forEntity) {
        try {
            Constructor<? extends BehaviorTransformation> transformationConstructor =
                    inheritedTransform.getClass().getConstructor(Entity.class);
            return transformationConstructor.newInstance(forEntity);
        } catch (NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
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
        for (int i = 0; i < 4; i++) {
            EntityStatistics statistics
                    = new EntityStatistics().copyFrom(owningEntity.getEntityStatistics());
            Vector movement = new Vector(1, 0).rotatedBy(Math.PI / 2 * i);

            statistics.setAttackDamage(owningEntity.getEntityStatistics()
                                                   .getModifiedAttackDamage());
            statistics.setAttackDamageModifier(0.5);
            statistics.setSpeedModifier(2);

            Projectile splitOff = new Projectile(
                    Sprite.asCircle((int) owningEntity.getDisplayNode().getWidth() / 2,
                                    Color.GREY),
                    owningEntity.getOwningRoom(),
                    movement,
                    20,
                    statistics) {
                @Override
                protected List<? extends Entity> getCollisionTargets() {
                    return owningEntity.getBehaviorSet()
                                       .getCollisionBehavior()
                                       .getPossibleCollisionTargets();
                }

                @Override
                protected List<? extends Entity> getOverlapTargets() {
                    return owningEntity.getBehaviorSet()
                                       .getOverlapBehavior()
                                       .getPossibleOverlapTargets();
                }
            };
            splitOff.setEntityPosition(owningEntity.getDisplayNode().getTranslateX(),
                                       owningEntity.getDisplayNode().getTranslateY());

            // Inherit legal transformations
            PriorityQueue<BehaviorTransformation<? extends Entity>> inheritedTransforms =
                    owningEntity.getBehaviorSet().getBehaviorTransformations();
            List<BehaviorTransformation<? extends Entity>> filteredTransforms =
                    inheritedTransforms
                            .stream()
                            .filter(SplitOnDeathTransformation::isValidInheritedTransform)
                            .collect(Collectors.toList());
            List<BehaviorTransformation<? extends Entity>> newTransforms = new ArrayList<>();
            for (BehaviorTransformation<? extends Entity> filtered : filteredTransforms) {
                BehaviorTransformation<? extends Entity> newTransform =
                        tryMakeTransformFromInherited(filtered, splitOff);
                if (newTransform != null) {
                    newTransforms.add(newTransform);
                }
            }

            splitOff.getBehaviorSet().getBehaviorTransformations().addAll(newTransforms);

            owningEntity.getOwningRoom().addEntity(splitOff);
        }
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
