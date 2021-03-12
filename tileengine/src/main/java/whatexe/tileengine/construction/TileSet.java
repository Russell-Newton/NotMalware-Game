package whatexe.tileengine.construction;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Function;

public class TileSet {
    // These flags indicate tile transformations. Any transformation can be attained with these
    // three sets. H_RCCW is handled first, then VERTICALLY, then HORIZONTALLY
    private static final long FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
    private static final long FLIPPED_VERTICALLY_FLAG = 0x40000000;
    // Flipped horizontally, rotated ccw
    private static final long FLIPPED_H_RCCW_FLAG = 0x20000000;

    private final BufferedImage setImage;
    private final List<TileImage> tiles;
    private final whatexe.tileengine.fromtmx.TileSet fromTmx;

    public TileSet(whatexe.tileengine.fromtmx.TileSet fromTmx, Function<String, URL> resourceLoader)
            throws IOException {
        URL src = resourceLoader.apply(fromTmx.getImage().getSource());
        setImage = ImageIO.read(src);
        this.fromTmx = fromTmx;

        TileCutter cutter = new TileCutter(setImage);
        tiles = cutter.cut(fromTmx.getTileWidth(), fromTmx.getTileHeight(), fromTmx.getSpacing(),
                           fromTmx.getMargin());
    }

    public TileImage getTileImage(long gId) {
        boolean hFlip = (gId & FLIPPED_HORIZONTALLY_FLAG) != 0;
        boolean vFlip = (gId & FLIPPED_VERTICALLY_FLAG) != 0;
        boolean hRCcw = (gId & FLIPPED_H_RCCW_FLAG) != 0;

        gId &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_H_RCCW_FLAG);

        if (!(hFlip | vFlip | hRCcw)) {
            return tiles.get((int) gId - fromTmx.getFirstGid());
        }

        TileImage tile = tiles.get((int) gId - fromTmx.getFirstGid());
        BufferedImage transformedImage = tile.getImage();
        AffineTransform transformation = new AffineTransform();

        if (hFlip) {
            transformation.concatenate(AffineTransform.getScaleInstance(-1, 1));
            transformation.concatenate(
                    AffineTransform.getTranslateInstance(-transformedImage.getWidth(), 0));
        }
        if (vFlip) {
            transformation.concatenate(AffineTransform.getScaleInstance(1, -1));
            transformation.concatenate(
                    AffineTransform.getTranslateInstance(0, -transformedImage.getHeight()));
        }
        if (hRCcw) {
            transformation.concatenate(AffineTransform.getScaleInstance(-1, 1));
            transformation.concatenate(
                    AffineTransform.getTranslateInstance(-transformedImage.getWidth(), 0));
            transformation.concatenate(
                    AffineTransform.getRotateInstance(Math.PI / 2,
                                                      transformedImage.getWidth() / 2.,
                                                      transformedImage.getHeight() / 2.));
        }

        AffineTransformOp transformOp = new AffineTransformOp(
                transformation, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        transformedImage = transformOp.filter(transformedImage, null);

        return new TileImage(transformedImage, tile.getX(), tile.getY());
    }

    public boolean isGIdInRange(long gId) {
        gId &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_H_RCCW_FLAG);
        return gId - fromTmx.getFirstGid() < fromTmx.getTileCount();
    }
}
