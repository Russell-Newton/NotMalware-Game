package whatexe.dungeoncrawler.entities.projectiles;

import javafx.scene.paint.Color;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.behavior.attack.DoNothingAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.collision.ProjectileCollisionBehavior;
import whatexe.dungeoncrawler.entities.behavior.death.RemoveOnDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.SimpleMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.ProjectileOverlapBehavior;
import whatexe.dungeoncrawler.entities.behavior.BehaviorSet;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.List;

public abstract class Projectile extends Entity {

    protected int lifetime;

    public Projectile(Sprite displayNode, Room owningRoom, Vector direction, int lifetime,
                      EntityStatistics statistics) {
        super(displayNode, owningRoom, statistics);
        this.lifetime = lifetime;
        canTick = true;

        behaviorSet.set(new BehaviorSet(
                new DoNothingAttackBehavior(this),
                new SimpleMovementBehavior(this, direction),
                new RemoveOnDeathBehavior(this),
                new ProjectileOverlapBehavior(this, this::getOverlapTargets),
                new ProjectileCollisionBehavior(this, this::getCollisionTargets)
        ));
    }

    public Projectile(Room owningRoom, double speed, Vector direction, int damage, int lifetime,
                      double radius) {
        super(Sprite.asCircle((int) radius, Color.BLACK), owningRoom);
        this.lifetime = lifetime;
        canTick = true;

        entityStatistics.setSpeed(speed);
        entityStatistics.setAttackDamage(damage);

        behaviorSet.set(new BehaviorSet(
                new DoNothingAttackBehavior(this),
                new SimpleMovementBehavior(this, direction),
                new RemoveOnDeathBehavior(this),
                new ProjectileOverlapBehavior(this, this::getOverlapTargets),
                new ProjectileCollisionBehavior(this, this::getCollisionTargets)
        ));
    }

    public Projectile(Room owningRoom, double speed, Vector direction, int damage, double radius) {
        this(owningRoom, speed, direction, damage, -1, radius);
    }

    protected abstract List<? extends Entity> getCollisionTargets();

    protected abstract List<? extends Entity> getOverlapTargets();

    public int getLifetime() {
        return lifetime;
    }

    @Override
    public void tick() {
        super.tick();
        if (lifetime > 0) {
            lifetime--;
        }
        if (lifetime == 0) {
            setHealth(-1);
        }
    }
}
