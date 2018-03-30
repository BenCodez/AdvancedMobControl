package com.Ben12345rocks.AdvancedMobControl.Object;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.Ben12345rocks.AdvancedMobControl.Main;

/**
 * The Class EntityHandler.
 */
public class EntityHandler {
	private ArrayList<EntityHandle> entityHandles;
	private Main plugin;

	public EntityHandler() {
		plugin = Main.plugin;

		load();
	}

	public void load() {
		entityHandles = new ArrayList<EntityHandle>();
		File folder = new File(plugin.getDataFolder(), "Entities");
		if (!folder.exists()) {
			folder.mkdirs();
			try {
				folder.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File file : folder.listFiles()) {

			EntityHandle handle = new EntityHandle(file);
			entityHandles.add(handle);
		}
	}

	public EntityHandle getHandle(EntityType entityType, World world, int looting, SpawnReason reason) {
		ArrayList<EntityHandle> matchedHandles = new ArrayList<EntityHandle>();

		for (EntityHandle h : entityHandles) {
			// entity matches
			if (h.getType().equalsIgnoreCase(entityType.toString())) {
				// world check
				if (h.getWorld().equals("") || h.getWorld().equalsIgnoreCase(world.getName())) {
					// check looting
					if (h.getLooting() == -1 || h.getLooting() == looting) {
						// check spawn reason
						if (reason == null || h.getSpawnReason().equalsIgnoreCase(reason.toString())) {
							matchedHandles.add(h);
						}
					}
				}
			}
		}

		EntityHandle highestPriority = null;
		for (EntityHandle h : matchedHandles) {
			if (highestPriority == null || highestPriority.getPriority() < h.getPriority()) {
				highestPriority = h;
			}
		}

		return highestPriority;
	}

	public ArrayList<EntityHandle> getEntityHandles() {
		return entityHandles;
	}
}
