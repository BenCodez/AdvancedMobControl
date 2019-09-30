package com.Ben12345rocks.AdvancedMobControl.Listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

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

		if (handle != null) {
			// double nHealth = plugin.getAttributeHandle().getMaxHealth(event.getEntity());
			double health = handle.getHealth();
			if (health >= 0) {
				event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
				event.getEntity().setHealth(health);
			} else {
				event.getEntity().setHealth(0);
				event.getEntity().remove();
			}
		}

		if (event.getSpawnReason().equals(SpawnReason.SPAWNER)) {
			event.getEntity().setMetadata("Spawner", new MetadataValue() {

				@Override
				public Object value() {
					return true;
				}

				@Override
				public void invalidate() {
				}

				@Override
				public Plugin getOwningPlugin() {
					return plugin;
				}

				@Override
				public String asString() {
					return "true";
				}

				@Override
				public short asShort() {
					return 0;
				}

				@Override
				public long asLong() {
					return 0;
				}

				@Override
				public int asInt() {
					return 0;
				}

				@Override
				public float asFloat() {
					return 0;
				}

				@Override
				public double asDouble() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public byte asByte() {
					return 0;
				}

				@Override
				public boolean asBoolean() {
					return true;
				}
			});
		}
	}

}