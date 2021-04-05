package whatexe.dungeoncrawler.entities.projectiles;

import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.List;

public class PlayerProjectile extends Projectile {

    public PlayerProjectile(Sprite displayNode, Room owningRoom, Vector direction, int lifetime,
                            EntityStatistics statistics) {
        super(displayNode, owningRoom, direction, lifetime, statistics);
    }

    public PlayerProjectile(Room owningRoom, double speed, Vector direction, int damage,
                            double radius) {
        super(owningRoom, speed, direction, damage, radius);
    }

    public PlayerProjectile(Room owningRoom, double speed, Vector direction, int damage,
                            int lifetime, double radius) {
        super(owningRoom, speed, direction, damage, lifetime, radius);
    }

    @Override
    protected List<? extends Entity> getCollisionTargets() {
        return owningRoom.getEnemies();
    }

    @Override
    protected List<? extends Entity> getOverlapTargets() {
        return getCollisionTargets();
    }
}
