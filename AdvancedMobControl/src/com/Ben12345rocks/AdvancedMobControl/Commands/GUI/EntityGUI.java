package com.Ben12345rocks.AdvancedMobControl.Commands.GUI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.ClickType;

import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory.ClickEvent;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventoryButton;
import com.Ben12345rocks.AdvancedCore.Util.Item.ItemBuilder;
import com.Ben12345rocks.AdvancedCore.Util.Misc.ArrayUtils;
import com.Ben12345rocks.AdvancedCore.Util.Misc.MiscUtils;
import com.Ben12345rocks.AdvancedCore.Util.Misc.StringUtils;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.InputMethod;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.ValueRequestBuilder;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.Listeners.BooleanListener;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.Listeners.NumberListener;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.Listeners.StringListener;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Object.EntityHandle;

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
		BInventory inv = new BInventory("EntityHandles");

		for (EntityHandle h : plugin.getEntityHandler().getEntityHandles()) {
			BInventoryButton b = new BInventoryButton(
					new ItemBuilder(Material.STONE).setName(h.getFile().getdFile().getName())) {

				@Override
				public void onClick(ClickEvent event) {
					openEntityGUI(event.getPlayer(), (EntityHandle) event.getButton().getData().get("Handle"));
				}
			};
			b.addData("Handle", h);
			inv.addButton(b);
		}

		inv.addButton(new BInventoryButton(new ItemBuilder(Material.PAPER).setName("&cCreate")
				.addLoreLine("&cCreate entity handle file, can be any name").addLoreLine("&cSet EntityType after")) {

			@Override
			public void onClick(ClickEvent event) {
				new ValueRequestBuilder(new StringListener() {

					@Override
					public void onInput(Player player, String value) {
						plugin.getEntityHandler().create(value);
						openGUI(player);
					}
				}, new String[] {}).usingMethod(InputMethod.CHAT).allowCustomOption(true).currentValue("")
						.request(event.getPlayer());
			}
		});

		inv.openInventory(player);
	}

	public void openEntityGUI(Player player, EntityHandle handle) {
		BInventory inv = new BInventory("EntityHandle: " + handle.getFile().getdFile().getName());

		LinkedHashMap<String, ArrayList<String>> stringOptions = new LinkedHashMap<String, ArrayList<String>>();

		ArrayList<String> entityTypes = new ArrayList<String>();
		for (EntityType type : EntityType.values()) {
			entityTypes.add(type.toString());
		}
		stringOptions.put("EntityType", entityTypes);

		stringOptions.put("World", MiscUtils.getInstance().getWorldNames());

		ArrayList<String> spawnReasons = new ArrayList<String>();
		for (SpawnReason type : SpawnReason.values()) {
			spawnReasons.add(type.toString());
		}
		stringOptions.put("SpawnReason", spawnReasons);

		for (final Entry<String, ArrayList<String>> entry : stringOptions.entrySet()) {
			inv.addButton(new BInventoryButton(new ItemBuilder(Material.PAPER)
					.setName("&c" + entry.getKey() + " = " + handle.getData().getString(entry.getKey()))) {

				@Override
				public void onClick(ClickEvent event) {
					new ValueRequestBuilder(new StringListener() {

						@Override
						public void onInput(Player player, String value) {
							handle.set(entry.getKey(), value);
							player.sendMessage(
									StringUtils.getInstance().colorize("&cSetting " + entry.getKey() + " to " + value));
						}
					}, ArrayUtils.getInstance().convert(entry.getValue())).allowCustomOption(true)
							.currentValue(handle.getData().getString(entry.getKey(), "")).request(event.getPlayer());

				}
			});
		}

		ArrayList<String> booleanOptions = new ArrayList<String>();
		booleanOptions.add("DisableRightClick");
		booleanOptions.add("RemoveDrops");

		for (final String key : booleanOptions) {
			inv.addButton(new BInventoryButton(
					new ItemBuilder(Material.STONE).setName("&c" + key + " = " + handle.getData().getBoolean(key))) {

				@Override
				public void onClick(ClickEvent event) {
					new ValueRequestBuilder(new BooleanListener() {

						@Override
						public void onInput(Player player, boolean value) {
							handle.set(key, value);
							player.sendMessage(StringUtils.getInstance().colorize("&cSetting " + key + " to " + value));
						}
					}).currentValue("" + handle.getData().getBoolean(key)).request(event.getPlayer());
				}
			});
		}

		ArrayList<String> intOptions = new ArrayList<String>();
		intOptions.add("Health");
		intOptions.add("Looting");
		intOptions.add("Money");
		intOptions.add("Exp");
		intOptions.add("Priority");

		for (final String key : intOptions) {
			inv.addButton(new BInventoryButton(
					new ItemBuilder(Material.STONE).setName("&c" + key + " = " + handle.getData().getInt(key))) {

				@Override
				public void onClick(ClickEvent event) {
					new ValueRequestBuilder(new NumberListener() {

						@Override
						public void onInput(Player player, Number value) {
							handle.set(key, value.intValue());
							player.sendMessage(
									StringUtils.getInstance().colorize("&cSetting " + key + " to " + value.intValue()));
						}
					}, new Number[] { 0, 1, 2, 3, 10, 50, 100, 1000 }).allowCustomOption(true)
							.currentValue("" + handle.getData().getInt(key)).request(event.getPlayer());
				}
			});
		}

		inv.addButton(new BInventoryButton(new ItemBuilder(Material.BONE).setName("&cDrops")) {

			@Override
			public void onClick(ClickEvent event) {
				BInventory dInv = new BInventory("Drops");
				dInv.addButton(
						new BInventoryButton(new ItemBuilder(Material.PAPER).setName("&cRight click drosp to remove")) {

							@Override
							public void onClick(ClickEvent arg0) {
								
							}
						});
				for (ItemBuilder item : handle.getDrops()) {
					dInv.addButton(new BInventoryButton(item) {

						@Override
						public void onClick(ClickEvent event) {
							if (event.getClick().equals(ClickType.RIGHT)) {
								handle.removeDrop(item);
							}
						}
					});
				}

			}
		});

		inv.openInventory(player);

	}
}