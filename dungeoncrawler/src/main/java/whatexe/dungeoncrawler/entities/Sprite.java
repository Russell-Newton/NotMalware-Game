package whatexe.dungeoncrawler.entities;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Paint;

public class Sprite extends Canvas {

    private final SimpleObjectProperty<Image> spriteSheet;
    private final SimpleObjectProperty<Rectangle2D> viewport;
    private boolean isPrimitive = false;

    public Sprite() {
        this.spriteSheet = new SimpleObjectProperty<>();
        this.viewport = new SimpleObjectProperty<>();

        this.spriteSheet.addListener((__, oldValue, newValue) -> {
            if (getViewport() == null) {
                setViewport(new Rectangle2D(0, 0, newValue.getWidth(), newValue.getHeight()));
            }
            updateSprite();
        });
        this.viewport.addListener((__, oldValue, newValue) -> {
            setWidth(newValue.getWidth());
            setHeight(newValue.getHeight());

            updateSprite();
        });
    }

    public Sprite(Image spriteSheet) {
        this(spriteSheet,
             new Rectangle2D(0, 0, spriteSheet.getWidth(), spriteSheet.getHeight()));
    }

    public Sprite(Image spriteSheet, Rectangle2D viewport) {
        super(viewport.getWidth(), viewport.getHeight());
        this.spriteSheet = new SimpleObjectProperty<>(spriteSheet);
        this.viewport = new SimpleObjectProperty<>(viewport);

        this.spriteSheet.addListener((__, oldValue, newValue) -> {
            if (getViewport() == null) {
                setViewport(new Rectangle2D(0, 0, newValue.getWidth(), newValue.getHeight()));
            }
            updateSprite();
        });
        this.viewport.addListener((__, oldValue, newValue) -> {
            setWidth(newValue.getWidth());
            setHeight(newValue.getHeight());

            applyCss();

            updateSprite();
        });

        updateSprite();
    }

    public static Image getImageFromSpriteSheet(Image sheet, Rectangle2D viewport) {
        return getImageFromSpriteSheet(sheet,
                                       (int) viewport.getMinX(),
                                       (int) viewport.getMinY(),
                                       (int) viewport.getWidth(),
                                       (int) viewport.getHeight());
    }

    public static Image getImageFromSpriteSheet(Image sheet, int x, int y, int width,
                                                int height) {
        WritableImage image = new WritableImage(width, height);
        PixelReader pr = sheet.getPixelReader();
        PixelWriter pw = image.getPixelWriter();

        for (int readY = 0; readY < height; readY++) {
            for (int readX = 0; readX < width; readX++) {
                pw.setColor(readX, readY, pr.getColor(readX + x, readY + y));
            }
        }

        return image;
    }

    public static Sprite asRectangle(int width, int height, Paint fill) {
        Sprite sprite = new Sprite();
        sprite.setWidth(width);
        sprite.setHeight(height);
        GraphicsContext gc = sprite.getGraphicsContext2D();
        gc.setFill(fill);
        gc.fillRect(0, 0, width, height);

        // WritableImage writableImage = new WritableImage(width, height);
        // sprite.snapshot(null, writableImage);
        //
        // sprite.setSpriteSheet(writableImage);
        sprite.isPrimitive = true;

        return sprite;
    }

    public static Sprite asCircle(int radius, Paint fill) {
        Sprite sprite = new Sprite();
        sprite.setWidth(2 * radius);
        sprite.setHeight(2 * radius);

        GraphicsContext gc = sprite.getGraphicsContext2D();
        gc.setFill(fill);
        gc.fillOval(0, 0, 2 * radius, 2 * radius);

        // WritableImage writableImage = new WritableImage(2 * radius, 2 * radius);
        //
        // SnapshotParameters parameters = new SnapshotParameters();
        // parameters.setFill(Color.TRANSPARENT);
        // sprite.snapshot(parameters, writableImage);
        //
        // sprite.setSpriteSheet(writableImage);
        sprite.isPrimitive = true;

        return sprite;
    }

    private void updateSprite() {
        if (!isPrimitive) {
            GraphicsContext gc = getGraphicsContext2D();
            gc.setImageSmoothing(false);
            gc.clearRect(0, 0, getViewport().getWidth(), getViewport().getHeight());
            gc.drawImage(getImageFromSpriteSheet(getSpriteSheet(), getViewport()),
                         0, 0);
        }
    }

    public Image getSpriteSheet() {
        return spriteSheet.get();
    }

    public void setSpriteSheet(Image spriteSheet) {
        this.spriteSheet.set(spriteSheet);
    }

    public SimpleObjectProperty<Image> spriteSheetProperty() {
        return spriteSheet;
    }

    public Rectangle2D getViewport() {
        return viewport.get();
    }

    public void setViewport(Rectangle2D viewport) {
        this.viewport.set(viewport);
    }

    public SimpleObjectProperty<Rectangle2D> viewportProperty() {
        return viewport;
    }
}
