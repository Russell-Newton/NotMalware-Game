package whatexe.dungeoncrawler.entities.items;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.effects.SpeedBoostEffect;
import whatexe.dungeoncrawler.entities.effects.StatusEffect;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class SpeedPotion extends Potion {

    public SpeedPotion(Entity owningEntity) {
        super(new Sprite(new Image(
                SpeedPotion.class.getResourceAsStream("speed_potion.png"))), owningEntity);
    }

    public SpeedPotion(Room owningRoom) {
        super(new Sprite(new Image(
                SpeedPotion.class.getResourceAsStream("speed_potion.png"))), owningRoom);
    }

    @Override
    public StatusEffect getConsumeEffect() {
        return new SpeedBoostEffect(owningEntity, 2000);
    }

    @Override
    public String getDescription() {
        return "Doubles speed for 10 seconds.";
    }
}
