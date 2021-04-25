package whatexe.dungeoncrawler.entities.items;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.effects.AttackBoostEffect;
import whatexe.dungeoncrawler.entities.effects.StatusEffect;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class AttackPotion extends Potion {

    public AttackPotion(Room owningRoom) {
        super(new Sprite(new Image(
                AttackPotion.class.getResourceAsStream("attack_potion.png"))), owningRoom);
    }

    public AttackPotion(Entity owningEntity) {
        super(new Sprite(new Image(
                AttackPotion.class.getResourceAsStream("attack_potion.png"))), owningEntity);
    }


    @Override
    public StatusEffect getConsumeEffect() {
        return new AttackBoostEffect(owningEntity, 6000);
    }

    @Override
    public String getDescription() {
        return "Doubles attack damage for 30 seconds.";
    }
}
