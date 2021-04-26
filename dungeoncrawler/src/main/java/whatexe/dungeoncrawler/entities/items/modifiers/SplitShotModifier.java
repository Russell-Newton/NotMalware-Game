package whatexe.dungeoncrawler.entities.items.modifiers;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.transformations.BehaviorTransformation;
import whatexe.dungeoncrawler.entities.behavior.transformations.SplittingProjectilesTransformation;
import whatexe.dungeoncrawler.layout.rooms.Room;

public class SplitShotModifier extends Modifier {
    public SplitShotModifier(Room owningRoom) {
        super(getSprite(), owningRoom);
    }

    public SplitShotModifier(Entity owningEntity) {
        super(getSprite(), owningEntity);
    }

    private static Sprite getSprite() {
        return new Sprite(new Image(SplitShotModifier.class.getResourceAsStream(
                "splitshot.png")));
    }

    @Override
    protected BehaviorTransformation<? extends Entity> getNewTransformation(Entity owningEntity) {
        return new SplittingProjectilesTransformation(owningEntity);
    }

    @Override
    public String getDescription() {
        return "Projectiles split into four other on impact. Split-off projectiles inherit "
                + "certain transformations";
    }
}
