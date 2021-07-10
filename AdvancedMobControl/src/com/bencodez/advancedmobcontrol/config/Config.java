/*
 *
 */
package com.bencodez.advancedmobcontrol.config;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;

import com.bencodez.advancedcore.api.yml.YMLFile;
import com.bencodez.advancedmobcontrol.AdvancedMobControlMain;

// TODO: Auto-generated Javadoc
/**
 * The Class Config.
 */
public class Config extends YMLFile {

	private AdvancedMobControlMain plugin;

	/**
	 * Instantiates a new config.
	 */
	public Config(AdvancedMobControlMain plugin) {
		super(plugin, new File(plugin.getDataFolder(), "Config.yml"), true);
		this.plugin = plugin;
	}

	public String getDataStorage() {
		return getData().getString("DataStorage", "FLAT");
	}

	public boolean getDebug() {
		return getData().getBoolean("Debug");
	}

	/**
	 * Gets the format help line.
	 *
	 * @return the format help line
	 */
	public String getFormatHelpLine() {
		return getData().getString("Format.HelpLine", "&3&l%Command% - &3%HelpMessage%");
	}

	/**
	 * Gets the format money.
	 *
	 * @return the format money
	 */
	public String getFormatMoney() {
		return getData().getString("Format.Money", "You were given $%Money% for killing %Entity%");
	}

	/**
	 * Gets the format money damage.
	 *
	 * @return the format money damage
	 */
	public String getFormatMoneyDamage() {
		return getData().getString("Format.MoneyDamage", "You were given $%Money% for killing %Entity% by %Damage%");
	}

	/**
	 * Gets the format no perms.
	 *
	 * @return the format no perms
	 */
	public String getFormatNoPerms() {
		return getData().getString("Format.NoPerms", "&cYou do not have enough permission!");
	}

	/**
	 * Gets the format not number.
	 *
	 * @return the format not number
	 */
	public String getFormatNotNumber() {
		return getData().getString("Format.NotNumber", "&cError on &6%arg%&c, number expected!");
	}

	/**
	 * Gets the max mobs.
	 *
	 * @return the max mobs
	 */
	public int getMaxMobs() {
		return getData().getInt("MaxMobs", 20);
	}

	public ConfigurationSection getMysql() {
		return getData().getConfigurationSection("MySQL");
	}

	@Override
	public void onFileCreation() {
		plugin.saveResource("Config.yml", true);
	}

}
