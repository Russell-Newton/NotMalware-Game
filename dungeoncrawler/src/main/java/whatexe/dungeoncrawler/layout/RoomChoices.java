package whatexe.dungeoncrawler.layout;

import whatexe.dungeoncrawler.layout.rooms.*;
import whatexe.tileengine.TiledMap;

public enum RoomChoices {
    STANDARD {
        private static final String DEFAULT_MAP_FILE = "NewTemplate.tmx";

        @Override
        public Room getStartingRoom(Level owningLevel) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(DEFAULT_MAP_FILE),
                                                     getClass()::getResource);

                return new StartingRoom(fromTiledMap, owningLevel);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Room getBossRoom(Level owningLevel, Direction exitDirection) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(
                        "Boss.tmx"), getClass()::getResource);

                return new BossRoom(fromTiledMap, owningLevel, exitDirection);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Room getOrdinaryRoom(Level owningLevel, Direction... exitDirections) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(DEFAULT_MAP_FILE),
                                                     getClass()::getResource);

                return new SimpleRoom(fromTiledMap, owningLevel, exitDirections);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Room getShopRoom(Level owningLevel, Direction... exitDirections) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(
                        "Shop.tmx"), getClass()::getResource);

                return new ShopRoom(fromTiledMap, owningLevel, exitDirections);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Room getChallengeRoom(Level owningLevel, Direction... exitDirections) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(
                        "Challenge.tmx"), getClass()::getResource);

                return new ChallengeRoom(fromTiledMap, owningLevel, exitDirections);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    },
    CALIBRATION {
        private static final String DEFAULT_MAP_FILE = "Calibration.tmx";

        @Override
        public Room getStartingRoom(Level owningLevel) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(DEFAULT_MAP_FILE),
                                                     getClass()::getResource);

                return new StartingRoom(fromTiledMap, owningLevel);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Room getBossRoom(Level owningLevel, Direction exitDirection) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(DEFAULT_MAP_FILE),
                                                     getClass()::getResource);

                return new BossRoom(fromTiledMap, owningLevel, exitDirection);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Room getOrdinaryRoom(Level owningLevel, Direction... exitDirections) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(DEFAULT_MAP_FILE),
                                                     getClass()::getResource);

                return new SimpleRoom(fromTiledMap, owningLevel, exitDirections);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Room getShopRoom(Level owningLevel, Direction... exitDirections) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(DEFAULT_MAP_FILE),
                                                     getClass()::getResource);

                return new ShopRoom(fromTiledMap, owningLevel, exitDirections);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Room getChallengeRoom(Level owningLevel, Direction... exitDirections) {
            try {
                TiledMap fromTiledMap = new TiledMap(getClass().getResource(DEFAULT_MAP_FILE),
                                                     getClass()::getResource);

                return new ChallengeRoom(fromTiledMap, owningLevel, exitDirections);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    public abstract Room getStartingRoom(Level owningLevel);

    public abstract Room getBossRoom(Level owningLevel, Direction exitDirection);

    public abstract Room getOrdinaryRoom(Level owningLevel, Direction... exitDirections);

    public abstract Room getShopRoom(Level owningLevel, Direction... exitDirections);

    public abstract Room getChallengeRoom(Level owningLevel, Direction... exitDirections);
}
