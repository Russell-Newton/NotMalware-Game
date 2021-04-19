package whatexe.dungeoncrawler.entities.doors;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.overlap.DoorOverlapBehavior;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.io.IOException;

public abstract class Door extends Entity {
    protected final Level owningLevel;
    protected final Direction direction;
    protected final Image lockedImageSheet;
    protected final Image unlockedImageSheet;
    protected SimpleBooleanProperty isLocked;

    public Door(Level owningLevel, Direction direction, Rectangle2D hitbox,
                Room owningRoom) {
        super(new Sprite(), hitbox, owningRoom);
        this.owningLevel = owningLevel;
        this.direction = direction;
        this.lockedImageSheet = prepareLockedImage();
        this.unlockedImageSheet = prepareUnlockedImage();
        this.isLocked = new SimpleBooleanProperty(true);

        getBehaviorSet().setOverlapBehavior(new DoorOverlapBehavior(this));
    }

    public Door(Level owningLevel, Direction direction, Room owningRoom) {
        super(new Sprite(), owningRoom);
        this.owningLevel = owningLevel;
        this.direction = direction;
        this.lockedImageSheet = prepareLockedImage();
        this.unlockedImageSheet = prepareUnlockedImage();
        this.isLocked = new SimpleBooleanProperty(true);

        getBehaviorSet().setOverlapBehavior(new DoorOverlapBehavior(this));
    }

    /**
     * When given a list of doors, it will return the first door in that list with the direction
     * specified
     *
     * @param doors     List of doors to search through
     * @param direction The direction of the returning door
     * @return The door from the list with the specified direction
     */
    public static Door getDoorFromList(ObservableList<Door> doors, Direction direction) {
        for (Door door : doors) {
            if (door.direction == direction) {
                return door;
            }
        }
        return null;
    }

    protected Rectangle2D getViewport(Direction direction) {
        int x = 0;
        int y = 0;
        int width = 32;
        int height = 32;
        if (direction != null) {
            switch (direction) {
            case UP:
                width = 64;
                break;
            case LEFT:
                x = 64;
                height = 64;
                break;
            case RIGHT:
                x = 96;
                height = 64;
                break;
            case DOWN:
                y = 32;
                width = 64;
                break;
            default:
                break;
            }
        }
        return new Rectangle2D(x, y, width, height);
    }

    protected Image prepareLockedImage() {
        try {
            return new Image(getClass().getResource("doors_locked.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Image prepareUnlockedImage() {
        try {
            return new Image(getClass().getResource("doors_unlocked.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void updateDoorLock() {
        if (this.isLocked.get()) {
            getDisplayNode().setSpriteSheet(lockedImageSheet);
            // this.displayNode.setStyle("-fx-background-color: lightCoral;");
        } else {
            getDisplayNode().setSpriteSheet(unlockedImageSheet);
            // this.displayNode.setStyle("-fx-background-color: lightGreen;");
        }
        getDisplayNode().setViewport(getViewport(direction));

        updateHitboxPosition();
    }

    public void unlock() {
        this.isLocked.set(false);
        updateDoorLock();
    }

    public void lock() {
        this.isLocked.set(true);
        updateDoorLock();
    }

    public boolean isLocked() {
        return isLocked.get();
    }

    public Level getOwningLevel() {
        return owningLevel;
    }
}
