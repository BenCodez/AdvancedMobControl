package com.Ben12345rocks.AdvancedMobControl.Listeners;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.Ben12345rocks.AdvancedCore.Objects.User;
import com.Ben12345rocks.AdvancedCore.UserManager.UserManager;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Object.EntityHandle;

/**
 * The Class EntityDeath.
 */
public class EntityDeath implements Listener {

	/** The plugin. */
	@SuppressWarnings("unused")
	private static Main plugin;

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
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			int looting = 0;
			EntityHandle handle = plugin.getEntityHandler().getHandle(event.getEntityType(),
					event.getEntity().getLocation().getWorld(), looting, null);
			if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent damage = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
				if (damage.getDamager() instanceof Player) {
					Player player = (Player) damage.getDamager();
					User user = UserManager.getInstance().getUser(player);
					// handle.addKill(user);
					// handle.runRewards(user, damage.getCause().toString());
					handle = plugin.getEntityHandler().getHandle(event.getEntityType(),
							event.getEntity().getLocation().getWorld(), looting, null);
					looting = player.getInventory().getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);

					if (handle != null) {
						user.giveMoney(handle.getMoney());

					}
				}
			}

			if (handle == null) {
				return;
			}
			
			int exp = handle.getExp();
			if (exp >= 0) {
				event.setDroppedExp(exp);
			}

			ArrayList<ItemStack> drops = handle.getDrops();
			if (handle.removeDrops()) {
				event.getDrops().clear();

			}
			event.getDrops().addAll(drops);
		}
	}

}