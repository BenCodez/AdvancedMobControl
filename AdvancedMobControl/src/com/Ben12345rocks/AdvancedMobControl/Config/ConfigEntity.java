package com.Ben12345rocks.AdvancedMobControl.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.Ben12345rocks.AdvancedMobControl.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigEntitys.
 */
public class ConfigEntity {

	/** The instance. */
	static ConfigEntity instance = new ConfigEntity();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/**
	 * Gets the single instance of ConfigEntitys.
	 *
	 * @return single instance of ConfigEntitys
	 */
	public static ConfigEntity getInstance() {
		return instance;
	}

	/**
	 * Gets the data.
	 *
	 * @param Entity
	 *            the site name
	 * @return the data
	 */
	public FileConfiguration getData(String entity) {
		File dFile = getEntityFile(entity);
		FileConfiguration data = YamlConfiguration.loadConfiguration(dFile);
		return data;
	}

	/**
	 * Instantiates a new config vote sites.
	 */
	private ConfigEntity() {
	}

	/**
	 * Instantiates a new config vote sites.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public ConfigEntity(Main plugin) {
		ConfigEntity.plugin = plugin;
	}

	/**
	 * Gets the vote site file.
	 *
	 * @param Entity
	 *            the site name
	 * @return the vote site file
	 */
	public File getEntityFile(String entity) {
		File dFile = new File(plugin.getDataFolder() + File.separator
				+ "Entity", entity + ".yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(dFile);
		if (!dFile.exists()) {
			try {
				data.save(dFile);
			} catch (IOException e) {
				plugin.getLogger().severe(
						ChatColor.RED + "Could not create Entity/" + entity
								+ ".yml!");

			}
		}
		return dFile;

	}

	/**
	 * Gets the vote sites files.
	 *
	 * @return the vote sites files
	 */
	public ArrayList<String> getEntitysFiles() {
		File folder = new File(plugin.getDataFolder() + File.separator
				+ "Entity");
		String[] fileNames = folder.list();
		return com.Ben12345rocks.AdvancedCore.Utils.getInstance().convertArray(fileNames);
	}

	/**
	 * Gets the vote sites names.
	 *
	 * @return the vote sites names
	 */
	public ArrayList<String> getEntitysNames() {
		ArrayList<String> Entitys = getEntitysFiles();
		if (Entitys == null) {
			return new ArrayList<String>();
		}
		for (int i = 0; i < Entitys.size(); i++) {
			Entitys.set(i, Entitys.get(i).replace(".yml", ""));
		}
		for (int i = Entitys.size() - 1; i >= 0; i--) {
			// plugin.getLogger().info(Entitys.get(i));
			if (Entitys.get(i).equalsIgnoreCase("Example")
					|| Entitys.get(i).equalsIgnoreCase("null")) {
				// plugin.getLogger().info("Removed: " + Entitys.get(i));
				Entitys.remove(i);

			}

		}

		return Entitys;
	}

	/**
	 * Rename vote site.
	 *
	 * @param Entity
	 *            the site name
	 * @param newName
	 *            the new name
	 * @return true, if successful
	 */
	public boolean renameEntity(String entity, String newName) {
		return getEntityFile(entity).renameTo(
				new File(plugin.getDataFolder() + File.separator + "Entity",
						newName + ".yml"));
	}

	/**
	 * Sets the.
	 *
	 * @param Entity
	 *            the site name
	 * @param path
	 *            the path
	 * @param value
	 *            the value
	 */
	public void set(String entity, String path, Object value) {
		// String playerName = user.getPlayerName();
		File dFile = getEntityFile(entity);
		FileConfiguration data = YamlConfiguration.loadConfiguration(dFile);
		data.set(path, value);
		saveData(dFile, data);
	}

	/**
	 * Save data.
	 */
	public void saveData(File dFile, FileConfiguration data) {
		try {
			data.save(dFile);
		} catch (IOException e) {
			Bukkit.getServer()
					.getLogger()
					.severe(ChatColor.RED + "Could not save " + dFile.getName());
		}
	}

	/**
	 * Sets the up.
	 *
	 * @param Entity
	 *            the new up
	 */
	public void setup(String entity) {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		File dFile = new File(plugin.getDataFolder() + File.separator
				+ "Entity", entity + ".yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(dFile);
		if (!dFile.exists()) {
			try {
				data.save(dFile);
				if (entity.equalsIgnoreCase("ExampleEntity")) {
					plugin.saveResource("Entity" + File.separator
							+ "ExampleEntity.yml", true);
				}
			} catch (IOException e) {
				plugin.getLogger().severe(
						ChatColor.RED + "Could not create Entity/" + entity
								+ ".yml!");
			}
		}
	}

	public int getMoney(String entity) {
		return getData(entity).getInt("Money");
	}

	public void setMoney(String entity, int value) {
		set(entity, "Money", value);
	}

	public int getMoney(String entity, String damage) {
		return getData(entity).getInt(damage + ".Money");
	}

	public void setMoney(String entity, String damage, int value) {
		set(entity, damage + ".Money", value);
	}

	public void setExp(String entity, int value) {
		set(entity, "EXP", value);
	}

	public int getExp(String entity) {
		return getData(entity).getInt("EXP");
	}

	public double getHealth(String entity, String spawnReason) {
		return getData(entity).getDouble(spawnReason + ".Health");
	}

	public void setHealth(String entity, String spawnReason, double value) {
		set(entity, spawnReason + ".Health", value);
	}
}
