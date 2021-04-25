package whatexe.dungeoncrawler.entities.effects;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.miscellaneous.Particle;

public abstract class StatusEffect {

    protected final Entity owningEntity;
    private int lifetime;
    private boolean shouldRemove;

    public StatusEffect(Entity owningEntity, int lifetime) {
        this.owningEntity = owningEntity;
        this.lifetime = lifetime;

        onAdd();
    }

    protected abstract void onAdd();

    protected abstract void onRemove();

    protected abstract void onTick();


    protected abstract Particle getDefaultParticle();

    public final void tick() {
        if (lifetime == 0 && !shouldRemove) {
            shouldRemove = true;
            onRemove();
            return;
        }
        if (lifetime % 30 == 0) {
            Particle newParticle = getDefaultParticle();

            double x =
                    owningEntity.getHitbox().getMinX() + owningEntity.getHitbox().getWidth() / 2;
            double y =
                    owningEntity.getHitbox().getMinY() + owningEntity.getHitbox().getHeight() / 2;

            double xOffset = Math.random() * 40 - 20;
            double yOffset = Math.random() * 40 - 20;

            xOffset += owningEntity.getHitbox().getWidth() / 2 * Math.signum(xOffset);
            yOffset += owningEntity.getHitbox().getHeight() / 2 * Math.signum(yOffset);

            newParticle.setEntityPosition(x + xOffset, y + yOffset);
            owningEntity.getOwningRoom().addEntity(newParticle);
        }

        lifetime = Math.max(0, lifetime - 1);

        onTick();
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }
}
