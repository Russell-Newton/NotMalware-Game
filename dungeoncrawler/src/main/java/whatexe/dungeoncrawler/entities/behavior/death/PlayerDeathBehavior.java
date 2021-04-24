package whatexe.dungeoncrawler.entities.behavior.death;

import javafx.beans.property.SimpleBooleanProperty;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.player.Player;

public class PlayerDeathBehavior extends DeathBehavior<Player> {

    private final SimpleBooleanProperty isDead;

    public PlayerDeathBehavior(Player owningEntity) {
        super(owningEntity);

        isDead = new SimpleBooleanProperty(false);
        isDead.addListener((__, oldValue, newValue) -> switchToEndScreen());
    }

    private void switchToEndScreen() {
        ((LevelController) SceneManager.getInstance().getSceneController("Level"))
                .transitionToEndScreen(false);
    }

    @Override
    public void die() {
        isDead.set(true);
    }
}
