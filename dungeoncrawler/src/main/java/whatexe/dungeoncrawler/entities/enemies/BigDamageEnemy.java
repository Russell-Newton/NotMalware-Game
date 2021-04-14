package whatexe.dungeoncrawler.entities.enemies;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.AnimatedSprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.movement.FollowingMovementBehavior;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.ArrayList;
import java.util.List;

public class BigDamageEnemy extends Enemy {
    public BigDamageEnemy(Room owningRoom) {
        super(new AnimatedSprite(
                new Image(Enemy.class.getResourceAsStream("mcafee.png")),
                32,
                32,
                4,
                4,
                1,
                1000), 10, owningRoom);

        entityStatistics.setSpeed(1);
        entityStatistics.setAttackDamage(10);

        behaviorSet.get().setMovementBehavior(
                new FollowingMovementBehavior(this, () -> {
                    List<Entity> targets = new ArrayList<>();
                    if (owningRoom.getPlayer() != null) {
                        targets.add(owningRoom.getPlayer());
                    }
                    targets.addAll(owningRoom.getFriends());
                    return owningRoom.getVisibleEntities(targets);
                }));
    }

    @Override
    public int getDanger() {
        return 3;
    }
}
