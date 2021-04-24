package whatexe.dungeoncrawler.entities.items;

import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.death.RemoveOnDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.CurrencyOverlapBehavior;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.io.IOException;

public class Currency extends Item {

    public Currency(Room owningRoom) {
        super(getDefaultDisplayNode(), owningRoom);
        getBehaviorSet().setOverlapBehavior(new CurrencyOverlapBehavior(this));
        getBehaviorSet().setDeathBehavior(new RemoveOnDeathBehavior(this));
    }

    public static Sprite getDefaultDisplayNode() {
        try {
            return new Sprite(new Image(Currency.class.getResource("currency.png")
                                                      .openStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getPrice() {
        return 0;
    }
}
