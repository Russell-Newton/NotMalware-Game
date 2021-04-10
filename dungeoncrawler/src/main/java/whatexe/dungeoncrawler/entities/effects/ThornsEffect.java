package whatexe.dungeoncrawler.entities.effects;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.miscellaneous.Particle;

public class ThornsEffect extends StatusEffect {
    public ThornsEffect(Entity owningEntity, int lifetime) {
        super(owningEntity, lifetime);
    }

    @Override
    protected void onAdd() {

    }

    @Override
    protected void onRemove() {

    }

    @Override
    protected void onTick() {

    }

    @Override
    protected Particle getDefaultParticle() {
        return new Particle(owningEntity.getOwningRoom(), 5, Color.SEAGREEN, 40);
    }
}
