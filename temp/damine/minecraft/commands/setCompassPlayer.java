package co.daviddu.minecraft.commands;

import co.daviddu.minecraft.compassTracking;
import com.mysql.jdbc.Buffer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class setCompassPlayer implements CommandExecutor {
    private compassTracking plugin;
    private static int task1;

    public setCompassPlayer(compassTracking plugin) {
        this.plugin = plugin;
        plugin.getCommand("setCompassPlayer").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, final String[] args) {

        final BukkitScheduler schedule = Bukkit.getServer().getScheduler();

        if(!(sender instanceof Player)){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Only players can run this command");
            return true;
        }
        final Player p = (Player) sender;
        if(args.length == 0){
            p.sendMessage(ChatColor.RED + "Need to specify a player");
            return true;
        }
        if(args.length > 1){
            p.sendMessage(ChatColor.RED + "Can only specify one player");
            return true;
        }
        if(!(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0])))){
            p.sendMessage(ChatColor.RED + "Invalid Player");
            return true;
        }
        if(command.getName().equalsIgnoreCase("setcompassplayer")){

            p.sendMessage(ChatColor.GREEN + "Compass Now Pointing To " + args[0]);

            Bukkit.getScheduler().cancelTask(task1);

            final Location previousLocation = new Location(Bukkit.getServer().getWorld("world"), 0, 0, 0);

            task1 = schedule.scheduleSyncRepeatingTask(this.plugin, new Runnable(){

                @Override
                public void run(){
                    if((!p.getWorld().getEnvironment().equals(World.Environment.NORMAL)) && !(Bukkit.getPlayer(args[0]).getWorld().getEnvironment().equals(World.Environment.NORMAL))){
                        for (ItemStack item : p.getInventory().getContents()){
                            if(item.getType() == Material.COMPASS){
                                CompassMeta meta = (CompassMeta) item.getItemMeta();
                                Location l = Bukkit.getPlayer(args[0]).getLocation();
                                l.setY(0);
                                l.getBlock().setType(Material.LODESTONE);
                                meta.setLodestone(l);
                                meta.setLodestoneTracked(true);
                                item.setItemMeta(meta);
                            }
                        }
                    }
                    else if(!(p.getWorld().getEnvironment().equals(Bukkit.getPlayer(args[0]).getWorld().getEnvironment()))){


                        if(p.getWorld().getEnvironment().equals(World.Environment.NORMAL)){
                            for (int kk = 0; kk < p.getInventory().getContents().length; kk++){
                                if(p.getInventory().getItem(kk).getType() == Material.COMPASS){
                                /*CompassMeta meta = (CompassMeta) item.getItemMeta();
                                meta.setLodestoneTracked(false);
                                item.setItemMeta(meta);*/
                                    p.getInventory().setItem(kk, new ItemStack(Material.COMPASS, 1));
                                    p.setCompassTarget(Bukkit.getPlayer(args[0]).getLocation());
                                    break;
                                }
                            }
                        }

                        p.setCompassTarget(new Location(p.getWorld(), Math.random() * 1180332121, Math.random() * 23423, Math.random() * 643252045));
                        p.setCompassTarget(new Location(p.getWorld(), Math.random() * -1180332121, Math.random() * 23423, Math.random() * -643252045));

                    }
                    else{
                        for (int kk = 0; kk < p.getInventory().getContents().length; kk++){
                            if(p.getInventory().getItem(kk).getType() == Material.COMPASS){
                                /*CompassMeta meta = (CompassMeta) item.getItemMeta();
                                meta.setLodestoneTracked(false);
                                item.setItemMeta(meta);*/
                                p.getInventory().setItem(kk, new ItemStack(Material.COMPASS, 1));
                                p.setCompassTarget(Bukkit.getPlayer(args[0]).getLocation());
                                break;
                            }
                        }

                    }
                }

            }, 0L, 10L);

        }
        return true;
    }

    public static int getTask1() {
        return task1;
    }
}
