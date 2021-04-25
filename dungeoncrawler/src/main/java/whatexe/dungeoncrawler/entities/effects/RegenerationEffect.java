package whatexe.dungeoncrawler.entities.effects;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.miscellaneous.Particle;

public class RegenerationEffect extends StatusEffect {

    public RegenerationEffect(Entity owningEntity) {
        super(owningEntity, 10);
    }

    @Override
    protected void onAdd() {
        owningEntity.setHealth(Math.min(owningEntity.getHealth() + 30,
                                         owningEntity.getEntityStatistics().getMaxHealth()));
    }

    @Override
    protected void onRemove() {
    }

    @Override
    protected void onTick() {
    }

    @Override
    protected Particle getDefaultParticle() {
        return new Particle(owningEntity.getOwningRoom(), 5, Color.INDIANRED, 40);
    }
}
