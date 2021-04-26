package whatexe.dungeoncrawler.entities.items.modifiers;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.transformations.BehaviorTransformation;
import whatexe.dungeoncrawler.entities.behavior.transformations.BendingProjectileTransformation;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class SpoonBenderModifier extends Modifier {
    public SpoonBenderModifier(Room owningRoom) {
        super(new Sprite(
                      new Image(SpoonBenderModifier.class.getResourceAsStream("spoonbender.png"))),
              owningRoom);
    }

    public SpoonBenderModifier(Entity owningEntity) {
        super(new Sprite(
                      new Image(SpoonBenderModifier.class.getResourceAsStream("spoonbender.png"))),
              owningEntity);
    }

    @Override
    protected BehaviorTransformation<? extends Entity> getNewTransformation(Entity owningEntity) {
        return new BendingProjectileTransformation(owningEntity);
    }

    @Override
    public String getDescription() {
        return "Projectiles bend towards enemies, as if pulled by gravity.";
    }
}
