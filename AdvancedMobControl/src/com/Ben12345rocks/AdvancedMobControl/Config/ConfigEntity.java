package com.Ben12345rocks.AdvancedMobControl.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.Ben12345rocks.AdvancedCore.Util.Misc.ArrayUtils;
import com.Ben12345rocks.AdvancedCore.YML.YMLFile;
import com.Ben12345rocks.AdvancedMobControl.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigEntity.
 */
public class ConfigEntity extends YMLFile {

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
	public ConfigEntity() {
		super(new File(Main.plugin.getDataFolder(), "Entities.yml"));
	}

	/**
	 * Gets the data.
	 *
	 * @param entity
	 *            the entity
	 * @return the data
	 */
	public ConfigurationSection getData(String entity) {
		if (getData().isConfigurationSection(entity)) {
			return getData().getConfigurationSection(entity);
		}
		getData().createSection(entity);
		return getData().getConfigurationSection(entity);
	}

	/**
	 * Gets the entitys names.
	 *
	 * @return the entitys names
	 */
	public ArrayList<String> getEntitysNames() {
		Set<String> entities = getData().getConfigurationSection("").getKeys(false);
		if (entities == null) {
			entities = new HashSet<String>();
		}

		return ArrayUtils.getInstance().convert(entities);
	}

	/**
	 * Gets the exp.
	 *
	 * @param entity
	 *            the entity
	 * @return the exp
	 */
	public int getExp(String entity, int looting) {
		return getData(entity).getInt("EXP." + looting);
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

	@SuppressWarnings("unchecked")
	public List<String> getRewards(String entity) {
		return (List<String>) getData(entity).getList("Rewards", new ArrayList<String>());
	}

	@SuppressWarnings("unchecked")
	public List<String> getRewards(String entity, String damage) {
		return (List<String>) getData(entity).getList(damage + ".Rewards", new ArrayList<String>());
	}

	public boolean getDisableNormalClick(String entity) {
		return getData(entity).getBoolean("DisableRightClick");
	}

	public void setDisableNormalClick(String entity, boolean value) {
		set(entity + ".DisableRightClick", value);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getRightClickedRewards(String entity) {
		return (ArrayList<String>) getData(entity).getList("RightClickRewards", new ArrayList<String>());
	}

	public void setRightClickedRewards(String entity, ArrayList<String> value) {
		set(entity + ".RightClickRewards", value);
	}

	/**
	 * Sets the exp.
	 *
	 * @param entity
	 *            the entity
	 * @param value
	 *            the value
	 */
	public void setExp(String entity, int looting, int value) {
		set(entity + ".EXP." + looting, value);
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
		set(entity + "." + spawnReason + ".Health", value);
	}

	/**
	 * Sets the money.
	 *
	 * @param entity
	 *            the entity
	 * @param value
	 *            the value
	 */
	public void setRewards(String entity, List<String> value) {
		set(entity + ".Rewards", value);
	}

	public void set(String path, Object value) {
		getData().set(path, value);
		saveData();
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
	 * Sets the money.
	 *
	 * @param entity
	 *            the entity
	 * @param value
	 *            the value
	 */
	public void setMoney(String entity, int value) {
		set(entity + ".Money", value);
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
		set(entity + "." + damage + ".Money", value);
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
	 * Sets the money.
	 *
	 * @param entity
	 *            the entity
	 * @param damage
	 *            the damage
	 * @param value
	 *            the value
	 */
	public void setRewards(String entity, String damage, List<String> value) {
		set(entity + "." + damage + ".Rewards", value);
	}

	public Set<String> getDrops(String entity, int looting) {
		if (!getData(entity).isConfigurationSection("Drops." + looting)) {
			getData(entity).createSection("Drops." + looting);
		}
		return getData(entity).getConfigurationSection("Drops." + looting).getKeys(false);

	}

	@Override
	public void onFileCreation() {
	}

	public ConfigurationSection getDropsItem(String entity, int looting, String item) {
		return getData(entity).getConfigurationSection("Drops." + looting + "." + item);
	}
}
