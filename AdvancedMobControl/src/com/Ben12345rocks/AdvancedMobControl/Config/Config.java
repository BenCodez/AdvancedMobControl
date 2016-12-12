/*
 *
 */
package com.Ben12345rocks.AdvancedMobControl.Config;

import java.io.File;

import com.Ben12345rocks.AdvancedCore.YML.YMLFile;
import com.Ben12345rocks.AdvancedMobControl.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class Config.
 */
public class Config extends YMLFile {

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

	/**
	 * Instantiates a new config.
	 */
	public Config() {
		super(new File(Main.plugin.getDataFolder(), "Config.yml"));
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
	 * Gets the max mobs.
	 *
	 * @return the max mobs
	 */
	public int getMaxMobs() {
		return getData().getInt("MaxMobs", 20);
	}

	@Override
	public void onFileCreation() {
		plugin.saveResource("Config.yml", false);
	}

}
