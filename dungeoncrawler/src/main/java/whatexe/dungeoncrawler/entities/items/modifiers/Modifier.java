package whatexe.dungeoncrawler.entities.items.modifiers;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.transformations.BehaviorTransformation;
import whatexe.dungeoncrawler.entities.items.Equippable;
import whatexe.dungeoncrawler.entities.items.Item;
import whatexe.dungeoncrawler.entities.player.Player;
import whatexe.dungeoncrawler.layout.rooms.Room;

public abstract class Modifier extends Item implements Equippable {

    protected BehaviorTransformation<? extends Entity> transformation;

    public Modifier(Sprite displayNode, Room owningRoom) {
        super(displayNode, owningRoom);
    }

    public Modifier(Sprite displayNode,
                    Entity owningEntity) {
        super(displayNode, owningEntity);
    }

    @Override
    public void setOwningEntity(Entity owningEntity) {
        super.setOwningEntity(owningEntity);
        assert owningEntity instanceof Player;
        transformation = getNewTransformation(owningEntity);
    }

    @Override
    public BehaviorTransformation<? extends Entity> getEquipTransformation() {
        return transformation;
    }

    protected abstract BehaviorTransformation<? extends Entity> getNewTransformation(
            Entity owningEntity);

    @Override
    public int getPrice() {
        return 80;
    }
}
