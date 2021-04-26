package whatexe.dungeoncrawler.entities.items;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.effects.RegenerationEffect;
import whatexe.dungeoncrawler.entities.effects.StatusEffect;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class HealthPotion extends Potion {

    public HealthPotion(Room owningRoom) {
        super(new Sprite(new Image(
                HealthPotion.class.getResourceAsStream("health_potion.png"))), owningRoom);
    }

    public HealthPotion(Entity owningEntity) {
        super(new Sprite(new Image(
                HealthPotion.class.getResourceAsStream("health_potion.png"))), owningEntity);
    }

    @Override
    public StatusEffect getConsumeEffect() {
        return new RegenerationEffect(owningEntity);
    }

    @Override
    public String getDescription() {
        return "Regenerates 30 health instantly.";
    }
}
