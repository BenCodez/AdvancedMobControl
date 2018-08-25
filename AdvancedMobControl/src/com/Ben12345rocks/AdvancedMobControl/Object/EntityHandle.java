package com.Ben12345rocks.AdvancedMobControl.Object;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.Ben12345rocks.AdvancedCore.Util.Item.ItemBuilder;
import com.Ben12345rocks.AdvancedCore.YML.YMLFile;
import com.Ben12345rocks.AdvancedCore.YML.YMLFileHandler;

public class EntityHandle {
	private String world;
	private int health;
	private String type;
	private boolean disableRightClick;
	private boolean removeDrops;
	private String spawnReason;
	private int looting;
	private int money;
	private int exp;
	private int priority = 0;
	private ArrayList<ConfigurationSection> drops;

	private YMLFileHandler file;

	public EntityHandle(File file) {
		this.file = new YMLFileHandler(file);
		this.file.setup();
		loadValues();
	}

	public void addDrop(ItemStack itemInHand) {
		String itemSec = itemInHand.getType().toString();
		if (getData().isConfigurationSection("Drops")) {
			while (getData().getConfigurationSection("Drops").getKeys(false).contains(itemSec)) {
				itemSec += "1";
			}
		}

		HashMap<String, Object> data = new ItemBuilder(itemInHand).createConfigurationData();

		for (Entry<String, Object> entry : data.entrySet()) {
			getData().set("Drops." + itemSec + "." + entry.getKey(), entry.getValue());
		}
		file.saveData();

		loadValues();
	}

	private void addPriority() {
		priority = priority + 1;
	}

	public FileConfiguration getData() {
		return file.getData();
	}

	public ArrayList<ItemBuilder> getDrops() {
		ArrayList<ItemBuilder> drops = new ArrayList<ItemBuilder>();
		for (ConfigurationSection d : this.drops) {
			drops.add(new ItemBuilder(d));
		}
		return drops;
	}

	public int getExp() {
		return exp;
	}

	/**
	 * @return the file
	 */
	public YMLFile getFile() {
		return file;
	}

	public int getHealth() {
		return health;
	}

	public int getLooting() {
		return looting;
	}

	public int getMoney() {
		return money;
	}

	public int getPriority() {
		return priority;
	}

	public String getSpawnReason() {
		return spawnReason;
	}

	public String getType() {
		return type;
	}

	public String getWorld() {
		return world;
	}

	public boolean isDisableRightClick() {
		return disableRightClick;
	}

	public boolean isRemoveDrops() {
		return removeDrops;
	}

	public void loadValues() {
		world = getData().getString("World", "");
		health = getData().getInt("Health", -1);
		type = getData().getString("EntityType", "");
		disableRightClick = getData().getBoolean("DisableRightClick");
		removeDrops = getData().getBoolean("RemoveDrops");
		spawnReason = getData().getString("SpawnReason", "");
		looting = getData().getInt("Looting", -1);
		money = getData().getInt("Money");
		exp = getData().getInt("Exp");
		drops = new ArrayList<ConfigurationSection>();
		if (getData().isConfigurationSection("Drops")) {
			for (String sec : getData().getConfigurationSection("Drops").getKeys(false)) {
				ConfigurationSection d = getData().getConfigurationSection("Drops." + sec);
				drops.add(d);
			}
		}

		if (!world.isEmpty()) {
			addPriority();
		}
		if (!spawnReason.isEmpty()) {
			addPriority();
		}
		if (looting >= 0) {
			addPriority();
		}

		int p = getData().getInt("Priority", -1);
		if (p > 0) {
			priority = p;
		}
	}

	public void removeDrop(ItemBuilder item) {
		if (getData().isConfigurationSection("Drops")) {
			for (String sec : getData().getConfigurationSection("Drops").getKeys(false)) {
				ConfigurationSection d = getData().getConfigurationSection("Drops." + sec);
				if (new ItemBuilder(d).equals(item)) {
					set("Drops." + sec, null);
				}
			}
		}
	}

	public void set(String key, Object value) {
		getData().set(key, value);
		file.saveData();
	}

}
