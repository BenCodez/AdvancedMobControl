package com.Ben12345rocks.AdvancedMobControl.Listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.Ben12345rocks.AdvancedCore.Objects.User;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Object.EntityHandler;

public class EntityDeath implements Listener {

	private static Main plugin;

	// VotingRewards voteReward = VotingRewards.getInstance();

	public EntityDeath(Main plugin) {
		EntityDeath.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player
				&& (event.getEntity() instanceof LivingEntity)) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (event.getDamage() >= entity.getHealth()) {
				Player player = (Player) event.getDamager();
				EntityHandler handle = new EntityHandler(event.getEntityType());
				User user = new User(plugin, player);
				handle.addKill(user);
				handle.runRewards(user, event.getCause().toString());
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof LivingEntity) {

			EntityHandler handle = new EntityHandler(event.getEntityType());
			event.setDroppedExp(handle.getExp(event.getDroppedExp()));

		}
	}

}