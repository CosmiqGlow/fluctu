package de.cosmiqglow.fluctus;

import de.cosmiqglow.fluctus.state.State;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class GamePhase extends State implements Listener {

    private final JavaPlugin plugin;
    private final Set<Listener> listeners = new HashSet<>();
    private final Set<BukkitTask> tasks = new HashSet<>();

    public GamePhase(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final void start() {
        register(this);
    }

    @Override
    public final void end() {
        listeners.forEach(HandlerList::unregisterAll);
        tasks.forEach(task -> {
            if (!task.isCancelled()) {
                task.cancel();
            }
        });

        listeners.clear();
        tasks.clear();
    }

    protected Collection<? extends Player> getPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    protected void broadcast(String message) {
        for (Player player : getPlayers()) {
            player.sendMessage(message);
        }
    }

    protected final void register(Listener listener) {
        listeners.add(listener);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    protected final void registerGlobal(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    protected final void unregisterGlobal(Event event) {
        event.getHandlers().unregister(plugin);
    }

    protected final void runTask(Runnable runnable, long delay) {
        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    protected final void runTaskTimer(Runnable runnable, long delay, long interval) {
        BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, interval);
        tasks.add(task);
    }

    protected final JavaPlugin getPlugin() {
        return plugin;
    }

}
