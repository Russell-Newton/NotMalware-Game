package whatexe.dungeoncrawler.entities.enemies.bosses;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.AnimatedSprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.MovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.transformations.ExplosiveProjectileTransformation;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.projectiles.EnemyProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.ArrayList;
import java.util.List;

public class SecondBoss extends Boss {

    private AttackState currentAttackState = AttackState.PRIMARY_DELAY;
    private boolean changedState = false;
    private int currentStateTimer = currentAttackState.duration();

    public SecondBoss(Room owningRoom) {
        super(defaultSprite(), owningRoom, defaultStatistics());
        getEntityStatistics().setSpeed(1.5);
        getBehaviorSet().setAttackBehavior(new SecondBossAttackBehavior(this));
        getBehaviorSet().setMovementBehavior(new SecondBossMovementBehavior(this));
        getBehaviorSet().getBehaviorTransformations().add(
                new ExplosiveProjectileTransformation(this));
    }

    private static Sprite defaultSprite() {
        Sprite sprite = new AnimatedSprite(
                new Image(Enemy.class.getResourceAsStream("registry.png")),
                128,
                128,
                4,
                4,
                1,
                1000);
        return sprite;
    }

    private static EntityStatistics defaultStatistics() {
        return new EntityStatistics(120, 1, 1, 1);
    }

    @Override
    public int getDanger() {
        return 12;
    }

    private enum AttackState {
        /*
        Synchronize lunges with the music. A full lunge sequence lasts one measure, with 3
        measures of delay between.
         */
        PRIMARY_DELAY {
            @Override
            protected int duration() {
                return 2000 / 5;
            }

            @Override
            protected AttackState nextState() {
                return LUNGE1;
            }
        },
        LUNGE1 {
            @Override
            protected int duration() {
                return 500 / 5;
            }

            @Override
            protected AttackState nextState() {
                return LUNGE2;
            }
        },
        LUNGE2 {
            @Override
            protected int duration() {
                return 500 / 5;
            }

            @Override
            protected AttackState nextState() {
                return LUNGE3;
            }
        },
        LUNGE3 {
            @Override
            protected int duration() {
                return 500 / 5;
            }

            @Override
            protected AttackState nextState() {
                return LUNGE4;
            }
        },
        LUNGE4 {
            @Override
            protected int duration() {
                return 500 / 5;
            }

            @Override
            protected AttackState nextState() {
                return CHARGING;
            }
        },
        CHARGING {
            @Override
            protected int duration() {
                return 6000 / 5;
            }

            @Override
            protected AttackState nextState() {
                return LUNGE1;
            }
        };

        protected abstract int duration();

        protected abstract AttackState nextState();
    }

    private class SecondBossAttackBehavior extends AttackBehavior<SecondBoss> {
        public SecondBossAttackBehavior(SecondBoss owningEntity) {
            super(owningEntity);
        }

        @Override
        protected List<? extends Entity> getDefaultAttackEntities() {
            List<Projectile> bullets = new ArrayList<>();
            EntityStatistics bulletStats = new EntityStatistics();
            bulletStats.setAttackDamage(owningEntity.getEntityStatistics()
                                                    .getModifiedAttackDamage());
            bulletStats.setSpeed(6);
            for (int i = 0; i < 8; i++) {
                Vector direction = new Vector(1, 0).rotatedBy(i * Math.PI / 4);

                Projectile bullet =
                        new EnemyProjectile(Sprite.asCircle(8, Color.HOTPINK),
                                            owningRoom,
                                            direction,
                                            200,
                                            new EntityStatistics().copyFrom(bulletStats));

                bullet.setEntityPosition(owningEntity.getDisplayNode()
                                                     .getBoundsInParent()
                                                     .getCenterX(),
                                         owningEntity.getDisplayNode()
                                                     .getBoundsInParent()
                                                     .getCenterY());
                bullets.add(bullet);
            }

            return bullets;
        }

        @Override
        protected void setAttackDirection() {
            if (changedState && currentAttackState != AttackState.CHARGING) {
                attackDirection = new Vector(1, 0);
                changedState = false;
            } else {
                attackDirection = new Vector(0, 0);
            }
        }
    }

    private class SecondBossMovementBehavior extends MovementBehavior<SecondBoss> {

        private Vector lungeVector = new Vector(0, 0);

        public SecondBossMovementBehavior(SecondBoss owningEntity) {
            super(owningEntity);
        }

        @Override
        public Vector getMovement() {
            if (currentStateTimer > 0) {
                currentStateTimer--;
            } else if (currentStateTimer == 0) {
                currentAttackState = currentAttackState.nextState();
                currentStateTimer = currentAttackState.duration();
                changedState = true;

                if (getOwningRoom().getPlayer() != null) {
                    lungeVector =
                            owningEntity.vectorToOtherEntity(getOwningRoom().getPlayer());
                } else {
                    lungeVector = new Vector(0, 0);
                }

                if (currentAttackState == AttackState.CHARGING) {
                    owningEntity.getEntityStatistics().setSpeedModifier(0);
                } else {
                    owningEntity.getEntityStatistics().setSpeedModifier(1);
                }
            }
            if (lungeVector.isZero()) {
                return lungeVector;
            }
            return lungeVector.unit()
                              .scaledBy(owningEntity.getEntityStatistics().getModifiedSpeed());
        }
    }
}
