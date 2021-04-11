package whatexe.dungeoncrawler.entities.effects;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.miscellaneous.Particle;

public class InvisibilityEffect extends StatusEffect {
    public InvisibilityEffect(Entity owningEntity, int lifetime) {
        super(owningEntity, lifetime);
    }

    @Override
    protected void onAdd() {
        owningEntity.getDisplayNode().setOpacity(0.5);
    }

    @Override
    protected void onRemove() {
        owningEntity.getDisplayNode().setOpacity(1);
    }

    @Override
    protected void onTick() {

    }

    @Override
    protected Particle getDefaultParticle() {
        return new Particle(owningEntity.getOwningRoom(), 5, Color.LIGHTBLUE, 40);
    }
}
