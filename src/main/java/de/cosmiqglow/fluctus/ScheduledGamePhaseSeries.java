package de.cosmiqglow.fluctus;

import de.cosmiqglow.fluctus.state.StateSeries;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class ScheduledGamePhaseSeries extends StateSeries {

    private final Plugin plugin;
    private final long interval;
    private BukkitTask scheduledTask;

    public ScheduledGamePhaseSeries(Plugin plugin) {
        this(plugin, 20);
    }

    public ScheduledGamePhaseSeries(Plugin plugin, long interval) {
        this.plugin = plugin;
        this.interval = interval;
    }

    @Override
    protected final void onStart() {
        super.onStart();
        scheduledTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this::update, 0L, interval);
    }

    @Override
    protected final void onEnd() {
        super.onEnd();
        scheduledTask.cancel();
        scheduledTask = null;
    }

}
