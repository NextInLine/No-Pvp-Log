package me.nextinline.AntiLogger;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AntiLogEvent implements Listener {
	
	ArrayList<Player> inCombat = new ArrayList<Player>();
	Core plugin;
	public AntiLogEvent(Core plugin){
		this.plugin = plugin;
	}


	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event){
		
		final Player attacker = (Player) event.getDamager();
		final Player attacked = (Player) event.getEntity();
		if(!(attacked.hasPermission("nopvplog.exempt"))){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){

			if(!(inCombat.contains(attacker))){
				
				inCombat.add(attacker);
				inCombat.add(attacked);
				attacker.sendMessage(ChatColor.RED+"You are now in combat with " + attacked.getName());
				attacked.sendMessage(ChatColor.RED+"You are now in combat with " + attacker.getName());	


				plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {

					@Override
					public void run() {
						attacker.sendMessage(ChatColor.RED+"You are no longer in combat!");
						attacked.sendMessage(ChatColor.RED+"You are no longer in combat!");

						inCombat.remove(attacker);
						inCombat.remove(attacked);

					}
				},  plugin.getConfig().getLong("Time_In_Combat") * 20);
			}     
		}
	}
}
	@EventHandler
	public void playerLogEvent(PlayerCommandPreprocessEvent event){
		if(plugin.getConfig().getBoolean("Disable_Commands_In_PVP") == true){
			Player commandguy = event.getPlayer();
			if(inCombat.contains(commandguy)){
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerLogEvent(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(inCombat.contains(player)){
			if(plugin.getConfig().getBoolean("Kill_On_Pvp_Log") == true){
				player.setHealth(0);
			}
			if(plugin.getConfig().getBoolean("Announce_On_PvP_Log") == true){
				plugin.getServer().broadcastMessage(ChatColor.RED+ player.getName() + " has just PVP logged! Shame on them!" );
			}
			if(plugin.getConfig().getBoolean("Ban_On_Pvp_Log") == true){
				player.setBanned(true);
			}
		}
	}
}
