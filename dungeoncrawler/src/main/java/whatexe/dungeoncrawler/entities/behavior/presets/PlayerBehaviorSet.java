package whatexe.dungeoncrawler.entities.behavior.presets;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import whatexe.dungeoncrawler.MainApp;
import whatexe.dungeoncrawler.entities.behavior.BehaviorSet;
import whatexe.dungeoncrawler.entities.behavior.attack.PlayerAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.attack.specials.SpecialAttackBehavior;
import whatexe.dungeoncrawler.entities.behavior.collision.PlayerCollisionBehavior;
import whatexe.dungeoncrawler.entities.behavior.death.PlayerDeathBehavior;
import whatexe.dungeoncrawler.entities.behavior.movement.PlayerMovementBehavior;
import whatexe.dungeoncrawler.entities.behavior.overlap.DoNothingOverlapBehavior;
import whatexe.dungeoncrawler.entities.player.DebugPlayer;
import whatexe.dungeoncrawler.entities.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static javafx.scene.input.KeyCode.*;

public class PlayerBehaviorSet extends BehaviorSet {
    /**
     * A map of key bindings, mapped to boolean {@link Consumer}s. The Consumers receive true if
     * the key was pressed and false if the key was released.
     */
    private final Map<KeyCode, Consumer<Boolean>> keyBindings;
    private final SimpleObjectProperty<SpecialAttackBehavior> specialAttackBehavior;
    private final Player owner;

    public PlayerBehaviorSet(Player owner, SpecialAttackBehavior specialAttackBehavior) {
        super(new PlayerAttackBehavior(owner),
              new PlayerMovementBehavior(owner),
              new PlayerDeathBehavior(owner),
              new DoNothingOverlapBehavior(owner),
              new PlayerCollisionBehavior(owner));

        this.owner = owner;
        this.specialAttackBehavior = new SimpleObjectProperty<>(specialAttackBehavior);

        keyBindings = new HashMap<>();
    }

    public void initControls() {
        MainApp.addKeyPressHandler(event -> {
            if (!event.isConsumed()) {
                try {
                    keyBindings.get(event.getCode()).accept(true);
                } catch (NullPointerException ignored) {
                }
            }
        });

        MainApp.addKeyReleaseHandler(event -> {
            if (!event.isConsumed()) {
                try {
                    keyBindings.get(event.getCode()).accept(false);
                } catch (NullPointerException ignored) {
                }
            }
        });

        initMovementControls();
    }

    private void initMovementControls() {
        keyBindings.put(W, pressed -> getPlayerMovementBehavior().setGoingUp(pressed));
        keyBindings.put(S, pressed -> getPlayerMovementBehavior().setGoingDown(pressed));
        keyBindings.put(A, pressed -> getPlayerMovementBehavior().setGoingLeft(pressed));
        keyBindings.put(D, pressed -> getPlayerMovementBehavior().setGoingRight(pressed));

        keyBindings.put(UP, pressed -> getPlayerAttackBehavior().setAttackingUp(pressed));
        keyBindings.put(DOWN, pressed -> getPlayerAttackBehavior().setAttackingDown(pressed));
        keyBindings.put(LEFT, pressed -> getPlayerAttackBehavior().setAttackingLeft(pressed));
        keyBindings.put(RIGHT, pressed -> getPlayerAttackBehavior().setAttackingRight(pressed));
        keyBindings.put(SPACE, pressed -> {
            if (owner.getOwningRoom().isTicking()) {
                owner.getOwningRoom()
                     .addEntities(getSpecialAttackBehavior().attack());
            }
        });

        keyBindings.put(E, pressed -> {
            if (!pressed) {
                owner.operateInventory();
            }
        });

        keyBindings.put(ESCAPE, pressed -> {
            if (!pressed) {
                owner.operatePauseMenu();
            }
        });

        if (owner instanceof DebugPlayer) {
            keyBindings.put(K, pressed -> {
                owner.getOwningRoom().getEnemies().clear();
            });
        }
    }


    private PlayerMovementBehavior getPlayerMovementBehavior() {
        return (PlayerMovementBehavior) getMovementBehavior();
    }

    private PlayerAttackBehavior getPlayerAttackBehavior() {
        return (PlayerAttackBehavior) getAttackBehavior();
    }

    public SpecialAttackBehavior getSpecialAttackBehavior() {
        return specialAttackBehavior.get();
    }

    public SimpleObjectProperty<SpecialAttackBehavior> specialAttackBehaviorProperty() {
        return specialAttackBehavior;
    }
}
