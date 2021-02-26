package whatexe.tileengine.construction;

import java.awt.image.BufferedImage;

public class TileImage {

    private final BufferedImage image;
    private final int x;
    private final int y;

    public TileImage(BufferedImage image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
