package whatexe.dungeoncrawler.entities.behavior.attack.specials;

import javafx.beans.property.SimpleIntegerProperty;
import whatexe.dungeoncrawler.entities.Entity;
import whatexe.dungeoncrawler.entities.behavior.attack.AttackBehavior;
import whatexe.dungeoncrawler.entities.player.Player;

import java.util.List;

public abstract class SpecialAttackBehavior extends AttackBehavior<Player> {

    private final SimpleIntegerProperty maxCharge;
    protected final SimpleIntegerProperty charge;

    public SpecialAttackBehavior(Player owningEntity, int maxCharge) {
        super(owningEntity);

        this.maxCharge = new SimpleIntegerProperty(maxCharge);
        charge = new SimpleIntegerProperty(getMaxCharge());
    }

    @Override
    public final List<? extends Entity> attack() {
        if (getCharge() == getMaxCharge()) {
            setCharge(0);
            return getDefaultAttackEntities();
        }
        return List.of();
    }

    public int getMaxCharge() {
        return maxCharge.get();
    }

    public void setMaxCharge(int maxCharge) {
        this.maxCharge.set(maxCharge);
    }

    public int getCharge() {
        return charge.get();
    }

    public SimpleIntegerProperty chargeProperty() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge.set(charge);
    }

    public SimpleIntegerProperty maxChargeProperty() {
        return maxCharge;
    }

    public void recharge() {
        setCharge(Math.min(getCharge() + 1, getMaxCharge()));
    }
}
