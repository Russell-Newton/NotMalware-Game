package whatexe.tileengine.construction;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileCutter {

    private BufferedImage setImage;
    private int nextX;
    private int nextY;

    public TileCutter(BufferedImage setImage) {
        this.setImage = setImage;
    }

    public List<TileImage> cut(int width, int height, int spacing, int margin) {
        List<TileImage> out = new ArrayList<>();

        nextX = margin;
        nextY = margin;
        int currX = nextX;
        int currY = nextY;

        for (BufferedImage tileImage = getNextTile(width, height, spacing, margin);
             tileImage != null;
             tileImage = getNextTile(width, height, spacing, margin)) {
            out.add(new TileImage(tileImage, currX, currY));
            currX = nextX;
            currY = nextY;
        }

        return out;
    }

    private BufferedImage getNextTile(int width, int height, int spacing, int margin) {
        if (nextY + height + margin <= setImage.getHeight()) {
            BufferedImage tileImage = setImage.getSubimage(nextX, nextY, width, height);

            nextX += width + spacing;

            if (nextX + width + margin > setImage.getWidth()) {
                nextX = margin;
                nextY += height + spacing;
            }

            return tileImage;
        }
        return null;
    }
}
