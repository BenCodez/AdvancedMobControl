package com.Ben12345rocks.AdvancedMobControl.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Object.EntityHandle;

/**
 * The Class MobSpawn.
 */
public class MobClicked implements Listener {

	/** The plugin. */
	@SuppressWarnings("unused")
	private static Main plugin;

	// VotingRewards voteReward = VotingRewards.getInstance();

	/**
	 * Instantiates a new mob spawn.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public MobClicked(Main plugin) {
		MobClicked.plugin = plugin;
	}

	/**
	 * On creature spawn.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureClick(PlayerInteractEntityEvent event) {
	//	Player player = event.getPlayer();
		Entity entity = event.getRightClicked();

		EntityHandle handle = plugin.getEntityHandler().getHandle(entity.getType(), entity.getLocation().getWorld(), -1,
				null);

		if (handle.isRightClickDisabled()) {
			event.setCancelled(true);
		}
	}

}