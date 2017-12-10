package com.Ben12345rocks.AdvancedMobControl.Commands.GUI;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory.ClickEvent;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventoryButton;
import com.Ben12345rocks.AdvancedCore.Util.Item.ItemBuilder;
import com.Ben12345rocks.AdvancedCore.Util.Misc.ArrayUtils;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.InputMethod;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.ValueRequestBuilder;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.Listeners.Listener;
import com.Ben12345rocks.AdvancedMobControl.Main;

public class EntityGUI {
	static EntityGUI instance = new EntityGUI();

	/**
	 * Gets the single instance of Commands.
	 *
	 * @return single instance of Commands
	 */
	public static EntityGUI getInstance() {
		return instance;
	}

	/** The plugin. */
	Main plugin = Main.plugin;

	/**
	 * Instantiates a new commands.
	 */
	private EntityGUI() {
	}

	public void openGUI(Player player) {
		BInventory inv = new BInventory("Worlds");

		inv.addButton(new BInventoryButton(
				new ItemBuilder(Material.GRASS, 1).setName("All").addLoreLine("Editing here will effect all worlds")
						.addLoreLine("World specific configurations get priority though")) {

			@Override
			public void onClick(ClickEvent event) {
				openGUIWorld(event.getPlayer(), "All");
			}

		});

		for (World world : Bukkit.getWorlds()) {
			final String worldName = world.getName();
			inv.addButton(new BInventoryButton(new ItemBuilder(Material.GRASS, 1).setName(world.getName())) {

				@Override
				public void onClick(ClickEvent event) {
					openGUIWorld(event.getPlayer(), worldName);
				}
			});
		}

		inv.openInventory(player);
	}

	@SuppressWarnings("deprecation")
	public void openGUIWorld(Player player, String world) {
		BInventory inv = new BInventory("World Entities: " + world);

		for (String entity : plugin.getEntityHandler().getEntityTypes(world)) {
			inv.addButton(new BInventoryButton(new ItemBuilder(Material.GRASS, 1).setName(entity)) {

				@Override
				public void onClick(ClickEvent event) {
					// editEntity(event.getPlayer())
				}
			});
		}

		inv.openInventory(player);

		ArrayList<String> entities = plugin.getEntityHandler().getEntityTypes(world);
		LinkedHashMap<String, ItemStack> map = new LinkedHashMap<String, ItemStack>();
		for (String entity : entities) {
			map.put(entity, new ItemBuilder(Material.STONE, 1).setName(entity).toItemStack());
		}
		map.put("Add custom", new ItemBuilder(Material.MOB_SPAWNER, 1).setName("&cAdd custom")
				.addLoreLine("Click me to add an entity to configure").toItemStack());

		new ValueRequestBuilder(map, new Listener<String>() {

			@Override
			public void onInput(Player player, String str) {
				if (str.equalsIgnoreCase("Add custom")) {
					// add entity
					ArrayList<String> entities = new ArrayList<String>();
					for (EntityType type : EntityType.values()) {
						entities.add(type.toString());
					}
					new ValueRequestBuilder(new Listener<String>() {

						@Override
						public void onInput(Player player, String str) {
							openEntityGUI(player, str);
						}
					}, ArrayUtils.getInstance().convert(entities)).request(player);
					;
				} else {
					// open entity gui edit
					openEntityGUI(player, str);
				}
			}

		}).usingMethod(InputMethod.INVENTORY).request(player);

	}

	public void openEntityGUI(Player player, String str) {
		
	}
}