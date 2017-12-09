package com.Ben12345rocks.AdvancedMobControl.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Object.EntityHandle;

/**
 * The Class MobSpawn.
 */
public class MobSpawn implements Listener {

	/** The plugin. */
	private static Main plugin;

	/**
	 * Instantiates a new mob spawn.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public MobSpawn(Main plugin) {
		MobSpawn.plugin = plugin;
	}

	/**
	 * On creature spawn.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		EntityHandle handle = plugin.getEntityHandler().getHandle(event.getEntityType(), event.getLocation().getWorld(),
				-1, event.getSpawnReason());

		//double nHealth = plugin.getAttributeHandle().getMaxHealth(event.getEntity());
		double health = handle.getHealth();
		if (health >= 0) {
			plugin.getAttributeHandle().setMaxHealth(event.getEntity(), health);
			event.getEntity().setHealth(health);
		}

	}

}