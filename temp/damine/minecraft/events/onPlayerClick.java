package co.daviddu.minecraft.events;

import co.daviddu.minecraft.commands.setCompassPlayer;
import co.daviddu.minecraft.compassTracking;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;


public class onPlayerClick implements Listener {

    private compassTracking plugin;

    public onPlayerClick(compassTracking plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        final Player p = event.getPlayer();
        final Entity entity = getNearestEntityInSight(p, 5);
        if(event.getAction() == Action.RIGHT_CLICK_AIR && entity instanceof Player){
            if(p.getInventory().getItemInMainHand().getType() == Material.COMPASS){
                p.sendMessage(ChatColor.GREEN + "Compass Now Pointing To " + ((Player) entity).getName());
                final BukkitScheduler schedule = Bukkit.getServer().getScheduler();
                Bukkit.getScheduler().cancelTask(setCompassPlayer.getTask1());

                int task1 = schedule.scheduleSyncRepeatingTask(this.plugin, new Runnable(){

                    @Override
                    public void run(){
                        if((!p.getWorld().getEnvironment().equals(World.Environment.NORMAL)) && !(entity.getWorld().getEnvironment().equals(World.Environment.NORMAL))){
                            for (ItemStack item : p.getInventory().getContents()){
                                if(item.getType() == Material.COMPASS){
                                    CompassMeta meta = (CompassMeta) item.getItemMeta();
                                    Location l = entity.getLocation();
                                    l.setY(0);
                                    l.getBlock().setType(Material.LODESTONE);
                                    meta.setLodestone(l);
                                    meta.setLodestoneTracked(true);
                                    item.setItemMeta(meta);
                                }
                            }
                        }
                        else if(!(p.getWorld().getEnvironment().equals(entity.getWorld().getEnvironment()))){


                            if(p.getWorld().getEnvironment().equals(World.Environment.NORMAL)){
                                for (int kk = 0; kk < p.getInventory().getContents().length; kk++){
                                    if(p.getInventory().getItem(kk).getType() == Material.COMPASS){
                                /*CompassMeta meta = (CompassMeta) item.getItemMeta();
                                meta.setLodestoneTracked(false);
                                item.setItemMeta(meta);*/
                                        p.getInventory().setItem(kk, new ItemStack(Material.COMPASS, 1));
                                        p.setCompassTarget(entity.getLocation());
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
                                    p.setCompassTarget(entity.getLocation());
                                    break;
                                }
                            }

                        }
                    }

                }, 0L, 10L);
            }
        }
    }



    @EventHandler

    public void onPlayerDeath(PlayerDeathEvent event){
        Player p = event.getEntity().getPlayer();

        if(p.getInventory().contains(Material.COMPASS)){
            p.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }


    public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight(null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (int i = 0; i < sightBlock.size(); i++) {
            sight.add(sightBlock.get(i).getLocation());
        }
        for (int i = 0; i<sight.size(); i++) {
            for (int k = 0; k<entities.size(); k++) {
                if (Math.abs(entities.get(k).getLocation().getX() - sight.get(i).getX()) < 1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY() - sight.get(i).getY()) < 1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ() - sight.get(i).getZ()) < 1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null;
    }
}
