package whatexe.dungeoncrawler.entities.enemies.bosses;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.AnimatedSprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.attack.DoNothingAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.DoNothingMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.WanderMovementBehavior;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.projectiles.EnemyProjectile;
import whatexe.dungeoncrawler.entities.projectiles.Projectile;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThirdBoss extends Boss {

    private static final List<Vector> WARP_POINTS = Arrays.asList(
            // new Vector(224, 128, 1088, 576),
            // new Vector(1184, 128, -1088, 576),
            // new Vector(224, 576, 1088, -576),
            // new Vector(1184, 576, -1088, -576),
            new Vector(416, 128, 0, 1),
            new Vector(608, 128, 0, 1),
            new Vector(800, 128, 0, 1),
            new Vector(992, 128, 0, 1),
            new Vector(224, 352, 1, 0),
            new Vector(1184, 352, -1, 0),
            new Vector(416, 576, 0, -1),
            new Vector(608, 576, 0, -1),
            new Vector(800, 576, 0, -1),
            new Vector(992, 576, 0, -1)
                                                                 );

    private static Vector returnPosition;

    private AttackState currentAttackState = AttackState.PRIMARY_DELAY;
    private BossStage currentBossStage = BossStage.ONE_HUNDRED;
    private int currentStateTimer = currentAttackState.duration();
    private List<DummyBoss> currentDummies = new ArrayList<>();

    public ThirdBoss(Room owningRoom) {
        super(defaultSprite(), owningRoom, defaultStatistics());
    }

    private static Sprite defaultSprite() {
        Sprite sprite = new AnimatedSprite(
                new Image(Enemy.class.getResourceAsStream("windows.png")),
                128,
                128,
                4,
                4,
                1,
                1000);
        return sprite;
    }

    private static EntityStatistics defaultStatistics() {
        return new EntityStatistics(150, 1, 1, 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (currentStateTimer > 0) {
            currentStateTimer--;
        } else if (currentStateTimer == 0) {
            currentAttackState = currentAttackState.nextState(currentBossStage);
            currentStateTimer = currentAttackState.duration();

            currentAttackState.executeAs(this, currentDummies, currentBossStage);
        }
    }

    @Override
    public void adjustHealth(int dHealth) {
        super.adjustHealth(dHealth);
        if (getHealth() < getEntityStatistics().getMaxHealth() / 3) {
            currentBossStage = BossStage.THIRTY_THREE;
        } else if (getHealth() < 2 * getEntityStatistics().getMaxHealth() / 3) {
            currentBossStage = BossStage.SIXTY_SEVEN;
        } else {
            currentBossStage = BossStage.ONE_HUNDRED;
        }
        if (getHealth() <= 0) {
            owningRoom.removeEntities(currentDummies);
            currentDummies.clear();
        }
    }

    @Override
    public int getDanger() {
        return 20;
    }

    private enum BossStage {
        ONE_HUNDRED {
            @Override
            public int getWarpCount() {
                return 2;
            }

            @Override
            public int getDummyCount() {
                return 3;
            }
        },
        SIXTY_SEVEN {
            @Override
            public int getWarpCount() {
                return 2;
            }

            @Override
            public int getDummyCount() {
                return 5;
            }
        },
        THIRTY_THREE {
            @Override
            public int getWarpCount() {
                return 4;
            }

            @Override
            public int getDummyCount() {
                return 7;
            }
        };

        public abstract int getWarpCount();

        public abstract int getDummyCount();
    }

    private enum AttackState {
        PRIMARY_DELAY {
            @Override
            public int duration() {
                return 2000 / 5;
            }

            @Override
            public AttackState nextState(BossStage currentStage) {
                return WARP_OUT;
            }

            @Override
            public void executeAs(ThirdBoss asBoss,
                                  List<DummyBoss> dummies,
                                  BossStage currentStage) {

            }
        },
        WARP_OUT {
            @Override
            public int duration() {
                return 125 / 5;
            }

            @Override
            public AttackState nextState(BossStage currentStage) {
                return WARP_IN;
            }

            @Override
            public void executeAs(ThirdBoss asBoss,
                                  List<DummyBoss> dummies,
                                  BossStage currentStage) {
                asBoss.getBehaviorSet().setMovementBehavior(new DoNothingMovementBehavior(asBoss));
                if (returnPosition == null) {
                    returnPosition = new Vector(asBoss.getDisplayNode().getTranslateX(),
                                                asBoss.getDisplayNode().getTranslateY());
                }
                asBoss.setEntityPosition(10000, 10000);
                asBoss.owningRoom.removeEntities(dummies);
                dummies.clear();
            }
        },
        WARP_IN {

            @Override
            public int duration() {
                return 125 / 5;
            }

            @Override
            public AttackState nextState(BossStage currentStage) {
                return PAUSE_BEFORE_SHOOT;
            }

            @Override
            public void executeAs(ThirdBoss asBoss,
                                  List<DummyBoss> dummies,
                                  BossStage currentStage) {
                Collections.shuffle(WARP_POINTS);
                for (int i = 0; i < currentStage.getDummyCount(); i++) {
                    Vector warpPoint = WARP_POINTS.get(i);
                    DummyBoss newDummy = new DummyBoss(asBoss, warpPoint);
                    dummies.add(newDummy);
                }
                asBoss.owningRoom.addEntities(dummies);
            }
        },
        PAUSE_BEFORE_SHOOT {
            @Override
            public int duration() {
                return 750 / 5;
            }

            @Override
            public AttackState nextState(BossStage currentStage) {
                return SHOOT;
            }

            @Override
            public void executeAs(ThirdBoss asBoss,
                                  List<DummyBoss> dummies,
                                  BossStage currentStage) {

            }
        },
        SHOOT {
            @Override
            public int duration() {
                return 750 / 5;
            }

            @Override
            public AttackState nextState(BossStage currentStage) {
                return PAUSE_AFTER_SHOOT;
            }

            @Override
            public void executeAs(ThirdBoss asBoss,
                                  List<DummyBoss> dummies,
                                  BossStage currentStage) {
                for (DummyBoss dummy : dummies) {
                    dummy.getBehaviorSet().setAttackBehavior(new DummyAttackBehavior(dummy));
                }
            }
        },
        PAUSE_AFTER_SHOOT {
            private int numWarps = 0;

            @Override
            public int duration() {
                return 250 / 5;
            }

            @Override
            public AttackState nextState(BossStage currentStage) {
                if (numWarps >= currentStage.getWarpCount()) {
                    numWarps = 0;
                    return RETURN_TO_CENTER;
                }
                numWarps++;
                return WARP_OUT;
            }

            @Override
            public void executeAs(ThirdBoss asBoss,
                                  List<DummyBoss> dummies,
                                  BossStage currentStage) {
                for (DummyBoss dummy : dummies) {
                    dummy.getBehaviorSet().setAttackBehavior(new DoNothingAttackBehavior(dummy));
                }
            }
        },
        RETURN_TO_CENTER {
            @Override
            public int duration() {
                return 4000 / 5;
            }

            @Override
            public AttackState nextState(BossStage currentStage) {
                return WARP_OUT;
            }

            @Override
            public void executeAs(ThirdBoss asBoss,
                                  List<DummyBoss> dummies,
                                  BossStage currentStage) {
                asBoss.owningRoom.removeEntities(dummies);
                dummies.clear();
                asBoss.setEntityPosition(returnPosition.get(0), returnPosition.get(1));
                returnPosition = null;
                asBoss.getBehaviorSet().setMovementBehavior(new WanderMovementBehavior(asBoss));
            }
        };

        public abstract int duration();

        public abstract AttackState nextState(BossStage currentStage);

        public abstract void executeAs(ThirdBoss asBoss,
                                       List<DummyBoss> dummies,
                                       BossStage currentStage);
    }

    private static class DummyBoss extends Boss {

        private final ThirdBoss prototype;
        private final Vector warpPoint;

        public DummyBoss(ThirdBoss prototype, Vector warpPoint) {
            super(defaultSprite(), prototype.owningRoom, defaultDummyStatistics(prototype));
            this.prototype = prototype;
            this.warpPoint = warpPoint;
            getBehaviorSet().setMovementBehavior(new DoNothingMovementBehavior(this));
            setEntityPosition(warpPoint.get(0), warpPoint.get(1));
        }

        private static EntityStatistics defaultDummyStatistics(ThirdBoss prototype) {
            return new EntityStatistics(
                    prototype.getEntityStatistics().getMaxHealth(),
                    prototype.getEntityStatistics().getModifiedAttackDamage(),
                    10,
                    0,
                    1,
                    1,
                    1
            );
        }

        @Override
        public int getDanger() {
            return 20;
        }

        @Override
        public void adjustHealth(int dHealth) {
            prototype.adjustHealth(dHealth);
        }
    }

    private static class DummyAttackBehavior extends AttackBehavior<DummyBoss> {

        public DummyAttackBehavior(DummyBoss owningEntity) {
            super(owningEntity);
            attackDirection = new Vector(owningEntity.warpPoint.get(2),
                                         owningEntity.warpPoint.get(3));
        }

        @Override
        protected List<? extends Entity> getDefaultAttackEntities() {
            return List.of(getBullet());
        }

        @Override
        protected void setAttackDirection() {

        }

        private Projectile getBullet() {
            EntityStatistics bulletStats = new EntityStatistics();
            bulletStats.setSpeed(15);
            bulletStats.setAttackDamage(owningEntity.getEntityStatistics()
                                                    .getModifiedAttackDamage());
            Projectile bullet = new EnemyProjectile(Sprite.asCircle(15, Color.HOTPINK),
                                                    owningEntity.getOwningRoom(),
                                                    attackDirection,
                                                    2000,
                                                    bulletStats);

            bullet.setEntityPosition(owningEntity.getDisplayNode()
                                                 .getBoundsInParent()
                                                 .getCenterX(),
                                     owningEntity.getDisplayNode()
                                                 .getBoundsInParent()
                                                 .getCenterY());
            return bullet;
        }

    }

}
