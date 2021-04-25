package whatexe.dungeoncrawler.entities.items.modifiers;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.transformations.BehaviorTransformation;
import whatexe.dungeoncrawler.entities.behavior.transformations.ExplosiveProjectileTransformation;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class ExplodeShotModifier extends Modifier {
    public ExplodeShotModifier(Entity owningEntity) {
        super(new Sprite(
                      new Image(ExplodeShotModifier.class.getResourceAsStream(
                              "explosiveshot.png"))),
              owningEntity);
    }

    public ExplodeShotModifier(Room owningRoom) {
        super(new Sprite(
                      new Image(ExplodeShotModifier.class.getResourceAsStream(
                              "explosiveshot.png"))),
              owningRoom);
    }

    @Override
    protected BehaviorTransformation<? extends Entity> getNewTransformation(Entity owningEntity) {
        return new ExplosiveProjectileTransformation(owningEntity);
    }

    @Override
    public String getDescription() {
        return "Projectiles explode on impact.\nWARNING! These explosions will damage you!";
    }
}
