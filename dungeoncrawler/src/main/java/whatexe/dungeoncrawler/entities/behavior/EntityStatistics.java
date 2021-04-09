package whatexe.dungeoncrawler.entities.behavior;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class EntityStatistics {
    private static final double DEFAULT_SPEED = 1;
    private static final int DEFAULT_ATTACK_DAMAGE = 5;
    private static final int DEFAULT_ATTACK_DELAY = 50;

    private final SimpleIntegerProperty maxHealth;
    private final SimpleDoubleProperty attackDamage;
    private final SimpleDoubleProperty attackDelay;
    private final SimpleDoubleProperty speed;

    private final SimpleDoubleProperty attackDamageModifier;
    private final SimpleDoubleProperty attackSpeedModifier;
    private final SimpleDoubleProperty speedModifier;


    public EntityStatistics(int maxHealth,
                            double attackDamage,
                            double attackDelay,
                            double speed,
                            double attackDamageModifier,
                            double attackSpeedModifier,
                            double speedModifier) {
        this.maxHealth = new SimpleIntegerProperty(maxHealth);
        this.attackDamage = new SimpleDoubleProperty(attackDamage);
        this.attackDelay = new SimpleDoubleProperty(attackDelay);
        this.speed = new SimpleDoubleProperty(speed);
        this.attackDamageModifier = new SimpleDoubleProperty(attackDamageModifier);
        this.attackSpeedModifier = new SimpleDoubleProperty(attackSpeedModifier);
        this.speedModifier = new SimpleDoubleProperty(speedModifier);
    }

    public EntityStatistics(int maxHealth,
                            double attackDamageModifier,
                            double attackSpeedModifier,
                            double speedModifier) {
        this(maxHealth,
             DEFAULT_ATTACK_DAMAGE,
             DEFAULT_ATTACK_DELAY,
             DEFAULT_SPEED,
             attackDamageModifier,
             attackSpeedModifier,
             speedModifier);
    }

    public EntityStatistics(double attackDamageModifier,
                            double attackSpeedModifier,
                            double speedModifier) {
        this(0, attackDamageModifier, attackSpeedModifier, speedModifier);
    }

    public EntityStatistics() {
        this(1, 1, 1);
    }

    /**
     * Copies the values from the other EntityStatistics into this one.
     * @param other The EntityStatistics to copy from.
     * @return this object, for method chaining.
     */
    public EntityStatistics copyFrom(EntityStatistics other) {
        maxHealth.set(other.getMaxHealth());
        attackDamage.set(other.getAttackDamage());
        attackDelay.set(other.getAttackDelay());
        speed.set(other.getSpeed());

        attackDamageModifier.set(other.getAttackDamageModifier());
        attackSpeedModifier.set(other.getAttackSpeedModifier());
        speed.set(other.getSpeedModifier());

        return this;
    }

    public int getMaxHealth() {
        return maxHealth.get();
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth.set(maxHealth);
    }

    public SimpleIntegerProperty maxHealthProperty() {
        return maxHealth;
    }

    public double getAttackDamage() {
        return attackDamage.get();
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage.set(attackDamage);
    }

    public SimpleDoubleProperty attackDamageProperty() {
        return attackDamage;
    }

    public double getAttackDelay() {
        return attackDelay.get();
    }

    public void setAttackDelay(double attackDelay) {
        this.attackDelay.set(attackDelay);
    }

    public SimpleDoubleProperty attackDelayProperty() {
        return attackDelay;
    }

    public double getSpeed() {
        return speed.get();
    }

    public void setSpeed(double speed) {
        this.speed.set(speed);
    }

    public SimpleDoubleProperty speedProperty() {
        return speed;
    }

    public double getAttackDamageModifier() {
        return attackDamageModifier.get();
    }

    public void setAttackDamageModifier(double attackDamageModifier) {
        this.attackDamageModifier.set(attackDamageModifier);
    }

    public SimpleDoubleProperty attackDamageModifierProperty() {
        return attackDamageModifier;
    }

    public double getAttackSpeedModifier() {
        return attackSpeedModifier.get();
    }

    public void setAttackSpeedModifier(double attackSpeedModifier) {
        this.attackSpeedModifier.set(attackSpeedModifier);
    }

    public SimpleDoubleProperty attackSpeedModifierProperty() {
        return attackSpeedModifier;
    }

    public double getSpeedModifier() {
        return speedModifier.get();
    }

    public void setSpeedModifier(double speedModifier) {
        this.speedModifier.set(speedModifier);
    }

    public SimpleDoubleProperty speedModifierProperty() {
        return speedModifier;
    }

    public double getModifiedSpeed() {
        return getSpeedModifier() * getSpeed();
    }

    public double getModifiedAttackDamage() {
        return getAttackDamage() * getAttackDamageModifier();
    }

    public double getModifiedAttackDelay() {
        return getAttackDelay() / getAttackSpeedModifier();
    }
}
