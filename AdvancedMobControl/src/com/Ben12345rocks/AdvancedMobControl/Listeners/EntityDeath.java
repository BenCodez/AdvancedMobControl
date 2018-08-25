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
import org.bukkit.metadata.MetadataValue;

import com.Ben12345rocks.AdvancedCore.Rewards.RewardBuilder;
import com.Ben12345rocks.AdvancedCore.UserManager.User;
import com.Ben12345rocks.AdvancedCore.UserManager.UserManager;
import com.Ben12345rocks.AdvancedCore.Util.Item.ItemBuilder;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Object.EntityHandle;

/**
 * The Class EntityDeath.
 */
public class EntityDeath implements Listener {

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
			Player killer = null;
			int looting = 0;
			EntityHandle handle = plugin.getEntityHandler().getHandle(event.getEntityType(),
					event.getEntity().getLocation().getWorld(), looting, null);
			if (handle == null) {
				return;
			}
			boolean spawner = false;
			for (MetadataValue data : event.getEntity().getMetadata("Spawner")) {
				if (data.getOwningPlugin().equals(plugin)) {
					spawner = data.asBoolean();
				}
			}
			if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent damage = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
				if (damage.getDamager() instanceof Player) {
					killer = (Player) damage.getDamager();
					User user = UserManager.getInstance().getUser(killer);
					looting = killer.getInventory().getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
					EntityHandle handle2 = plugin.getEntityHandler().getHandle(event.getEntityType(),
							event.getEntity().getLocation().getWorld(), looting, null);
					if (handle2 != null) {
						handle = handle2;
					}
					if (!spawner) {
						user.giveMoney(handle.getMoney());
					}

					RewardBuilder rewardBuilder = new RewardBuilder(handle.getData(), "Rewards");
					rewardBuilder.getRewardOptions().getPlaceholders().put("spawner", "" + spawner);
					rewardBuilder.send(killer);

				}
			}

			int exp = handle.getExp();
			if (exp >= 0) {
				event.setDroppedExp(exp);
			}

			ArrayList<ItemBuilder> drops = handle.getDrops();
			if (handle.isRemoveDrops()) {
				event.getDrops().clear();

			}

			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for (ItemBuilder build : drops) {
				items.add(build.toItemStack(killer));
			}
			event.getDrops().addAll(items);
		}
	}

}