package whatexe.dungeoncrawler.layout.rooms;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import whatexe.dungeoncrawler.entities.EntityChooser;
import whatexe.dungeoncrawler.entities.doors.Door;
import whatexe.dungeoncrawler.entities.doors.SimpleDoor;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.motionsupport.BoundaryRectangle;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.tileengine.MapObject;
import whatexe.tileengine.TiledMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleRoom extends Room {

    private static final int MAX_MONSTERS = 4;

    public SimpleRoom(TiledMap fromTiledMap,
                      Level owningLevel,
                      Direction... exitDirections) {
        super(fromTiledMap, owningLevel, exitDirections);
    }

    @Override
    protected void initDoors(Direction[] directionsToGenerate) {
        List<MapObject> doorMapObject = fromTiledMap.getObjectGroups().get("Doors").getObjects();
        Collections.sort(doorMapObject, new MapObjectComparator());
        List<Direction> toGenerate = Arrays.asList(directionsToGenerate);

        for (Direction direction : Direction.values()) {
            MapObject correspondingDoor = doorMapObject.get(direction.ordinal());

            Rectangle2D hitbox = new Rectangle2D(correspondingDoor.getX(),
                                                 correspondingDoor.getY(),
                                                 correspondingDoor.getWidth(),
                                                 correspondingDoor.getHeight());

            Door door = new SimpleDoor(owningLevel, direction, hitbox, this);
            door.setEntityPosition(correspondingDoor.getX(), correspondingDoor.getY());

            if (toGenerate.contains(direction)) {
                doors.add(door);
            } else {
                boundaryRectangles.add(
                        new BoundaryRectangle(this,
                                              hitbox));
            }
        }

        updateDoorLocks();
    }

    @Override
    protected void initEntities() {
        List<Rectangle> spawningRegions =
                fromTiledMap.getObjectGroups().get("Enemies").getObjects().stream()
                            .map(mapObject -> new Rectangle(
                                    mapObject.getX(),
                                    mapObject.getY(),
                                    mapObject.getWidth(),
                                    mapObject.getHeight()))
                            .collect(Collectors.toList());

        int maxDanger = owningLevel.getMaxDanger();
        int maxEnemies = maxDanger - 2;
        int rollingDanger = 0;

        while (rollingDanger < maxDanger && enemies.size() < maxEnemies) {
            Enemy current = EntityChooser.getInstance().getRandomEnemy(this);
            Vector spawnPosition = getRandomSpawnPosition(spawningRegions);
            enemies.add(current);
            current.setEntityPosition(spawnPosition.get(0),
                                      spawnPosition.get(1));

            rollingDanger += current.getDanger();
        }
    }

    @Override
    protected void handleOnRoomClear() {
        hasVisited = true;
        updateDoorLocks();
    }

    protected Vector getRandomSpawnPosition(List<Rectangle> spawningRegions) {
        Rectangle chosenRegion =
                spawningRegions.get((int) (Math.random() * spawningRegions.size()));

        double x = Math.random() * chosenRegion.getWidth() + chosenRegion.getX();
        double y = Math.random() * chosenRegion.getHeight() + chosenRegion.getY();

        return new Vector(x, y);
    }
}
