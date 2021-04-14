package whatexe.dungeoncrawler;

public enum Difficulty {
    EASY {
        @Override
        public int getDefaultHealth() {
            return 50;
        }

        @Override
        public int getDefaultMoney() {
            return 15;
        }

        @Override
        public int getMaxDanger(int depth) {
            // maps like [6, 8, 9, 10, 10, 11, ...]
            return (int) (2 * Math.log(depth + 1) / Math.log(2)) + 6;
        }
    }, NORMAL {
        @Override
        public int getDefaultHealth() {
            return 50;
        }

        @Override
        public int getDefaultMoney() {
            return 0;
        }

        @Override
        public int getMaxDanger(int depth) {
            // maps like [8, 10, 11, 12, 12, 13, ...]
            return (int) (2 * Math.log(depth + 1) / Math.log(2)) + 8;
        }
    }, HARD {
        @Override
        public int getDefaultHealth() {
            return 40;
        }

        @Override
        public int getDefaultMoney() {
            return 0;
        }

        @Override
        public int getMaxDanger(int depth) {
            // maps like [10, 11, 13, 13, 14, 15, ...]
            return (int) (3 * Math.log(depth + 2) / Math.log(2)) + 7;
        }
    };

    public abstract int getDefaultHealth();

    public abstract int getDefaultMoney();

    /**
     * Get the max Enemy danger total per room for a given level depth.
     * @param depth the depth of the level to get the danger for (zero-indexed).
     * @return the max Enemy danger for a given room.
     */
    public abstract int getMaxDanger(int depth);
}

