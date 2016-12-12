package com.Ben12345rocks.AdvancedMobControl.Listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.Ben12345rocks.AdvancedCore.Objects.User;
import com.Ben12345rocks.AdvancedCore.UserManager.UserManager;
import com.Ben12345rocks.AdvancedCore.Util.Item.ItemBuilder;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Config.ConfigEntity;
import com.Ben12345rocks.AdvancedMobControl.Object.EntityHandler;

/**
 * The Class EntityDeath.
 */
public class EntityDeath implements Listener {

	/** The plugin. */
	@SuppressWarnings("unused")
	private static Main plugin;

	// VotingRewards voteReward = VotingRewards.getInstance();

	/**
	 * Instantiates a new entity death.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public EntityDeath(Main plugin) {
		EntityDeath.plugin = plugin;
	}

	/**
	 * On creature death.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			EntityHandler handle = new EntityHandler(event.getEntityType());
			event.setDroppedExp(handle.getExp(event.getDroppedExp()));
			int looting = 0;
			if (ConfigEntity.getInstance().getDrops(event.getEntity().getType().toString(), looting).size() != 0) {
				event.getDrops().clear();
				for (String item : ConfigEntity.getInstance().getDrops(event.getEntity().getType().toString(),
						looting)) {
					event.getDrops()
							.add(new ItemBuilder(ConfigEntity.getInstance()
									.getDropsItem(event.getEntity().getType().toString(), looting, item))
											.toItemStack());
				}
			}
			if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent damage = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
				if (damage.getDamager() instanceof Player) {
					Player player = (Player) damage.getDamager();
					User user = UserManager.getInstance().getUser(player);
					handle.addKill(user);
					handle.runRewards(user, damage.getCause().toString());
					looting = player.getInventory().getItemInMainHand()
							.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
					if (looting != 0) {
						if (ConfigEntity.getInstance().getDrops(event.getEntity().getType().toString(), looting)
								.size() != 0) {
							event.getDrops().clear();
							for (String item : ConfigEntity.getInstance()
									.getDrops(event.getEntity().getType().toString(), looting)) {
								event.getDrops()
										.add(new ItemBuilder(ConfigEntity.getInstance()
												.getDropsItem(event.getEntity().getType().toString(), looting, item))
														.toItemStack());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * On creature spawn.
	 *
	 * @param event
	 *            the event
	 */
	/*
	 * @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	 * public void onCreatureDamage(EntityDamageByEntityEvent event) { if
	 * (event.getDamager() instanceof Player && (event.getEntity() instanceof
	 * LivingEntity)) { LivingEntity entity = (LivingEntity) event.getEntity();
	 * if (event.getDamage() >= entity.getHealth()) { Player player = (Player)
	 * event.getDamager(); EntityHandler handle = new
	 * EntityHandler(event.getEntityType()); User user = new User(plugin,
	 * player); handle.addKill(user); handle.runRewards(user,
	 * event.getCause().toString()); } } }
	 */

}