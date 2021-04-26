package whatexe.dungeoncrawler.entities.items;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.effects.HealthBoostEffect;
import whatexe.dungeoncrawler.entities.effects.StatusEffect;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class HealthBoostPotion extends Potion {
    public HealthBoostPotion(Room owningRoom) {
        super(getSprite(), owningRoom);
    }

    public HealthBoostPotion(Entity owningEntity) {
        super(getSprite(), owningEntity);
    }

    private static Sprite getSprite() {
        return new Sprite(new Image(
                HealthPotion.class.getResourceAsStream("health_boost_potion.png")));
    }

    @Override
    public String getDescription() {
        return "Increases max HP by 30 for 30 seconds.";
    }

    @Override
    public StatusEffect getConsumeEffect() {
        return new HealthBoostEffect(owningEntity);
    }
}
