package de.cosmiqglow.fluctus;

import de.cosmiqglow.fluctus.state.StateSeries;
import org.bukkit.plugin.java.JavaPlugin;

public class GamePhaseSeries extends StateSeries {

    private final JavaPlugin plugin;

    public GamePhaseSeries(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected JavaPlugin getGame() {
        return plugin;
    }
}
