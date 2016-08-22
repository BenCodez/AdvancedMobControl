package com.Ben12345rocks.AdvancedMobControl.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Object.EntityHandler;

public class MobSpawn implements Listener {

	@SuppressWarnings("unused")
	private static Main plugin;

	// VotingRewards voteReward = VotingRewards.getInstance();

	public MobSpawn(Main plugin) {
		MobSpawn.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		EntityHandler entityHandle = new EntityHandler(event.getEntityType());
		event.getEntity().setMaxHealth(
				entityHandle.creatureSpawn(event.getEntity().getMaxHealth(),
						event.getSpawnReason()));
		event.getEntity().setHealth(1.0);
	}

}