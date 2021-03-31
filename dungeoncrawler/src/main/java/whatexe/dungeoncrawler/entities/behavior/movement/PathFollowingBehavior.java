package whatexe.dungeoncrawler.entities.behavior.movement;

import javafx.beans.property.SimpleObjectProperty;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;

import java.util.Arrays;
import java.util.List;

/**
 * Uses linear interpolation to return the necessary displacement for an entity moving along a
 * path. Assumes the entity actually moves every tick.
 */
public class PathFollowingBehavior extends MovementBehavior<Entity> {

    private final List<Vector> path;
    private final SimpleObjectProperty<FollowType> followType;
    private Vector currentPosition;
    private int currentIndex;
    private boolean movingForward;

    public PathFollowingBehavior(Entity owningEntity, FollowType followType, List<Vector> path) {
        super(owningEntity);

        this.path = path;
        this.followType = new SimpleObjectProperty<>(followType);

        if (path.size() <= 1) {
            currentPosition = new Vector(0, 0);
        } else {
            currentPosition = this.path.get(0);
        }

        movingForward = true;
    }

    public PathFollowingBehavior(Entity owningEntity, FollowType followType, Vector... path) {
        this(owningEntity, followType, Arrays.asList(path));
    }

    @Override
    public Vector getMovement() {
        if (path.size() <= 1) {
            return new Vector(0, 0);
        }

        Vector direction;
        Vector next;
        Vector previous;

        if (movingForward) {
            next = path.get(currentIndex + 1);
            previous = path.get(currentIndex);
        } else {
            next = path.get(currentIndex);
            previous = path.get(currentIndex + 1);
        }

        direction = next.minus(previous).unit();

        Vector toNext = next.minus(currentPosition);
        Vector movement;
        if (toNext.magnitude() < owningEntity.getEntityStatistics().getModifiedSpeed()) {
            movement = direction.scaledBy(toNext.magnitude());
            currentIndex += movingForward ? 1 : -1;
        } else {
            movement = direction.scaledBy(owningEntity.getEntityStatistics().getModifiedSpeed());
        }

        if (movingForward && currentIndex == path.size() - 1) {
            switch (getFollowType()) {
            case LOOP:
                currentIndex = 0;
                movement = path.get(0).minus(path.get(path.size() - 1)).plus(movement);
                break;
            case AUTO_REVERSE:
                currentIndex--;
                movingForward = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getFollowType());
            }
        } else if (!movingForward && currentIndex == -1) {
            switch (getFollowType()) {
            case LOOP:
                currentIndex = path.size() - 2;
                movement = path.get(path.size() - 1).minus(path.get(0)).plus(movement);
                break;
            case AUTO_REVERSE:
                currentIndex++;
                movingForward = true;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getFollowType());
            }
        }

        currentPosition = currentPosition.plus(movement);

        return movement;
    }

    public List<Vector> getPath() {
        return path;
    }

    public FollowType getFollowType() {
        return followType.get();
    }

    public SimpleObjectProperty<FollowType> followTypeProperty() {
        return followType;
    }

    public enum FollowType {
        LOOP,
        AUTO_REVERSE
    }
}
