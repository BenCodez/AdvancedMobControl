package com.Ben12345rocks.AdvancedMobControl.Object;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.Ben12345rocks.AdvancedCore.Util.Item.ItemBuilder;
import com.Ben12345rocks.AdvancedCore.YML.YMLFile;

public class EntityHandle {
	private String type;
	private String world;

	/**
	 * @return the file
	 */
	public YMLFile getFile() {
		return file;
	}

	private ConfigurationSection data;
	private YMLFile file;

	public EntityHandle(String type, String world2, ConfigurationSection data, YMLFile file) {
		this.type = type;
		this.world = world2;
		this.data = data;
		this.file = file;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the world
	 */
	public String getWorld() {
		return world;
	}

	/**
	 * @param world
	 *            the world to set
	 */
	public void setWorld(String world) {
		this.world = world;
	}

	/**
	 * @return the data
	 */
	public ConfigurationSection getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(ConfigurationSection data) {
		this.data = data;

	}

	public int getExp() {
		return getData().getInt("Exp", -1);
	}

	public int getMoney() {
		return getData().getInt("Money");
	}

	public int getLooting() {
		return getData().getInt("Looting", -1);
	}

	public int getHealth() {
		return getData().getInt("Health", -1);
	}

	public String getSpawnReason() {
		return getData().getString("SpawnReason", "");
	}

	@SuppressWarnings("deprecation")
	public ArrayList<ItemStack> getDrops() {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ConfigurationSection data = getData().getConfigurationSection("Drops");
		if (data != null) {
			for (String str : data.getKeys(false)) {
				items.add(new ItemBuilder(data.getConfigurationSection(str)).toItemStack());
			}
		}
		return items;
	}

	public boolean removeDrops() {
		return getData().getBoolean("RemoveDrops");
	}

	public boolean isRightClickDisabled() {
		return getData().getBoolean("DisableRightClick");
	}

}
