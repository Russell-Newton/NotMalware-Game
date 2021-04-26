package whatexe.dungeoncrawler.layout;

import javafx.scene.layout.GridPane;
import whatexe.dungeoncrawler.Difficulty;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.layout.generation.RoomPosition;
import whatexe.dungeoncrawler.layout.generation.RoomPositionProperty;
import whatexe.dungeoncrawler.layout.rooms.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static whatexe.dungeoncrawler.layout.MinimapTile.*;

public class Level {

    private final RoomPositionProperty currentRoomPosition;
    private final Map<RoomPosition, Room> maze;
    private final Difficulty difficulty;
    private final int depth;
    private GridPane minimap;
    private Sprite[][] minimapSprites;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public Level(Difficulty difficulty) {
        this(difficulty, new RoomPosition(0, 0));
    }

    public Level(Difficulty difficulty, int depth) {
        this(difficulty, new RoomPosition(0, 0), depth);
    }

    public Level(Difficulty difficulty, RoomPosition startingRoomPosition) {
        this(difficulty, startingRoomPosition, 0);
    }

    public Level(Difficulty difficulty, RoomPosition startingRoomPosition, int depth) {
        currentRoomPosition = new RoomPositionProperty(startingRoomPosition);
        maze = new HashMap<>();
        this.difficulty = difficulty;
        this.depth = depth;
        minX = 1;
        maxX = -1;
        minY = 1;
        maxY = -1;
        currentRoomPosition.addListener((__, oldValue, newValue) -> {
            Sprite oldPlayerSprite =
                    minimapSprites[oldValue.getX() - minX][oldValue.getY() - minY];
            Sprite oldRegularSprite =
                    minimapSprites[newValue.getX() - minX][newValue.getY() - minY];
            Room oldPlayerRoom = maze.get(oldValue);
            Sprite newRegularSprite = getAppropriateTile(oldPlayerRoom).getSprite();
            Sprite newPlayerSprite = PLAYER.getSprite();

            minimapSprites[oldValue.getX() - minX][oldValue.getY() - minY] = newRegularSprite;
            minimapSprites[newValue.getX() - minX][newValue.getY() - minY] = newPlayerSprite;
            minimap.getChildren().removeAll(oldPlayerSprite, oldRegularSprite);
            minimap.add(newPlayerSprite,
                        newValue.getX() - minX,
                        newValue.getY() - minY);
            minimap.add(newRegularSprite,
                        oldValue.getX() - minX,
                        oldValue.getY() - minY);
        });
    }

    public Room getCurrentRoom() {
        return maze.get(currentRoomPosition.get());
    }

    public Room moveUp() {
        currentRoomPosition.set(currentRoomPosition.get().getAbove());
        return getCurrentRoom();
    }

    public Room moveDown() {
        currentRoomPosition.set(currentRoomPosition.get().getBelow());
        return getCurrentRoom();
    }

    public Room moveLeft() {
        currentRoomPosition.set(currentRoomPosition.get().getLeft());
        return getCurrentRoom();
    }

    public Room moveRight() {
        currentRoomPosition.set(currentRoomPosition.get().getRight());
        return getCurrentRoom();
    }

    public Room move(Direction direction) {
        currentRoomPosition.set(currentRoomPosition.get().getNeighbor(direction));
        return getCurrentRoom();
    }

    public Map<RoomPosition, Room> getMaze() {
        return maze;
    }

    public RoomPositionProperty currentRoomPositionProperty() {
        return currentRoomPosition;
    }

    private void initBounds() {
        for (RoomPosition position : maze.keySet()) {
            minX = Math.min(minX, position.getX());
            minY = Math.min(minY, position.getY());
            maxX = Math.max(maxX, position.getX());
            maxY = Math.max(maxY, position.getY());
        }
    }

    public int getHeight() {
        if (minX == 1) {
            initBounds();
        }
        return maxY - minY + 1;
    }

    public int getWidth() {
        if (minX == 1) {
            initBounds();
        }
        return maxX - minX + 1;
    }

    public GridPane getMinimap() {
        if (minimap == null) {
            minimap = new GridPane();
            minimapSprites = new Sprite[getWidth()][getHeight()];
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    MinimapTile toAdd;
                    RoomPosition currentPosition = new RoomPosition(x + minX, y + minY);
                    Room roomAtPos = maze.get(currentPosition);
                    toAdd = getAppropriateTile(roomAtPos);
                    if (currentPosition.equals(currentRoomPosition.get())) {
                        toAdd = PLAYER;
                    }
                    Sprite addSprite = toAdd.getSprite();
                    minimap.add(addSprite, x, y);
                    minimapSprites[x][y] = addSprite;
                }
            }
        }

        return minimap;
    }

    public String mazeToString() {
        getHeight();
        String[][] mazeRep = new String[maxY - minY + 1][maxX - minX + 1];
        for (Map.Entry<RoomPosition, Room> entry : maze.entrySet()) {
            RoomPosition position = entry.getKey();
            Room room = entry.getValue();
            String toAdd;
            if (position.equals(currentRoomPosition.get())) {
                toAdd = "P";
            } else if (StartingRoom.class.equals(room.getClass())) {
                toAdd = "O";
            } else if (SimpleRoom.class.equals(room.getClass())) {
                toAdd = "*";
            } else {
                toAdd = room.getClass().getSimpleName().charAt(0) + "";
            }
            mazeRep[position.getY() - minY][position.getX() - minX] = toAdd;
        }

        StringBuilder builder = new StringBuilder();
        for (String[] row : mazeRep) {
            for (String room : row) {
                builder.append(Objects.requireNonNullElse(room, " "));
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public Room getNeighborRoom(Direction direction) {
        return getMaze().get(currentRoomPosition.get().getNeighbor(direction));
    }

    public RoomPosition getRoomPosition() {
        return currentRoomPosition.get();
    }

    public int getDepth() {
        return depth;
    }

    public int getMaxDanger() {
        return difficulty.getMaxDanger(depth);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
