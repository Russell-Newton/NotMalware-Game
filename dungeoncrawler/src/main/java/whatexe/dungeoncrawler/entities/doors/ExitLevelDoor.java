package whatexe.dungeoncrawler.entities.doors;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.overlap.DoorOverlapBehavior;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.io.IOException;
import java.util.Map;

public class ExitLevelDoor extends SimpleDoor {

    private static final int END_DEPTH = 2;

    public ExitLevelDoor(Level owningLevel,
                         Rectangle2D hitbox,
                         Room owningRoom) {
        super(owningLevel, null, hitbox, owningRoom);
        canTick = true;

        getBehaviorSet().setOverlapBehavior(new DoorOverlapBehavior(this) {
            @Override
            public void handleOverlap(Entity otherEntity) {
                attemptLeave();
            }
        });
    }

    public ExitLevelDoor(Level owningLevel,
                         Room owningRoom) {
        super(owningLevel, null, owningRoom);
        canTick = true;
    }

    public void attemptLeave() {
        if (!isLocked.get()) {
            if (owningLevel.getDepth() == END_DEPTH) {
                try {
                    MainApp.switchScene("EndScreen", Map.of(
                            "ending",
                            true,
                            "time",
                            owningRoom.getPlayer().getTicks() * 5,
                            "killed",
                            owningRoom.getPlayer().getEnemiesKilled(),
                            "damageTaken",
                            owningRoom.getPlayer().getDmgTaken()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                LevelController controller =
                        (LevelController) SceneManager.getInstance().getSceneController("Level");
                controller.setState(Map.of("depth", owningLevel.getDepth() + 1));
            }
        }
    }

    @Override
    protected Image prepareLockedImage() {
        try {
            return new Image(getClass().getResource("exit_door_locked.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Image prepareUnlockedImage() {
        try {
            return new Image(getClass().getResource("exit_door_unlocked.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
