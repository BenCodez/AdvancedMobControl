package com.Ben12345rocks.AdvancedMobControl.Object;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
		File folder = new File(plugin.getDataFolder(), "Worlds");
		if (!folder.exists()) {
			folder.mkdirs();
			try {
				folder.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File file : folder.listFiles()) {

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

	public ArrayList<String> getEntityTypes(String world) {

		ArrayList<String> entities = new ArrayList<String>();
		for (Entry<EntityType, ArrayList<EntityHandle>> entry : entityHandles.entrySet()) {
			for (EntityHandle handle : entry.getValue()) {
				if (handle.getWorld().equalsIgnoreCase(world)) {
					if (!entities.contains(entry.getKey().toString())) {
						entities.add(entry.getKey().toString());
					}
				}
			}
		}
		return entities;
	}

}
