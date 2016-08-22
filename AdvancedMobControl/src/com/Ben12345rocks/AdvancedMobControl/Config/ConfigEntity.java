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
 * The Class ConfigEntity.
 */
public class ConfigEntity {

	/** The instance. */
	static ConfigEntity instance = new ConfigEntity();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/**
	 * Gets the single instance of ConfigEntity.
	 *
	 * @return single instance of ConfigEntity
	 */
	public static ConfigEntity getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new config entity.
	 */
	private ConfigEntity() {
	}

	/**
	 * Instantiates a new config entity.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public ConfigEntity(Main plugin) {
		ConfigEntity.plugin = plugin;
	}

	/**
	 * Gets the data.
	 *
	 * @param entity
	 *            the entity
	 * @return the data
	 */
	public FileConfiguration getData(String entity) {
		File dFile = getEntityFile(entity);
		FileConfiguration data = YamlConfiguration.loadConfiguration(dFile);
		return data;
	}

	/**
	 * Gets the entity file.
	 *
	 * @param entity
	 *            the entity
	 * @return the entity file
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
	 * Gets the entitys files.
	 *
	 * @return the entitys files
	 */
	public ArrayList<String> getEntitysFiles() {
		File folder = new File(plugin.getDataFolder() + File.separator
				+ "Entity");
		String[] fileNames = folder.list();
		return com.Ben12345rocks.AdvancedCore.Utils.getInstance().convertArray(
				fileNames);
	}

	/**
	 * Gets the entitys names.
	 *
	 * @return the entitys names
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
	 * Gets the exp.
	 *
	 * @param entity
	 *            the entity
	 * @return the exp
	 */
	public int getExp(String entity) {
		return getData(entity).getInt("EXP");
	}

	/**
	 * Gets the health.
	 *
	 * @param entity
	 *            the entity
	 * @param spawnReason
	 *            the spawn reason
	 * @return the health
	 */
	public double getHealth(String entity, String spawnReason) {
		return getData(entity).getDouble(spawnReason + ".Health");
	}

	/**
	 * Gets the money.
	 *
	 * @param entity
	 *            the entity
	 * @return the money
	 */
	public int getMoney(String entity) {
		return getData(entity).getInt("Money");
	}

	/**
	 * Gets the money.
	 *
	 * @param entity
	 *            the entity
	 * @param damage
	 *            the damage
	 * @return the money
	 */
	public int getMoney(String entity, String damage) {
		return getData(entity).getInt(damage + ".Money");
	}

	/**
	 * Rename entity.
	 *
	 * @param entity
	 *            the entity
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
	 * Save data.
	 *
	 * @param dFile
	 *            the d file
	 * @param data
	 *            the data
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
	 * Sets the.
	 *
	 * @param entity
	 *            the entity
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
	 * Sets the exp.
	 *
	 * @param entity
	 *            the entity
	 * @param value
	 *            the value
	 */
	public void setExp(String entity, int value) {
		set(entity, "EXP", value);
	}

	/**
	 * Sets the health.
	 *
	 * @param entity
	 *            the entity
	 * @param spawnReason
	 *            the spawn reason
	 * @param value
	 *            the value
	 */
	public void setHealth(String entity, String spawnReason, double value) {
		set(entity, spawnReason + ".Health", value);
	}

	/**
	 * Sets the money.
	 *
	 * @param entity
	 *            the entity
	 * @param value
	 *            the value
	 */
	public void setMoney(String entity, int value) {
		set(entity, "Money", value);
	}

	/**
	 * Sets the money.
	 *
	 * @param entity
	 *            the entity
	 * @param damage
	 *            the damage
	 * @param value
	 *            the value
	 */
	public void setMoney(String entity, String damage, int value) {
		set(entity, damage + ".Money", value);
	}

	/**
	 * Sets the up.
	 *
	 * @param entity
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
}
