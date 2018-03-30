package com.Ben12345rocks.AdvancedMobControl.Object;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

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

	private ConfigurationSection data;
	private YMLFileHandler file;

	public EntityHandle(File file) {
		this.file = new YMLFileHandler(file);
		data = this.file.getData();
		world = data.getString("World", "");
		health = data.getInt("Health", -1);
		type = data.getString("EnitytType", "");
		disableRightClick = data.getBoolean("DisableRightClick");
		removeDrops = data.getBoolean("RemoveDrops");
		spawnReason = data.getString("SpawnReason", "");
		looting = data.getInt("Looting", -1);
		money = data.getInt("Money");
		exp = data.getInt("Exp");
		drops = new ArrayList<ItemBuilder>();
		if (data.isConfigurationSection("Drops")) {
			for (String sec : data.getConfigurationSection("Drops").getKeys(false)) {
				ConfigurationSection d = data.getConfigurationSection("Drops." + sec);
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

	public ConfigurationSection getData() {
		return data;
	}

}
