package whatexe.dungeoncrawler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import whatexe.dungeoncrawler.controllers.ConfigurationController;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.BehaviorSet;
import whatexe.dungeoncrawler.entities.behavior.attack.DoNothingAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.attack.PlayerAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.collision.DoNothingCollisionBehavior;
import whatexe.dungeoncrawler.entities.behavior.death.RemoveOnDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.DoNothingMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.FollowingMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.EnemyOverlapBehavior;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Disabled
@Deprecated(since = "Since player attacking overhaul")
public class EnemyPlayerInteractionTest extends ApplicationTest {

    private LevelController controller;
    private DummyPlayerAttackBehavior dummyPlayerAttackBehavior;
    private DummyEnemy enemy;

    @Override
    public void start(Stage stage) throws Exception {
        new MainApp().start(stage);

        SceneManager.getInstance().addScene("Level",
                                            ConfigurationController.class.getResource(
                                                    "LevelDisplay.fxml"));
        MainApp.switchScene("Level");

        stage.toFront();
    }

    @BeforeEach
    public void reset() {
        controller =
                (LevelController) SceneManager.getInstance().getSceneController("Level");
        interact(() -> {
            dummyPlayerAttackBehavior = new DummyPlayerAttackBehavior(controller.getPlayer());
            controller.getPlayer().getBehaviorSet().setAttackBehavior(dummyPlayerAttackBehavior);
            controller.getPlayer().getEntityStatistics().setAttackDamage(1);

            enemy = new DummyEnemy(10, controller.getCurrentRoom());
            enemy.setEntityPosition(300, 300);

            controller.getCurrentRoom().getEnemies().addAll(enemy);
        });
    }

    @AfterEach
    public void shutdown() throws Exception {
        FxToolkit.hideStage();

        release(new KeyCode[]{});
        release(new MouseButton[]{});

        SceneManager.getInstance().clear();
    }

    @Test
    public void testEnemyDeath() {
        try {
            press(KeyCode.SPACE);

            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> enemy.getHealth() <= 0);

            assertEquals(controller.getCurrentRoom().getEnemies().size(), 0);
        } catch (TimeoutException e) {
            fail();
        }
    }

    @Test
    public void testPlayerDeath() {
        Player player = controller.getPlayer();
        interact(() -> {
            player.getBehaviorSet().setDeathBehavior(new RemoveOnDeathBehavior(player));

            enemy.getBehaviorSet().setMovementBehavior(
                    new FollowingMovementBehavior(enemy, () -> {
                        List<Entity> targets = new ArrayList<>();
                        targets.add(player);
                        targets.addAll(player.getOwningRoom().getFriends());
                        return targets;
                    }));
        });

        try {
            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> player.getHealth() <= 0);
        } catch (TimeoutException e) {
            fail();
        }

    }

    private static class DummyEnemy extends Enemy {

        private int hitCount;

        public DummyEnemy(int startingHealth,
                          Room owningRoom) {
            super(Sprite.asRectangle(20, 20, Color.RED), startingHealth, owningRoom);

            entityStatistics.setAttackDamage(10);

            behaviorSet.set(new BehaviorSet(
                    new DoNothingAttackBehavior(this),
                    new DoNothingMovementBehavior(this),
                    new RemoveOnDeathBehavior(this),
                    new EnemyOverlapBehavior(this),
                    new DoNothingCollisionBehavior(this)
            ));

            healthProperty().addListener((__, oldValue, newValue) -> {
                hitCount++;
            });
        }

        public int getHitCount() {
            return hitCount;
        }

        @Override
        public int getDanger() {
            return Integer.MAX_VALUE;
        }
    }

    private static class DummyPlayerAttackBehavior extends PlayerAttackBehavior {

        private int attackCount;

        public DummyPlayerAttackBehavior(Player owningEntity) {
            super(owningEntity);
        }

        @Override
        public List<? extends Entity> attack() {
            attackCount++;
            return super.attack();
        }

        public int getAttackCount() {
            return attackCount;
        }
    }
}
