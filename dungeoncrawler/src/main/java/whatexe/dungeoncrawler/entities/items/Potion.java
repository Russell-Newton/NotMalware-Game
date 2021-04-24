package whatexe.dungeoncrawler.entities.items;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.layout.rooms.Room;

public abstract class Potion extends Item implements Consumable {

    public Potion(Sprite displayNode, Room owningRoom) {
        super(displayNode, owningRoom);
    }

    public Potion(Sprite displayNode, Entity owningEntity) {
        super(displayNode, owningEntity);
    }

    @Override
    public int getPrice() {
        return 20;
    }
}
