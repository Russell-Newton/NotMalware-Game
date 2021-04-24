package whatexe.dungeoncrawler.entities.enemies;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.AnimatedSprite;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class MeleeEnemy extends Enemy {
    public MeleeEnemy(Room owningRoom) {
        super(new AnimatedSprite(
                new Image(Enemy.class.getResourceAsStream("folder.png")),
                32,
                32,
                4,
                4,
                1,
                1000), 20, owningRoom);

        entityStatistics.setSpeed(0.8);
        entityStatistics.setAttackDamage(3);

    }

    @Override
    public int getDanger() {
        return 0;
    }
}
