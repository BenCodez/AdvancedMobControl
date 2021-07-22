package com.bencodez.advancedmobcontrol.object;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.bencodez.advancedcore.AdvancedCorePlugin;
import com.bencodez.advancedcore.api.item.ItemBuilder;
import com.bencodez.advancedcore.api.yml.YMLFileHandler;

import lombok.Getter;

public class EntityHandle {
	@Getter
	private String world;
	@Getter
	private int health;
	@Getter
	private String type;
	@Getter
	private boolean disableRightClick;
	@Getter
	private boolean removeDrops;
	private String spawnReason;
	@Getter
	private int looting;
	@Getter
	private int money;
	@Getter
	private int exp;
	@Getter
	private int priority = 0;
	private ArrayList<ConfigurationSection> drops;
	@Getter
	private YMLFileHandler file;

	public EntityHandle(AdvancedCorePlugin plugin, File file) {
		this.file = new YMLFileHandler(plugin, file);
		this.file.setup();
		loadValues();
	}

	public boolean isSpawnReason(String spawnReason) {
		if (spawnReason.isEmpty()) {
			return true;
		}
		for (String str : this.spawnReason.split(Pattern.quote("|"))) {
			if (str.equalsIgnoreCase(spawnReason)) {
				return true;
			}
		}
		return false;
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
