package com.Ben12345rocks.AdvancedMobControl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedCore.Objects.RewardHandler;
import com.Ben12345rocks.AdvancedCore.Thread.Thread;
import com.Ben12345rocks.AdvancedCore.Util.Metrics.Metrics;
import com.Ben12345rocks.AdvancedCore.Util.Updater.Updater;
import com.Ben12345rocks.AdvancedMobControl.Commands.CommandLoader;
import com.Ben12345rocks.AdvancedMobControl.Commands.Executor.CommandAdvancedMobControl;
import com.Ben12345rocks.AdvancedMobControl.Commands.TabComplete.AdvancedMobControlTabCompleter;
import com.Ben12345rocks.AdvancedMobControl.Config.Config;
import com.Ben12345rocks.AdvancedMobControl.Listeners.EntityDeath;
import com.Ben12345rocks.AdvancedMobControl.Listeners.MobClicked;
import com.Ben12345rocks.AdvancedMobControl.Listeners.MobSpawn;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main extends JavaPlugin {

	/** The plugin. */
	public static Main plugin;

	/** The advanced mob control commands. */
	public ArrayList<CommandHandler> advancedMobControlCommands;

	/** The updater. */
	public Updater updater;

	/**
	 * Check advanced core.
	 */
	public void checkAdvancedCore() {
		if (Bukkit.getPluginManager().getPlugin("AdvancedCore") != null) {
			plugin.getLogger().info("Found AdvancedCore");
		} else {
			plugin.getLogger().severe(
					"Failed to find AdvancedCore, plugin disabling");
			plugin.getLogger()
					.severe("Download at: https://www.spigotmc.org/resources/advancedcore.28295/");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
	}

	/**
	 * Check update.
	 */
	public void checkUpdate() {
		plugin.updater = new Updater(plugin, 28297, false);
		final Updater.UpdateResult result = plugin.updater.getResult();
		switch (result) {
		case FAIL_SPIGOT: {
			plugin.getLogger().info(
					"Failed to check for update for " + plugin.getName() + "!");
			break;
		}
		case NO_UPDATE: {
			plugin.getLogger().info(
					plugin.getName() + " is up to date! Version: "
							+ plugin.updater.getVersion());
			break;
		}
		case UPDATE_AVAILABLE: {
			plugin.getLogger().info(
					plugin.getName()
							+ " has an update available! Your Version: "
							+ plugin.getDescription().getVersion()
							+ " New Version: " + plugin.updater.getVersion());
			break;
		}
		default: {
			break;
		}
		}
	}

	/**
	 * Debug.
	 *
	 * @param message
	 *            the message
	 */
	public void debug(String message) {
		com.Ben12345rocks.AdvancedCore.Main.plugin.debug(plugin, message);
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

		RewardHandler.getInstance().addRewardFolder(
				new File(plugin.getDataFolder(), "Rewards"));

		plugin.getLogger().info(
				"Enabled " + plugin.getName() + " "
						+ plugin.getDescription().getVersion());

		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,
				new Runnable() {

					@Override
					public void run() {
						Thread.getInstance().run(new Runnable() {

							@Override
							public void run() {
								checkUpdate();
							}
						});
					}
				}, 10l);

		com.Ben12345rocks.AdvancedCore.Main.plugin.registerHook(this);

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
		pm.registerEvents(new MobClicked(this), this);

		plugin.debug("Loaded Events");

	}

	/**
	 * Reload.
	 */
	public void reload() {
		Config.getInstance().reloadData();
		com.Ben12345rocks.AdvancedCore.Main.plugin.reload();
	}

	/**
	 * Setup files.
	 */
	public void setupFiles() {
		Config.getInstance().setup(plugin);
		plugin.debug("Loaded Files");

	}

}
