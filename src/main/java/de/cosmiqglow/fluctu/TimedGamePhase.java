package de.cosmiqglow.fluctu;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class TimedGamePhase extends GamePhase {

    private final int initialTicks;
    private boolean shouldTick = false;
    private int currentTicks;

    public TimedGamePhase(JavaPlugin plugin, int initialTicks) {
        super(plugin);
        Validate.isTrue(initialTicks > 0);
        this.initialTicks = initialTicks;
        this.currentTicks = initialTicks;
    }

    @Override
    public final void update() {
        super.update();
        if (shouldTick) {
            currentTicks--;
        }
    }

    @Override
    public void reset() {
        super.reset();
        currentTicks = initialTicks;
    }

    protected int currentTicks() {
        return currentTicks;
    }

    protected void resetTicks() {
        currentTicks = initialTicks;
    }

    protected void enableTicking() {
        shouldTick = true;
    }

    protected void disableTicking() {
        shouldTick = false;
    }

    protected void setCurrentTicks(int ticks) {
        Validate.isTrue(ticks > 0);
        currentTicks = ticks;
    }

    protected int initialTicks() {
        return initialTicks;
    }

    @Override
    protected boolean isReadyToEnd() {
        return hasEnded() || currentTicks == 0;
    }

}
