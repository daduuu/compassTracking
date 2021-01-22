package co.daviddu.minecraft;

import co.daviddu.minecraft.commands.resetCompass;
import co.daviddu.minecraft.commands.setCompassPlayer;
import co.daviddu.minecraft.events.onPlayerClick;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class compassTracking extends JavaPlugin {
    @Override
    public void onEnable() {
        new setCompassPlayer(this);
        new resetCompass(this);
        getServer().getPluginManager().registerEvents(new onPlayerClick(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "compassTracking Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "compassTracking Plugin Disabled");
    }
}


