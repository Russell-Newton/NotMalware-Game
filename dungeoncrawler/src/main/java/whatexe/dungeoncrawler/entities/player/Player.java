package whatexe.dungeoncrawler.entities.player;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.SceneManager;
import whatexe.dungeoncrawler.controllers.InventoryController;
import whatexe.dungeoncrawler.controllers.LevelController;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.presets.PlayerBehaviorSet;
import whatexe.dungeoncrawler.entities.friends.Friend;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.layout.rooms.Room;
import whatexe.dungeoncrawler.music.MusicManager;

import java.io.IOException;


public class Player extends Friend {

    private static final double DAMAGE_DELAY = 50;

    private final Inventory inventory;
    private final SimpleIntegerProperty money;
    private final MalwareType malwareType;
    private boolean isInventoryOpen = false;
    private boolean isPauseMenuOpen = false;
    private double ticksSinceDamaged;

    private double dmgTaken;
    private int ticks;
    private int enemiesKilled;

    private SubScene pauseMenuSubScene;


    public Player(MalwareType malwareType) {
        super(malwareType.getDefaultSprite(), null, malwareType.getDefaultStatModifiers());
        canTick = true;

        money = new SimpleIntegerProperty();
        inventory = new Inventory(this);
        this.malwareType = malwareType;

        entityStatistics.setSpeed(2);

        behaviorSet.set(malwareType.getDefaultBehaviorSet(this));
        setDisplayScale(1.25);

        dmgTaken = 0;
        enemiesKilled = 0;
        ticks = 0;

        if (SceneManager.getInstance().getLoaderMap().containsKey("Pause Menu")) {
            SceneManager.getInstance().unloadScene("Pause Menu");
            SceneManager.getInstance().removeScene("Pause Menu");
        }
        SceneManager.getInstance()
                    .addScene("Pause Menu",
                              LevelController.class.getResource("PauseMenu.fxml"));
        try {
            pauseMenuSubScene =
                    new SubScene(SceneManager.getInstance().loadParent("Pause Menu"),
                                 418,
                                 284);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EntityStatistics getDefaultPlayerStatistics() {
        return new EntityStatistics(50,
                                    5,
                                    50,
                                    2,
                                    1,
                                    1,
                                    1);
    }

    public boolean isInventoryFull() {
        return inventory.isInventoryFull();
    }

    public void operateInventory() {
        if (!isPauseMenuOpen) {
            if (!isInventoryOpen) {
                isInventoryOpen = true;
                ((InventoryController) SceneManager
                        .getInstance().getSceneController("Inventory")).updateInventory();
                ((Pane) SceneManager.getInstance().getParent("Level"))
                        .getChildren().add(inventory.getPane());
                MainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);

                MusicManager.getInstance().pauseActive();
                MusicManager.getInstance().playTrack("Elevator");
                getOwningRoom().stopTicking();
            } else {
                isInventoryOpen = false;
                ((Pane) SceneManager.getInstance().getParent("Level"))
                        .getChildren().remove(inventory.getPane());
                MainApp.getPrimaryStage().getScene().setCursor(Cursor.NONE);

                getOwningRoom().startTicking();
                MusicManager.getInstance().stopTrack("Elevator");
                MusicManager.getInstance().unpauseActive();
            }
        }
    }

    public void operatePauseMenu() {
        if (isPauseMenuOpen) {
            isPauseMenuOpen = false;
            ((Pane) MainApp.getPrimaryStage().getScene().getRoot())
                    .getChildren().remove(pauseMenuSubScene);
            MainApp.getPrimaryStage().getScene().setCursor(Cursor.NONE);

            getOwningRoom().startTicking();
            MusicManager.getInstance().stopTrack("Elevator");
            MusicManager.getInstance().unpauseActive();
        } else {
            isPauseMenuOpen = true;
            ((Pane) MainApp.getPrimaryStage().getScene().getRoot())
                    .getChildren().add(pauseMenuSubScene);
            MainApp.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
            if (isInventoryOpen) {
                isInventoryOpen = false;
                ((Pane) SceneManager.getInstance().getParent("Level"))
                        .getChildren().remove(inventory.getPane());
            } else {
                MusicManager.getInstance().pauseActive();
                MusicManager.getInstance().playTrack("Elevator");
            }
            getOwningRoom().stopTicking();
        }
    }

    @Override
    public void tick() {
        super.tick();
        ticksSinceDamaged = Math.max(0, ticksSinceDamaged - 1);
        ticks++;
    }

    @Override
    public void adjustHealth(int dHealth) {
        if (ticksSinceDamaged == 0) {
            if (dHealth < 0) {
                dmgTaken -= dHealth;
            }
            health.set(Math.min(health.getValue() + dHealth, entityStatistics.getMaxHealth()));
            ticksSinceDamaged = DAMAGE_DELAY;
        }
    }

    public void initControls() {
        ((PlayerBehaviorSet) behaviorSet.get()).initControls();
    }

    public SimpleIntegerProperty moneyProperty() {
        return money;
    }

    public int getMoney() {
        return money.get();
    }

    public void setMoney(int money) {
        this.money.set(money);
    }

    public void adjustMoney(int dMoney) {
        money.set(money.getValue() + dMoney);
    }

    public void setCurrentRoom(Room currentRoom) {
        this.owningRoom = currentRoom;
    }

    public MalwareType getMalwareType() {
        return malwareType;
    }

    public double getDmgTaken() {
        return dmgTaken;
    }

    public void addEnemiesKilled(int enemiesKilled) {
        this.enemiesKilled += enemiesKilled;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public int getTicks() {
        return ticks;
    }

    public void recharge() {
        ((PlayerBehaviorSet) getBehaviorSet()).getSpecialAttackBehavior().recharge();
    }

    public SimpleIntegerProperty specialAttackChargeProperty() {
        return ((PlayerBehaviorSet) getBehaviorSet()).getSpecialAttackBehavior().chargeProperty();
    }

    public int getSpecialAttackMaxCharge() {
        return ((PlayerBehaviorSet) getBehaviorSet()).getSpecialAttackBehavior().getMaxCharge();
    }

    public boolean addItem(Item item) {
        return inventory.addItem(item);
    }

    public Inventory getInventory() {
        return inventory;
    }

}
