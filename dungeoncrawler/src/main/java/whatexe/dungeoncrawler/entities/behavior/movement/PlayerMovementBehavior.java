package whatexe.dungeoncrawler.entities.behavior.movement;

import whatexe.dungeoncrawler.entities.motionsupport.Vector;
import whatexe.dungeoncrawler.entities.player.Player;

public class PlayerMovementBehavior extends MovementBehavior<Player> {

    private boolean goingUp;
    private boolean goingLeft;
    private boolean goingRight;
    private boolean goingDown;

    public PlayerMovementBehavior(Player owningEntity) {
        super(owningEntity);
    }

    @Override
    public Vector getMovement() {
        int dx = 0;
        int dy = 0;
        if (goingUp) {
            dy--;
        }
        if (goingDown) {
            dy++;
        }
        if (goingRight) {
            dx++;
        }
        if (goingLeft) {
            dx--;
        }

        Vector movement = new Vector(dx, dy);
        if (movement.isZero()) {
            return movement;
        }

        return movement.unit()
                       .scaledBy(owningEntity.getEntityStatistics().getModifiedSpeed());
    }

    public boolean isGoingUp() {
        return goingUp;
    }

    public void setGoingUp(boolean goingUp) {
        this.goingUp = goingUp;
    }

    public boolean isGoingLeft() {
        return goingLeft;
    }

    public void setGoingLeft(boolean goingLeft) {
        this.goingLeft = goingLeft;
    }

    public boolean isGoingRight() {
        return goingRight;
    }

    public void setGoingRight(boolean goingRight) {
        this.goingRight = goingRight;
    }

    public boolean isGoingDown() {
        return goingDown;
    }

    public void setGoingDown(boolean goingDown) {
        this.goingDown = goingDown;
    }

}
