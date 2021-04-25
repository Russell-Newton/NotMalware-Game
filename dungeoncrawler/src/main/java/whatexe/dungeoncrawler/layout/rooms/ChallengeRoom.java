package whatexe.dungeoncrawler.layout.rooms;


import javafx.collections.ListChangeListener;
import javafx.scene.shape.Rectangle;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.EntityChooser;
import whatexe.dungeoncrawler.entities.enemies.Enemy;
import whatexe.dungeoncrawler.entities.items.modifiers.Modifier;
import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.layout.Direction;
import whatexe.dungeoncrawler.layout.Level;
import whatexe.tileengine.TiledMap;

import java.util.List;
import java.util.stream.Collectors;

public class ChallengeRoom extends SimpleRoom {
    private Modifier modifierTrigger;

    public ChallengeRoom(TiledMap fromTiledMap,
                         Level owningLevel,
                         Direction... exitDirections) {
        super(fromTiledMap, owningLevel, exitDirections);

    }

    @Override
    protected void initEntities() {
        modifierTrigger = EntityChooser.getInstance().getRandomModifier(this);
        modifierTrigger.setOwningRoom(this);
        miscEntities.add(modifierTrigger);
        modifierTrigger.setEntityPosition(getBoundsOfImage().getWidth() / 2 - 16,
                                          getBoundsOfImage().getHeight() / 2);


        miscEntities.addListener(new ListChangeListener<Entity>() {
            @Override
            public void onChanged(Change<? extends Entity> c) {
                if (!miscEntities.contains(modifierTrigger)) {
                    miscEntities.removeListener(this);
                    lockAllDoors();
                    spawnEnemies();
                }
            }
        });
    }

    protected void spawnEnemies() {
        List<Rectangle> spawningRegions =
                fromTiledMap.getObjectGroups().get("Enemies").getObjects().stream()
                            .map(mapObject -> new Rectangle(
                                    mapObject.getX(),
                                    mapObject.getY(),
                                    mapObject.getWidth(),
                                    mapObject.getHeight()))
                            .collect(Collectors.toList());
        int maxDanger = (owningLevel.getMaxDanger() * 3);
        int maxEnemies = maxDanger * 10;
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

}
