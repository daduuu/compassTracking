package co.daviddu.minecraft.commands;

import co.daviddu.minecraft.compassTracking;
import com.mysql.jdbc.Buffer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class resetCompass implements CommandExecutor {
    private compassTracking plugin;

    public resetCompass(compassTracking plugin) {
        this.plugin = plugin;
        plugin.getCommand("resetCompass").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Only players can run this command");
            return true;
        }
        Player p = (Player) sender;
        if(command.getName().equalsIgnoreCase("resetcompass")){
            Bukkit.getServer().getScheduler().cancelTasks(this.plugin);
            p.setCompassTarget(Bukkit.getWorld("world").getSpawnLocation());
        }
        return true;
    }
}
