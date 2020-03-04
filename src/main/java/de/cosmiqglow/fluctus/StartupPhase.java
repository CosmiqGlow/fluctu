package de.cosmiqglow.fluctus;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class StartupPhase extends TimedGamePhase {

    private final int minimumPlayers;
    private final ImmutableSet<Integer> counters;

    public StartupPhase(JavaPlugin plugin, int initialTicks, int minimumPlayers) {
        this(plugin, initialTicks, minimumPlayers, ImmutableSet.of(initialTicks, (initialTicks / 2), 3, 2, 1));
    }

    public StartupPhase(JavaPlugin plugin, int initialTicks, int minimumPlayers, ImmutableSet<Integer> counters) {
        super(plugin, initialTicks);
        Validate.isTrue(minimumPlayers > 0);
        Validate.notEmpty(counters);
        this.minimumPlayers = minimumPlayers;
        this.counters = counters;
    }

    @EventHandler
    public final void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getPlayers().size() >= minimumPlayers) {
            enableTicking();
            event.setJoinMessage("§a» §7" + player.getName() + " (§a" + getPlayers().size() + "§7/§e" + Bukkit.getMaxPlayers() + "§7)");
        } else {
            event.setJoinMessage("§a» §7" + player.getName() + " (§c" + getPlayers().size() + "§7/§e" + Bukkit.getMaxPlayers() + "§7)");
        }

        onPlayerJoin(event);
    }

    public abstract void onPlayerJoin(PlayerJoinEvent event);

    @EventHandler
    public final void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if ((getPlayers().size() - 1) < minimumPlayers) {
            disableTicking();
            resetTicks();
            event.setQuitMessage("§c« §7" + player.getName() + " (§c" + (getPlayers().size() - 1) + "§7/§e" + Bukkit.getMaxPlayers() + "§7)");
        } else {
            event.setQuitMessage("§c« §7" + player.getName() + " (§a" + (getPlayers().size() - 1) + "§7/§e" + Bukkit.getMaxPlayers() + "§7)");
        }
    }

    @EventHandler
    public final void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if ((getPlayers().size() - 1) < minimumPlayers) {
            disableTicking();
            resetTicks();
            event.setLeaveMessage("§c« §7" + player.getName() + " (§c" + (getPlayers().size() - 1) + "§7/§e" + Bukkit.getMaxPlayers() + "§7)");
        } else {
            event.setLeaveMessage("§c« §7" + player.getName() + " (§a" + (getPlayers().size() - 1) + "§7/§e" + Bukkit.getMaxPlayers() + "§7)");
        }
    }

    @Override
    protected void onStart() {
        if (getPlayers().size() >= minimumPlayers) {
            setCurrentTicks(initialTicks() / 2);
            enableTicking();
        }
    }

    @Override
    protected void onUpdate() {
        if (counters.contains(currentTicks())) {
            broadcast("§7Das Spiel beginnt in §e" + currentTicks() + " §7Sekunden.");
        }
    }

    @Override
    protected boolean isReadyToEnd() {
        return super.isReadyToEnd() && getPlayers().size() >= minimumPlayers;
    }

}
