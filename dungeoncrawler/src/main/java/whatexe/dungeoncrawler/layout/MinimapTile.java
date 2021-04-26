package whatexe.dungeoncrawler.layout;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import whatexe.dungeoncrawler.entities.Sprite;
import whatexe.dungeoncrawler.layout.rooms.BossRoom;
import whatexe.dungeoncrawler.layout.rooms.ChallengeRoom;
import whatexe.dungeoncrawler.layout.rooms.Room;
import whatexe.dungeoncrawler.layout.rooms.ShopRoom;

public enum MinimapTile {
    EMPTY {
        @Override
        public Sprite getSprite() {
            return new Sprite(SHEET, new Rectangle2D(0, 0, 24, 24));
        }
    },
    PLAYER {
        @Override
        public Sprite getSprite() {
            return new Sprite(SHEET, new Rectangle2D(24, 0, 24, 24));
        }
    },
    BOSS {
        @Override
        public Sprite getSprite() {
            return new Sprite(SHEET, new Rectangle2D(48, 0, 24, 24));
        }
    },
    SHOP {
        @Override
        public Sprite getSprite() {
            return new Sprite(SHEET, new Rectangle2D(0, 24, 24, 24));
        }
    },
    CHALLENGE {
        @Override
        public Sprite getSprite() {
            return new Sprite(SHEET, new Rectangle2D(24, 24, 24, 24));
        }
    },
    NULL {
        @Override
        public Sprite getSprite() {
            return new Sprite(SHEET, new Rectangle2D(48, 24, 24, 24));
        }
    };

    private static final Image SHEET = new Image(MinimapTile.class.getResourceAsStream(
            "MinimapSheet.png"));

    public static MinimapTile getAppropriateTile(Room room) {
        if (room == null) {
            return NULL;
        } else if (room instanceof BossRoom) {
            return BOSS;
        } else if (room instanceof ShopRoom) {
            return SHOP;
        } else if (room instanceof ChallengeRoom) {
            return CHALLENGE;
        } else {
            return EMPTY;
        }
    }

    public abstract Sprite getSprite();

}
