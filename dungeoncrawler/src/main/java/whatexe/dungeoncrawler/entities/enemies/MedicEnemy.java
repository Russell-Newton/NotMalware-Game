package whatexe.dungeoncrawler.entities.enemies;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.AnimatedSprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackNearestTargetBehavior;
import whatexe.dungeoncrawler.entities.behavior.collision.KillOnCollisionBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.GravitationalPullingBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.StayCloseMovementMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.DamageOnOverlapBehavior;
import whatexe.dungeoncrawler.entities.friends.Friend;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.projectiles.EnemyProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MedicEnemy extends Enemy {
    public MedicEnemy(Room owningRoom) {
        super(new AnimatedSprite(
                new Image(Enemy.class.getResourceAsStream("windows_defender.png")),
                32,
                32,
                4,
                4,
                1,
                1000), 20, owningRoom);

        getEntityStatistics().setSpeed(0.6);
        getEntityStatistics().setAttackDamage(10);
        getEntityStatistics().setAttackDelay(500);

        getBehaviorSet().setMovementBehavior(
                new StayCloseMovementMovementBehavior(this,
                                                      this::getMovementTargets,
                                                      100));
        getBehaviorSet().setAttackBehavior(new MedicAttackBehavior(this,
                                                                   this::getAttackTargets));
    }

    private List<Entity> getMovementTargets() {
        List<Entity> targets = new ArrayList<>(owningRoom.getEnemies());
        targets = targets.stream()
                         .filter(e -> !(e instanceof MedicEnemy))
                         .collect(Collectors.toList());
        return targets;
    }

    private List<Entity> getAttackTargets() {
        List<Entity> targets = getMovementTargets();
        targets.addAll(owningRoom.getFriends());
        if (owningRoom.getPlayer() != null) {
            targets.add(owningRoom.getPlayer());
        }
        return targets;
    }

    @Override
    public int getDanger() {
        return 3;
    }

    private static class MProjOverlapBehavior extends DamageOnOverlapBehavior<MedicProjectile> {

        private final Supplier<List<? extends Entity>> overlapTargetsSupplier;

        public MProjOverlapBehavior(MedicProjectile owningEntity,
                                    Supplier<List<? extends Entity>> overlapTargetsSupplier) {
            super(owningEntity);
            this.overlapTargetsSupplier = overlapTargetsSupplier;
        }

        @Override
        public List<? extends Entity> getPossibleOverlapTargets() {
            return overlapTargetsSupplier.get();
        }

        @Override
        public void handleOverlap(Entity otherEntity) {
            if (otherEntity instanceof Friend) {
                super.handleOverlap(otherEntity);
            } else {
                Entity.adjustHealthAsAttack(owningEntity,
                                            otherEntity,
                                            (int) owningEntity.getEntityStatistics()
                                                              .getModifiedAttackDamage());
            }
            owningEntity.setHealth(-1);
        }
    }

    private static class MProjCollisionBehavior extends KillOnCollisionBehavior {
        public MProjCollisionBehavior(Entity owningEntity,
                                      Supplier<List<? extends Entity>> collisionTargetsSupplier) {
            super(owningEntity, collisionTargetsSupplier);
        }

        @Override
        public void handleCollisionWithEntity(Entity otherEntity) {
            int dHealth;
            if (otherEntity instanceof Friend) {
                dHealth = (int) -owningEntity.getEntityStatistics().getModifiedAttackDamage();
            } else {
                dHealth = (int) owningEntity.getEntityStatistics().getModifiedAttackDamage();
            }
            Entity.adjustHealthAsAttack(owningEntity,
                                        otherEntity,
                                        dHealth);
            super.handleCollisionWithEntity(otherEntity);
        }
    }

    private class MedicAttackBehavior extends AttackNearestTargetBehavior {

        public MedicAttackBehavior(Entity owningEntity,
                                   Supplier<List<? extends Entity>> targetsSuppler) {
            super(owningEntity, targetsSuppler);
        }

        @Override
        public List<? extends Entity> getDefaultAttackEntities() {
            EntityStatistics bulletStats = new EntityStatistics();
            bulletStats.setSpeed(1.25);
            bulletStats.setAttackDamage(owningEntity.getEntityStatistics()
                                                    .getModifiedAttackDamage());

            Projectile bullet = new MedicProjectile(
                    Sprite.asCircle(5, Color.LIGHTGREEN),
                    owningEntity.getOwningRoom(),
                    attackDirection,
                    300,
                    bulletStats);

            bullet.setEntityPosition(owningEntity.getDisplayNode()
                                                 .getBoundsInParent()
                                                 .getCenterX(),
                                     owningEntity.getDisplayNode()
                                                 .getBoundsInParent()
                                                 .getCenterY());
            return List.of(bullet);
        }
    }

    private class MedicProjectile extends EnemyProjectile {
        public MedicProjectile(Sprite displayNode,
                               Room owningRoom,
                               Vector direction,
                               int lifetime,
                               EntityStatistics entityStatistics) {
            super(displayNode, owningRoom, direction, lifetime, entityStatistics);

            getBehaviorSet().setOverlapBehavior(
                    new MProjOverlapBehavior(this, this::getCollisionTargets));
            getBehaviorSet().setCollisionBehavior(
                    new MProjCollisionBehavior(this, this::getCollisionTargets));
            getBehaviorSet().setMovementBehavior(
                    new GravitationalPullingBehavior(this,
                                                     this::getCollisionTargets,
                                                     70,
                                                     direction));
        }

        @Override
        protected List<? extends Entity> getCollisionTargets() {
            return getAttackTargets();
        }
    }
}
