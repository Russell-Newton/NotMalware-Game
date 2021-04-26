package whatexe.dungeoncrawler.layout.rooms;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.controllers.BossHealthBarController;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.death.RemoveOnDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.OverlapBehavior;
import whatexe.dungeoncrawler.entities.doors.Door;
import whatexe.dungeoncrawler.entities.doors.ExitLevelDoor;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.enemies.bosses.Boss;
import whatexe.dungeoncrawler.entities.enemies.bosses.FirstBoss;
import whatexe.dungeoncrawler.entities.enemies.bosses.SecondBoss;
import whatexe.dungeoncrawler.entities.enemies.bosses.ThirdBoss;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.dungeoncrawler.music.MusicManager;
import whatexe.tileengine.MapObject;
import whatexe.tileengine.TiledMap;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class BossRoom extends SimpleRoom {

    private static final int BOSS_SPAWN_DELAY_TICKS = 4000 / 5;

    private static final Function<BossRoom, Boss>[] BOSS_OPTIONS = loadBossOptions();

    private final Pane healthBar;
    private final DoubleProperty healthBarProgressProperty;
    private boolean spawningBoss;
    private int spawningBossTimer;

    public BossRoom(TiledMap fromTiledMap, Level owningLevel, Direction fromDirection) {
        super(fromTiledMap, owningLevel, fromDirection);

        DoubleProperty tempProperty = null;
        Pane tempPane = new Pane();
        try {
            FXMLLoader loader = new FXMLLoader(Enemy.class.getResource("BossHealthBar.fxml"));
            tempPane = loader.load();
            BossHealthBarController healthBarController = loader.getController();
            tempProperty = healthBarController.getHealthBar().progressProperty();
        } catch (IOException e) {
            e.printStackTrace();
        }
        healthBar = tempPane;
        healthBarProgressProperty = tempProperty;
    }

    @SuppressWarnings("unchecked")
    private static Function<BossRoom, Boss>[] loadBossOptions() {
        Function<BossRoom, Boss> option1 = FirstBoss::new;
        Function<BossRoom, Boss> option2 = SecondBoss::new;
        Function<BossRoom, Boss> option3 = ThirdBoss::new;
        return new Function[]{option1, option2, option3};
    }

    @Override
    protected void initDoors(Direction[] directionsToGenerate) {
        super.initDoors(directionsToGenerate);
    }

    @Override
    protected void initEntities() {
        MapObject triggerZoneObject =
                fromTiledMap.getObjectGroups().get("Trigger").getObjects().get(0);
        Rectangle2D triggerZone = new Rectangle2D(triggerZoneObject.getX(),
                                                  triggerZoneObject.getY(),
                                                  triggerZoneObject.getWidth(),
                                                  triggerZoneObject.getHeight());
        BossTrigger trigger = new BossTrigger(this);
        trigger.setEntityPosition(triggerZone.getMinX(),
                                  triggerZone.getMinY());
        miscEntities.add(trigger);
    }

    @Override
    protected void handleOnRoomClear() {
        super.handleOnRoomClear();
        Door door = new ExitLevelDoor(owningLevel,
                                      new Rectangle2D(0, 0, 32, 32),
                                      this);
        this.doors.add(door);
        door.setEntityPosition(this.backgroundImage.getWidth() / 2 - 16,
                               this.backgroundImage.getHeight() / 2 - 16);

        updateDoorLocks();
    }

    private void startSpawnBossTimer() {
        spawningBoss = true;
        spawningBossTimer = BOSS_SPAWN_DELAY_TICKS;
        lockAllDoors();
        MusicManager.getInstance().pauseTrack("RegularPlay");
        MusicManager.getInstance().playTrack("Boss");

        Pane levelDisplayParent = ((Pane) SceneManager.getInstance().getParent("Level"));
        healthBar.setTranslateX((levelDisplayParent.getPrefWidth() - healthBar.getPrefWidth()) / 2);
        healthBar.setTranslateY(30);
        levelDisplayParent.getChildren().add(healthBar);
    }

    private void spawnBoss() {
        MapObject spawnZoneObject = fromTiledMap.getObjectGroups().get("Boss").getObjects().get(0);
        Rectangle2D spawnZone = new Rectangle2D(spawnZoneObject.getX(),
                                                spawnZoneObject.getY(),
                                                spawnZoneObject.getWidth(),
                                                spawnZoneObject.getHeight());

        Boss boss = BOSS_OPTIONS[owningLevel.getDepth()].apply(this);
        boss.setEntityPosition(spawnZone.getMinX(),
                               spawnZone.getMinY());
        enemies.add(boss);
        boss.healthProperty().addListener((__, oldValue, newValue) -> {
            healthBarProgressProperty.set(
                    (double) boss.getHealth() / boss.getEntityStatistics().getMaxHealth());
        });

        enemies.addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends Enemy> c) {
                if (!enemies.contains(boss)) {
                    MusicManager.getInstance().stopTrack("Boss");
                    MusicManager.getInstance().playTrack("RegularPlay");
                    ((Pane) SceneManager.getInstance().getParent("Level"))
                            .getChildren()
                            .remove(healthBar);
                }
            }
        });
    }

    @Override
    protected void tickEntities() {
        super.tickEntities();
        if (spawningBoss) {
            if (spawningBossTimer >= 0) {
                spawningBossTimer--;

                healthBarProgressProperty
                        .set(1 - ((double) spawningBossTimer / BOSS_SPAWN_DELAY_TICKS));
            } else {
                spawnBoss();
                spawningBoss = false;
            }
        }
    }

    public Pane getHealthBar() {
        return healthBar;
    }

    public double getHealthBarProgressProperty() {
        return healthBarProgressProperty.get();
    }

    public DoubleProperty healthBarProgressPropertyProperty() {
        return healthBarProgressProperty;
    }

    private class BossTrigger extends Entity {
        public BossTrigger(Room owningRoom) {
            super(Sprite.asRectangle(32, 32, Color.DARKRED), owningRoom);
            canTick = true;

            getBehaviorSet().setOverlapBehavior(new TriggerOverlapBehavior(this));
            getBehaviorSet().setDeathBehavior(new RemoveOnDeathBehavior(this));
        }

        private class TriggerOverlapBehavior extends OverlapBehavior<BossTrigger> {

            public TriggerOverlapBehavior(BossTrigger owningEntity) {
                super(owningEntity);
            }

            @Override
            public void handleOverlap(Entity otherEntity) {
                startSpawnBossTimer();
                setHealth(-1);
            }

            @Override
            public List<? extends Entity> getPossibleOverlapTargets() {
                if (getPlayer() != null) {
                    return List.of(getPlayer());
                }
                return List.of();
            }
        }
    }
}
