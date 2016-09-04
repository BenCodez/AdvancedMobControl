/*
 *
 */
package com.Ben12345rocks.AdvancedMobControl.Config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.Ben12345rocks.AdvancedCore.Util.Files.FilesManager;
import com.Ben12345rocks.AdvancedMobControl.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class Config.
 */
public class Config {

	/** The instance. */
	static Config instance = new Config();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/**
	 * Gets the single instance of Config.
	 *
	 * @return single instance of Config
	 */
	public static Config getInstance() {
		return instance;
	}

	/** The data. */
	FileConfiguration data;

	/** The d file. */
	File dFile;

	/**
	 * Instantiates a new config.
	 */
	private Config() {
	}

	/**
	 * Instantiates a new config.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public Config(Main plugin) {
		Config.plugin = plugin;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public FileConfiguration getData() {
		return data;
	}

	/**
	 * Gets the format money.
	 *
	 * @return the format money
	 */
	public String getFormatMoney() {
		return getData().getString("Format.Money",
				"You were given $%Money% for killing %Entity%");
	}

	/**
	 * Gets the format money damage.
	 *
	 * @return the format money damage
	 */
	public String getFormatMoneyDamage() {
		return getData().getString("Format.MoneyDamage",
				"You were given $%Money% for killing %Entity% by %Damage%");
	}

	/**
	 * Gets the max mobs.
	 *
	 * @return the max mobs
	 */
	public int getMaxMobs() {
		return getData().getInt("MaxMobs", 20);
	}

	/**
	 * Reload data.
	 */
	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dFile);
	}

	/**
	 * Save data.
	 */
	public void saveData() {
		FilesManager.getInstance().editFile(dFile, data);
	}

	/**
	 * Sets the up.
	 *
	 * @param p
	 *            the new up
	 */
	public void setup(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		dFile = new File(p.getDataFolder(), "Config.yml");

		if (!dFile.exists()) {
			try {
				dFile.createNewFile();
				plugin.saveResource("Config.yml", true);
			} catch (IOException e) {
				Bukkit.getServer().getLogger()
				.severe(ChatColor.RED + "Could not create Config.yml!");
			}
		}

		data = YamlConfiguration.loadConfiguration(dFile);
	}

}
