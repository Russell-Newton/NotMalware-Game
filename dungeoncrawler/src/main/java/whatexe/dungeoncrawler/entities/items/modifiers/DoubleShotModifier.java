package whatexe.dungeoncrawler.entities.items.modifiers;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.transformations.BehaviorTransformation;
import whatexe.dungeoncrawler.entities.behavior.transformations.DoubleShotTransformation;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class DoubleShotModifier extends Modifier {
    public DoubleShotModifier(Room owningRoom) {
        super(new Sprite(
                      new Image(DoubleShotModifier.class.getResourceAsStream("doubleshot.png"))),
              owningRoom);
    }

    public DoubleShotModifier(Entity owningEntity) {
        super(new Sprite(
                      new Image(DoubleShotModifier.class.getResourceAsStream(
                              "doubleshot.png"))),
              owningEntity);
    }

    @Override
    protected BehaviorTransformation<? extends Entity> getNewTransformation(Entity owningEntity) {
        return new DoubleShotTransformation(owningEntity);
    }

    @Override
    public String getDescription() {
        return "Projectiles are doubled.";
    }
}
