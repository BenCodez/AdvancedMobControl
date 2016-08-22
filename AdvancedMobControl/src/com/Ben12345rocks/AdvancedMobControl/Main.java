package com.Ben12345rocks.AdvancedMobControl;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedCore.Util.Metrics.Metrics;
import com.Ben12345rocks.AdvancedMobControl.Commands.CommandLoader;
import com.Ben12345rocks.AdvancedMobControl.Commands.Executor.CommandAdvancedMobControl;
import com.Ben12345rocks.AdvancedMobControl.Commands.TabComplete.AdvancedMobControlTabCompleter;
import com.Ben12345rocks.AdvancedMobControl.Config.Config;
import com.Ben12345rocks.AdvancedMobControl.Listeners.EntityDeath;
import com.Ben12345rocks.AdvancedMobControl.Listeners.MobSpawn;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main extends JavaPlugin {

	/** The plugin. */
	public static Main plugin;

	public ArrayList<CommandHandler> advancedMobControlCommands;

	/**
	 * Debug.
	 *
	 * @param message
	 *            the message
	 */
	public void debug(String message) {
		if (Config.getInstance().getDebugEnabled()) {
			plugin.getLogger().info("Debug: " + message);
		}
	}

	/**
	 * Metrics.
	 */
	private void metrics() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
			plugin.debug("Loaded Metrics");
		} catch (IOException e) {
			plugin.getLogger().info("Can't submit metrics stats");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable() {
		plugin = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {
		plugin = this;
		checkAdvancedCore();
		setupFiles();
		registerCommands();
		registerEvents();
		metrics();

		plugin.getLogger().info(
				"Enabled " + plugin.getName() + " "
						+ plugin.getDescription().getVersion());

	}
	
	public void checkAdvancedCore() {
		if (Bukkit.getPluginManager().getPlugin("AdvancedCore") != null) {
			plugin.getLogger().info("Found AdvancedCore");
		} else {
			plugin.getLogger().severe(
					"Failed to find AdvancedCore, plugin disabling");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
	}

	/**
	 * Register commands.
	 */
	private void registerCommands() {
		new CommandLoader();
		Bukkit.getPluginCommand("advancedmobcontrol").setExecutor(
				new CommandAdvancedMobControl(this));
		Bukkit.getPluginCommand("advancedmobcontrol").setTabCompleter(
				new AdvancedMobControlTabCompleter());

		plugin.debug("Loaded Commands");

	}

	/**
	 * Register events.
	 */
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new EntityDeath(this), this);
		pm.registerEvents(new MobSpawn(this), this);

		plugin.debug("Loaded Events");

	}

	/**
	 * Reload.
	 */
	public void reload() {
		Config.getInstance().reloadData();
	}

	/**
	 * Setup files.
	 */
	public void setupFiles() {
		Config.getInstance().setup(plugin);
		plugin.debug("Loaded Files");

	}

}
