package whatexe.dungeoncrawler.entities;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class AnimatedSprite extends Sprite {

    private final int numFrames;
    private final int columns;
    private final int rows;
    private final double duration;
    private final Timeline animationTimeline;
    private int currentFrame;

    /**
     * Assumes frames read left-right top-bottom.
     *
     * @param spriteSheet the sheet with frames.
     * @param frameWidth  the width of one frame.
     * @param frameHeight the height of one frame.
     * @param numFrames   the number of frames in total.
     * @param columns     the number of frames in the x direction.
     * @param rows        the number of frames in the y direction.
     * @param duration    the duration (in ms) for the animation to play.
     */
    public AnimatedSprite(Image spriteSheet,
                          int frameWidth,
                          int frameHeight,
                          int numFrames,
                          int columns,
                          int rows,
                          double duration) {
        super(spriteSheet, new Rectangle2D(0, 0, frameWidth, frameHeight));

        this.numFrames = numFrames;
        this.columns = columns;
        this.rows = rows;
        this.duration = duration;
        currentFrame = 0;
        animationTimeline = new Timeline(
                new KeyFrame(Duration.millis(timeStep()), "NextFrame", event -> nextFrame()));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
        animationTimeline.play();
    }

    private void nextFrame() {
        currentFrame = (currentFrame + 1) % numFrames;
        int indexX = currentFrame % columns;
        int indexY = currentFrame / columns;
        Rectangle2D newViewport = new Rectangle2D(indexX * getViewport().getWidth(),
                                                  indexY * getViewport().getHeight(),
                                                  getViewport().getWidth(),
                                                  getViewport().getHeight());
        setViewport(newViewport);
    }

    private double timeStep() {
        return duration / numFrames;
    }

    public static AnimatedSprite copyFrom(AnimatedSprite other) {
        AnimatedSprite newSprite = new AnimatedSprite(other.getSpriteSheet(),
                                                      (int) other.getViewport().getWidth(),
                                                      (int) other.getViewport().getHeight(),
                                                      other.numFrames,
                                                      other.columns,
                                                      other.rows,
                                                      other.duration);
        return newSprite;
    }
}
