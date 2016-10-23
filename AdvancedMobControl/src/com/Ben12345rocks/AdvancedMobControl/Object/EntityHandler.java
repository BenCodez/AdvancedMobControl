package com.Ben12345rocks.AdvancedMobControl.Object;

import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.Ben12345rocks.AdvancedCore.Utils;
import com.Ben12345rocks.AdvancedCore.Objects.RewardHandler;
import com.Ben12345rocks.AdvancedCore.Objects.User;
import com.Ben12345rocks.AdvancedMobControl.Config.Config;
import com.Ben12345rocks.AdvancedMobControl.Config.ConfigEntity;

/**
 * The Class EntityHandler.
 */
public class EntityHandler {

	/** The entity type. */
	private EntityType entityType;

	/**
	 * Instantiates a new entity handler.
	 *
	 * @param entityType
	 *            the entity type
	 */
	public EntityHandler(EntityType entityType) {
		this.entityType = entityType;
		ConfigEntity.getInstance().setup(this.entityType.toString());
	}

	/**
	 * Adds the kill.
	 *
	 * @param user
	 *            the user
	 */
	public void addKill(User user) {
		user.setPluginData(
				"PriceReduction." + entityType.toString(),
				user.getPluginData().getInt(
						"PriceReduction." + entityType.toString()) + 1);

	}

	/**
	 * Creature spawn.
	 *
	 * @param health
	 *            the health
	 * @param reason
	 *            the reason
	 * @return the double
	 */
	public double creatureSpawn(double health, SpawnReason reason) {
		double reasonHealth = ConfigEntity.getInstance().getHealth(
				entityType.toString(), reason.toString());
		if (reasonHealth == 0) {
			ConfigEntity.getInstance().setHealth(entityType.toString(),
					reason.toString(), health);
		} else {
			health = reasonHealth;
		}

		return health;
	}

	/**
	 * Gets the exp.
	 *
	 * @param defaultExp
	 *            the default exp
	 * @return the exp
	 */
	public int getExp(int defaultExp) {
		int exp = ConfigEntity.getInstance().getExp(entityType.toString());
		if (exp == 0) {
			ConfigEntity.getInstance()
					.setExp(entityType.toString(), defaultExp);
			exp = defaultExp;
		}
		if (exp < 0) {
			return 0;
		}
		return exp;
	}

	/**
	 * Removes the kills.
	 *
	 * @param user
	 *            the user
	 */
	public void removeKills(User user) {
		Set<String> mobs = user.getPluginData()
				.getConfigurationSection("PriceReduction").getKeys(false);
		if (mobs != null) {
			for (String mob : mobs) {
				if (!mob.equals(entityType.toString())) {
					int mobKills = user.getPluginData().getInt(
							"PriceReduction." + mob);
					if (mobKills > 0) {
						user.setPluginData("PriceReduction." + mob,
								mobKills - 1);
					}
				}
			}
		}
	}

	/**
	 * Run rewards.
	 *
	 * @param user
	 *            the user
	 * @param damage
	 *            the damage
	 */
	public void runRewards(User user, String damage) {
		int mobKills = user.getPluginData().getInt(
				"PriceReduction." + entityType.toString());
		// Main.plugin.debug("MobKills: " + mobKills);
		double percent = (double) (mobKills - 1)
				/ Config.getInstance().getMaxMobs();
		// Main.plugin.debug("PrevPercent: " + percent);

		// Main.plugin.debug("PrevPercent2: " + percent);
		if (percent > 1) {
			percent = 1;
		} else if (percent < 0) {
			percent = 0;
		}

		percent = (1 - percent);

		// Main.plugin.debug("Percent: " + percent);

		double money = ConfigEntity.getInstance().getMoney(
				entityType.toString());
		money = money * percent;
		user.giveMoney(money);
		// Main.plugin.debug("Money: " + money);
		if (money != 0) {
			user.sendMessage(Config
					.getInstance()
					.getFormatMoney()
					.replace("%Money%",
							Utils.getInstance().roundDecimals(money, 2))
					.replace("%Entity%", entityType.toString()));
		}

		for (String reward : ConfigEntity.getInstance().getRewards(
				entityType.toString())) {
			RewardHandler.getInstance().giveReward(user, reward, true);
		}

		if (damage != null) {
			money = ConfigEntity.getInstance().getMoney(entityType.toString(),
					damage);
			money = money * percent;
			user.giveMoney(money);
			// Main.plugin.debug("SpecificDamageMoney: " + money);
			if (money != 0) {
				user.sendMessage(Config
						.getInstance()
						.getFormatMoneyDamage()
						.replace("%Money%",
								Utils.getInstance().roundDecimals(money, 2))
						.replace("%Entity%", entityType.toString())
						.replace("%Damage%", damage));
			}
			for (String reward : ConfigEntity.getInstance().getRewards(
					entityType.toString(), damage)) {
				RewardHandler.getInstance().giveReward(user, reward, true);
			}

		}

		removeKills(user);
	}
}
