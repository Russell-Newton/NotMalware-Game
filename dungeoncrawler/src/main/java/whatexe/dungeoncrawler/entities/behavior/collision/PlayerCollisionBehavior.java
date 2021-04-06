package whatexe.dungeoncrawler.entities.behavior.collision;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.doors.Door;
import whatexe.dungeoncrawler.entities.doors.ExitLevelDoor;
import whatexe.dungeoncrawler.entities.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerCollisionBehavior extends CollisionBehavior<Player> {
    public PlayerCollisionBehavior(Player owningEntity) {
        super(owningEntity);
    }

    @Override
    public void handleCollisionWithEntity(Entity otherEntity) {

    }

    @Override
    public List<? extends Entity> getPossibleCollisionTargets() {
        List<Door> lockedDoors = new ArrayList<>();
        for (Door door : owningEntity.getOwningRoom().getDoors()) {
            if (!(door instanceof ExitLevelDoor) && door.isLocked()) {
                lockedDoors.add(door);
            }
        }
        return lockedDoors;
    }

    @Override
    public void handleCollisionWithBoundary() {

    }
}
