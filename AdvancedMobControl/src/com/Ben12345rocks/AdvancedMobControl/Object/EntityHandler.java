package com.Ben12345rocks.AdvancedMobControl.Object;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.Ben12345rocks.AdvancedCore.AdvancedCoreHook;
import com.Ben12345rocks.AdvancedCore.YML.YMLFile;
import com.Ben12345rocks.AdvancedMobControl.Main;

/**
 * The Class EntityHandler.
 */
public class EntityHandler {
	private HashMap<EntityType, ArrayList<EntityHandle>> entityHandles;
	private Main plugin;

	public EntityHandler() {
		plugin = Main.plugin;

		load();
	}

	public void load() {
		entityHandles = new HashMap<EntityType, ArrayList<EntityHandle>>();
		for (File file : new File(plugin.getDataFolder(), "Entities").listFiles()) {

			if (file.isDirectory()) {
				String world = file.getName();
				ArrayList<EntityHandle> handles = new ArrayList<EntityHandle>();
				for (File entityFile : file.listFiles()) {
					String entityTypeStr = entityFile.getName();
					try {

						YMLFile yml = new YMLFile(entityFile) {

							@Override
							public void onFileCreation() {

							}
						};

						String str = yml.getData().getString("EntityType", "");
						if (!str.isEmpty()) {
							entityTypeStr = str;
						}
						EntityType type = EntityType.valueOf(entityTypeStr);
						handles.add(new EntityHandle(entityFile.getName(), world,
								yml.getData().getConfigurationSection(""), yml));
						entityHandles.put(type, handles);

					} catch (EnumConstantNotPresentException e) {
						AdvancedCoreHook.getInstance().debug(e);
						plugin.getLogger().warning("EntityType " + entityTypeStr + " does not exist!");
					}
				}

			}

		}
	}

	public EntityHandle getHandle(EntityType type, World world, int looting, SpawnReason spawn) {
		String spawnReason = "";
		if (spawn != null) {
			spawnReason = spawn.toString();
		}
		if (entityHandles.get(type) != null) {
			for (EntityHandle handle : entityHandles.get(type)) {
				if (handle.getWorld().equalsIgnoreCase(world.getName())) {
					if (handle.getLooting() == looting) {
						if (spawnReason.equals(handle.getSpawnReason())) {
							return handle;
						}
					}
				}
			}
		}
		if (entityHandles.get(type) != null) {
			for (EntityHandle handle : entityHandles.get(type)) {
				if (handle.getWorld().equalsIgnoreCase("All")) {
					if (handle.getLooting() == looting) {
						if (spawnReason.equals(handle.getSpawnReason())) {
							return handle;
						}
					}
				}
			}
		}
		return null;
	}

}
