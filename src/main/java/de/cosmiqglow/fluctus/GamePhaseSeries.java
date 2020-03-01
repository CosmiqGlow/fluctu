package de.cosmiqglow.fluctus;

import de.cosmiqglow.fluctus.state.StatesSeries;
import org.bukkit.plugin.java.JavaPlugin;

public class GamePhaseSeries extends StatesSeries {

    private final JavaPlugin plugin;

    public GamePhaseSeries(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected JavaPlugin getGame() {
        return plugin;
    }
}
