package me.nextinline.AntiLogger;

import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin{
	public void onEnable(){
	
		getServer().getPluginManager().registerEvents(new AntiLogEvent(this), this);
		this.saveDefaultConfig();
	    this.getConfig().options().copyDefaults(true);
	}
	public void onDisable(){
		
	}
}
