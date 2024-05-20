package com.bencodez.advancedmobcontrol;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.bencodez.advancedcore.AdvancedCorePlugin;
import com.bencodez.advancedcore.api.command.CommandHandler;
import com.bencodez.advancedcore.api.updater.Updater;
import com.bencodez.advancedmobcontrol.commands.CommandLoader;
import com.bencodez.advancedmobcontrol.commands.executor.CommandAdvancedMobControl;
import com.bencodez.advancedmobcontrol.commands.tabcomplete.AdvancedMobControlTabCompleter;
import com.bencodez.advancedmobcontrol.config.Config;
import com.bencodez.advancedmobcontrol.listeners.EntityDeath;
import com.bencodez.advancedmobcontrol.listeners.MobClicked;
import com.bencodez.advancedmobcontrol.listeners.MobSpawn;
import com.bencodez.advancedmobcontrol.object.EntityHandler;
import com.bencodez.simpleapi.metrics.BStatsMetrics;

import lombok.Getter;
import lombok.Setter;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class AdvancedMobControlMain extends AdvancedCorePlugin {

	/** The plugin. */
	public static AdvancedMobControlMain plugin;

	/** The advanced mob control commands. */
	@Getter
	@Setter
	private ArrayList<CommandHandler> advancedMobControlCommands;

	/** The updater. */
	private Updater updater;

	@Getter
	private EntityHandler entityHandler;

	@Getter
	private Config configFile;

	/**
	 * Check update.
	 */
	public void checkUpdate() {
		plugin.updater = new Updater(plugin, 28297, false);
		final Updater.UpdateResult result = plugin.updater.getResult();
		switch (result) {
		case FAIL_SPIGOT: {
			plugin.getLogger().info("Failed to check for update for " + plugin.getName() + "!");
			break;
		}
		case NO_UPDATE: {
			plugin.getLogger().info(plugin.getName() + " is up to date! Version: " + plugin.updater.getVersion());
			break;
		}
		case UPDATE_AVAILABLE: {
			plugin.getLogger().info(plugin.getName() + " has an update available! Your Version: "
					+ plugin.getDescription().getVersion() + " New Version: " + plugin.updater.getVersion());
			break;
		}
		default: {
			break;
		}
		}
	}

	/**
	 * Metrics.
	 */
	private void metrics() {
		new BStatsMetrics(this, 41);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onUnLoad() {
		plugin = null;
	}

	@Override
	public void onPostLoad() {

		registerCommands();
		registerEvents();
		entityHandler = new EntityHandler(this);
		metrics();

		plugin.getLogger().info("Enabled " + plugin.getName() + " " + plugin.getDescription().getVersion());

		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				checkUpdate();
			}
		}, 10l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onPreLoad() {
		plugin = this;

		setupFiles();
		setJenkinsSite("ben12345rocks.com");
		updateHook();

	}

	/**
	 * Register commands.
	 */
	private void registerCommands() {
		new CommandLoader();
		Bukkit.getPluginCommand("advancedmobcontrol").setExecutor(new CommandAdvancedMobControl(this));
		Bukkit.getPluginCommand("advancedmobcontrol").setTabCompleter(new AdvancedMobControlTabCompleter());

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
		configFile.reloadData();
		updateHook();
		updateHook();
		entityHandler.load();

	}

	/**
	 * Setup files.
	 */
	public void setupFiles() {
		configFile = new Config(this);
		plugin.debug("Loaded Files");

	}

	public void updateHook() {
		setConfigData(configFile.getData());
	}

}
