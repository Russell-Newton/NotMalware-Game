package whatexe.dungeoncrawler.layout;

import whatexe.dungeoncrawler.layout.generation.LevelGenerator;

public enum Direction {
    UP {
        @Override
        public Direction getOpposite() {
            return DOWN;
        }

        @Override
        public Direction getCW() {
            return RIGHT;
        }

        @Override
        public Direction getCCW() {
            return LEFT;
        }
    },
    LEFT {
        @Override
        public Direction getOpposite() {
            return RIGHT;
        }

        @Override
        public Direction getCW() {
            return UP;
        }

        @Override
        public Direction getCCW() {
            return DOWN;
        }
    },
    RIGHT {
        @Override
        public Direction getOpposite() {
            return LEFT;
        }

        @Override
        public Direction getCW() {
            return DOWN;
        }

        @Override
        public Direction getCCW() {
            return UP;
        }
    },
    DOWN {
        @Override
        public Direction getOpposite() {
            return UP;
        }

        @Override
        public Direction getCW() {
            return LEFT;
        }

        @Override
        public Direction getCCW() {
            return RIGHT;
        }
    };

    /**
     * Useful for {@link LevelGenerator}s.
     *
     * @return the Direction opposite to this Direction.
     */
    public abstract Direction getOpposite();

    /**
     * Useful for {@link LevelGenerator}s.
     *
     * @return the Direction clockwise from this Direction.
     */
    public abstract Direction getCW();

    /**
     * Useful for {@link LevelGenerator}s.
     *
     * @return the Direction counter-clockwise from this Direction.
     */
    public abstract Direction getCCW();

}
