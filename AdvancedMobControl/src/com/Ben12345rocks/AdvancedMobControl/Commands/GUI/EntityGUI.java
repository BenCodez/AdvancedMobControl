package com.Ben12345rocks.AdvancedMobControl.Commands.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory.ClickEvent;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventoryButton;
import com.Ben12345rocks.AdvancedCore.Util.Item.ItemBuilder;
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
			BInventoryButton b = new BInventoryButton(new ItemBuilder(Material.STONE)) {

				@Override
				public void onClick(ClickEvent event) {
					openEntityGUI(event.getPlayer(), (EntityHandle) event.getButton().getData().get("Handle"));
				}
			};
			b.addData("Handle", h);
			inv.addButton(b);
		}

		inv.openInventory(player);
	}

	public void openEntityGUI(Player player, EntityHandle handle) {
		BInventory inv = new BInventory("EntityHandle - WIP");

		inv.openInventory(player);
	}
}