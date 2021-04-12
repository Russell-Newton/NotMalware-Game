package whatexe.dungeoncrawler.entities.projectiles;

import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.entities.behavior.EntityStatistics;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.layout.rooms.Room;

import java.util.ArrayList;
import java.util.List;

public class EnemyProjectile extends Projectile {
    public EnemyProjectile(Room owningRoom, int speed, Vector direction, int damage,
                           double radius) {
        this(owningRoom, speed, direction, damage, -1, radius);
    }

    public EnemyProjectile(Room owningRoom, int speed, Vector direction, int damage, int lifetime,
                           double radius) {
        super(owningRoom, speed, direction, damage, lifetime, radius);
    }

    public EnemyProjectile(Sprite displayNode, Room owningRoom, Vector direction, int lifetime,
                           EntityStatistics entityStatistics) {
        super(displayNode, owningRoom, direction, lifetime, entityStatistics);
    }

    @Override
    protected List<? extends Entity> getCollisionTargets() {
        ArrayList<Entity> list = new ArrayList<>(owningRoom.getFriends());
        if (owningRoom.getPlayer() != null) {
            list.add(owningRoom.getPlayer());
        }
        return list;
    }

    @Override
    protected List<? extends Entity> getOverlapTargets() {
        return getCollisionTargets();
    }
}
