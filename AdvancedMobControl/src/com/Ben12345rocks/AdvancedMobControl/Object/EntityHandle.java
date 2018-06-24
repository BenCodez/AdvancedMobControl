package com.Ben12345rocks.AdvancedMobControl.Object;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

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
	private ArrayList<ItemBuilder> drops;

	/**
	 * @return the file
	 */
	public YMLFile getFile() {
		return file;
	}

	private YMLFileHandler file;

	public EntityHandle(File file) {
		this.file = new YMLFileHandler(file);
		this.file.setup();
		loadValues();
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
		drops = new ArrayList<ItemBuilder>();
		if (getData().isConfigurationSection("Drops")) {
			for (String sec : getData().getConfigurationSection("Drops").getKeys(false)) {
				ConfigurationSection d = getData().getConfigurationSection("Drops." + sec);
				drops.add(new ItemBuilder(d));
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

	public ArrayList<ItemBuilder> getDrops() {
		return drops;
	}

	private void addPriority() {
		priority = priority + 1;
	}

	public int getPriority() {
		return priority;
	}

	public int getHealth() {
		return health;
	}

	public String getType() {
		return type;
	}

	public boolean isDisableRightClick() {
		return disableRightClick;
	}

	public boolean isRemoveDrops() {
		return removeDrops;
	}

	public String getSpawnReason() {
		return spawnReason;
	}

	public int getLooting() {
		return looting;
	}

	public int getMoney() {
		return money;
	}

	public int getExp() {
		return exp;
	}

	public String getWorld() {
		return world;
	}

	public FileConfiguration getData() {
		return file.getData();
	}

	public void set(String key, Object value) {
		getData().set(key, value);
		file.saveData();
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

}
