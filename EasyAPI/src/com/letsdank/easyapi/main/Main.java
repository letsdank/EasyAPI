package com.letsdank.easyapi.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static final String VERSION = "0.1";
	
	public static JavaPlugin instance;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		System.out.println("Initialized EasyAPI version " + VERSION);
	}
}
