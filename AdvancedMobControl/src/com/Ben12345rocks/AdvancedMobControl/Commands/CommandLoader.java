package com.Ben12345rocks.AdvancedMobControl.Commands;

import java.util.ArrayList;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.Ben12345rocks.AdvancedCore.Utils;
import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedCore.Objects.User;
import com.Ben12345rocks.AdvancedCore.Util.AnvilInventory.AInventory;
import com.Ben12345rocks.AdvancedCore.Util.AnvilInventory.AInventory.AnvilClickEventHandler;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventoryButton;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Config.ConfigEntity;

/**
 * The Class CommandLoader.
 */
public class CommandLoader {

	/** The plugin. */
	public Main plugin = Main.plugin;

	/**
	 * Instantiates a new command loader.
	 */
	public CommandLoader() {
		loadCommands();
	}

	/**
	 * Load commands.
	 */
	public void loadCommands() {
		plugin.advancedMobControlCommands = new ArrayList<CommandHandler>();
		plugin.advancedMobControlCommands.add(new CommandHandler(
				new String[] { "Reload" }, "AdvancedMobControl.Reload",
				"Reload the plugin") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				plugin.reload();
				sender.sendMessage(Utils.getInstance().colorize(
						"&c" + plugin.getName() + " v"
								+ plugin.getDescription().getVersion()
								+ " reloaded"));
			}
		});
		plugin.advancedMobControlCommands.add(new CommandHandler(
				new String[] { "Help" }, "AdvancedMobControl.Help",
				"View this page") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				ArrayList<TextComponent> msg = new ArrayList<TextComponent>();
				msg.add(Utils.getInstance().stringToComp(
						"&c" + plugin.getName() + " help"));
				for (CommandHandler cmdHandle : plugin.advancedMobControlCommands) {
					msg.add(cmdHandle.getHelpLine("/advancedmobcontrol"));
				}
				if (sender instanceof Player) {
					new User(plugin, (Player) sender).sendJson(msg);
				} else {
					sender.sendMessage(Utils.getInstance().convertArray(
							Utils.getInstance().comptoString(msg)));
				}
			}
		});
		plugin.advancedMobControlCommands.add(new CommandHandler(
				new String[] { "Perms" }, "AdvancedMobControl.Perms",
				"View permissions list") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				ArrayList<String> msg = new ArrayList<String>();
				msg.add("&c" + plugin.getName() + " permissions");
				for (CommandHandler cmdHandle : plugin.advancedMobControlCommands) {
					msg.add(cmdHandle.getPerm());
				}
				if (sender instanceof Player) {
					new User(plugin, (Player) sender).sendMessage(msg);
				} else {
					sender.sendMessage(Utils.getInstance().convertArray(msg));
				}
			}
		});

		plugin.advancedMobControlCommands.add(new CommandHandler(new String[] {
				"Entity", "(Entity)" }, "AdvancedMobControl.Entity",
				"Open GUI to edit entities") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				if (!(sender instanceof Player)) {
					return;
				}
				BInventory inv = new BInventory("EntityGUI: " + args[1]);

				ArrayList<String> lore = new ArrayList<String>();
				lore.add("&cCurrently: &c&l"
						+ ConfigEntity.getInstance().getMoney(args[1]));

				inv.addButton(0, new BInventoryButton("SetMoney", Utils
						.getInstance().convertArray(lore), new ItemStack(
						Material.GOLDEN_APPLE)) {

					@Override
					public void onClick(InventoryClickEvent event) {
						if (!(event.getWhoClicked() instanceof Player)) {
							return;
						}
						String entity = event.getInventory().getTitle()
								.split(" ")[1];
						Player player = (Player) event.getWhoClicked();
						AInventory gui = new AInventory(player,
								new AnvilClickEventHandler() {
									@Override
									public void onAnvilClick(
											AInventory.AnvilClickEvent event) {
										Player player = event.getPlayer();
										if (event.getSlot() == AInventory.AnvilSlot.OUTPUT) {

											event.setWillClose(true);
											event.setWillDestroy(true);

											if (Utils.getInstance().isInt(
													event.getName())) {
												ConfigEntity
														.getInstance()
														.setMoney(
																entity,
																Integer.parseInt(event
																		.getName()));
												player.sendMessage(Utils
														.getInstance()
														.colorize("&cMoney set"));
											} else {
												player.sendMessage(Utils
														.getInstance()
														.colorize(
																"&cError on: "
																		+ event.getName()
																		+ " Must be a number!"));
											}

										} else {
											event.setWillClose(false);
											event.setWillDestroy(false);
										}
									}
								});

						ItemStack item = new ItemStack(Material.NAME_TAG);
						item = Utils.getInstance().setName(
								item,
								""
										+ ConfigEntity.getInstance().getMoney(
												entity));
						ArrayList<String> lore = new ArrayList<String>();
						lore.add("&cRename item and take out to set value");
						lore.add("&cDoes not cost exp");
						item = Utils.getInstance().addLore(item, lore);

						gui.setSlot(AInventory.AnvilSlot.INPUT_LEFT, item);

						gui.open();

					}
				});

				inv.addButton(1, new BInventoryButton("SetMoneyDamage",
						new String[0], new ItemStack(Material.GOLDEN_APPLE)) {

					@Override
					public void onClick(InventoryClickEvent event) {
						if (!(event.getWhoClicked() instanceof Player)) {
							return;
						}
						Player player = (Player) event.getWhoClicked();
						String entity = event.getInventory().getTitle()
								.split(" ")[1];
						BInventory inv = new BInventory("EntityGUI: " + entity);
						int count = 0;
						for (DamageCause damage : DamageCause.values()) {
							if (count < 54) {
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("&cCurrently: &c&l"
										+ ConfigEntity.getInstance().getMoney(
												entity, damage.toString()));

								inv.addButton(count, new BInventoryButton(
										damage.toString(), Utils.getInstance()
												.convertArray(lore),
										new ItemStack(Material.STONE)) {

									@Override
									public void onClick(
											InventoryClickEvent event) {
										AInventory gui = new AInventory(player,
												new AnvilClickEventHandler() {

													@Override
													public void onAnvilClick(
															AInventory.AnvilClickEvent event) {
														Player player = event
																.getPlayer();
														if (event.getSlot() == AInventory.AnvilSlot.OUTPUT) {

															event.setWillClose(true);
															event.setWillDestroy(true);

															if (Utils
																	.getInstance()
																	.isInt(event
																			.getName())) {
																ConfigEntity
																		.getInstance()
																		.setMoney(
																				entity,
																				damage.toString(),

																				Integer.parseInt(event
																						.getName()));
																player.sendMessage(Utils
																		.getInstance()
																		.colorize(
																				"&cMoney set"));
															} else {
																player.sendMessage(Utils
																		.getInstance()
																		.colorize(
																				"&cError on: "
																						+ event.getName()
																						+ " Must be a number!"));
															}

														} else {
															event.setWillClose(false);
															event.setWillDestroy(false);
														}
													}
												});

										ItemStack item = new ItemStack(
												Material.NAME_TAG);
										item = Utils
												.getInstance()
												.setName(
														item,
														""
																+ ConfigEntity
																		.getInstance()
																		.getMoney(
																				entity,
																				damage.toString()));
										ArrayList<String> lore = new ArrayList<String>();
										lore.add("&cRename item and take out to set value");
										lore.add("&cDoes not cost exp");
										item = Utils.getInstance().addLore(
												item, lore);

										gui.setSlot(
												AInventory.AnvilSlot.INPUT_LEFT,
												item);

										gui.open();

									}
								});
								count++;
							}

						}
						inv.openInventory(player);

					}

				});

				inv.addButton(2, new BInventoryButton("SetHealth",
						new String[0], new ItemStack(Material.GOLDEN_APPLE)) {

					@Override
					public void onClick(InventoryClickEvent event) {
						if (!(event.getWhoClicked() instanceof Player)) {
							return;
						}
						Player player = (Player) event.getWhoClicked();
						String entity = event.getInventory().getTitle()
								.split(" ")[1];
						BInventory inv = new BInventory("EntityGUI: " + entity);
						int count = 0;
						for (SpawnReason spawnReason : SpawnReason.values()) {
							if (count < 54) {
								ArrayList<String> lore = new ArrayList<String>();
								lore.add("&cCurrently: &c&l"
										+ ConfigEntity.getInstance().getHealth(
												entity, spawnReason.toString()));

								inv.addButton(
										count,
										new BInventoryButton(spawnReason
												.toString(), Utils
												.getInstance().convertArray(
														lore), new ItemStack(
												Material.STONE)) {

											@Override
											public void onClick(
													InventoryClickEvent event) {
												AInventory gui = new AInventory(
														player,
														new AnvilClickEventHandler() {

															@Override
															public void onAnvilClick(
																	AInventory.AnvilClickEvent event) {
																Player player = event
																		.getPlayer();
																if (event
																		.getSlot() == AInventory.AnvilSlot.OUTPUT) {

																	event.setWillClose(true);
																	event.setWillDestroy(true);

																	if (Utils
																			.getInstance()
																			.isInt(event
																					.getName())) {
																		ConfigEntity
																				.getInstance()
																				.setHealth(
																						entity,
																						spawnReason
																								.toString(),
																						Integer.parseInt(event
																								.getName()));
																		player.sendMessage(Utils
																				.getInstance()
																				.colorize(
																						"&cHealth set"));
																	} else {
																		player.sendMessage(Utils
																				.getInstance()
																				.colorize(
																						"&cError on: "
																								+ event.getName()
																								+ " Must be a number!"));
																	}

																} else {
																	event.setWillClose(false);
																	event.setWillDestroy(false);
																}
															}
														});

												ItemStack item = new ItemStack(
														Material.NAME_TAG);
												item = Utils
														.getInstance()
														.setName(
																item,
																""
																		+ ConfigEntity
																				.getInstance()
																				.getHealth(
																						entity,
																						spawnReason
																								.toString()));
												ArrayList<String> lore = new ArrayList<String>();
												lore.add("&cRename item and take out to set value");
												lore.add("&cDoes not cost exp");
												item = Utils.getInstance()
														.addLore(item, lore);

												gui.setSlot(
														AInventory.AnvilSlot.INPUT_LEFT,
														item);

												gui.open();

											}
										});
								count++;
							}

						}
						inv.openInventory(player);

					}
				});

				lore = new ArrayList<String>();
				lore.add("&cCurrently: &c&l"
						+ ConfigEntity.getInstance().getExp(args[1]));

				inv.addButton(3, new BInventoryButton("SetEXP", Utils
						.getInstance().convertArray(lore), new ItemStack(
						Material.EXP_BOTTLE)) {

					@Override
					public void onClick(InventoryClickEvent event) {
						if (!(event.getWhoClicked() instanceof Player)) {
							return;
						}
						String entity = event.getInventory().getTitle()
								.split(" ")[1];
						Player player = (Player) event.getWhoClicked();
						AInventory gui = new AInventory(player,
								new AnvilClickEventHandler() {
									@Override
									public void onAnvilClick(
											AInventory.AnvilClickEvent event) {
										Player player = event.getPlayer();
										if (event.getSlot() == AInventory.AnvilSlot.OUTPUT) {

											event.setWillClose(true);
											event.setWillDestroy(true);

											if (Utils.getInstance().isInt(
													event.getName())) {
												ConfigEntity
														.getInstance()
														.setExp(entity,
																Integer.parseInt(event
																		.getName()));
												player.sendMessage(Utils
														.getInstance()
														.colorize("&cEXP set"));
											} else {
												player.sendMessage(Utils
														.getInstance()
														.colorize(
																"&cError on: "
																		+ event.getName()
																		+ " Must be a number!"));
											}

										} else {
											event.setWillClose(false);
											event.setWillDestroy(false);
										}
									}
								});

						ItemStack item = new ItemStack(Material.NAME_TAG);
						item = Utils.getInstance().setName(item,
								"" + ConfigEntity.getInstance().getExp(entity));
						ArrayList<String> lore = new ArrayList<String>();
						lore.add("&cRename item and take out to set value");
						lore.add("&cDoes not cost exp");
						item = Utils.getInstance().addLore(item, lore);

						gui.setSlot(AInventory.AnvilSlot.INPUT_LEFT, item);

						gui.open();

					}
				});

				inv.openInventory((Player) sender);

			}
		});
	}
	/*
	 * AInventory gui = new AInventory(player, new AnvilClickEventHandler() {
	 * 
	 * @Override public void onAnvilClick( AnvilGUI.AnvilClickEvent event) { if
	 * (event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
	 * event.setWillClose(true); event.setWillDestroy(true);
	 * 
	 * player.sendMessage(event.getName()); } else { event.setWillClose(false);
	 * event.setWillDestroy(false); } }
	 * 
	 * @Override public void onAnvilClick( AnvilClickEvent event) {
	 * 
	 * 
	 * } });
	 * 
	 * gui.setSlot(AnvilSlot.INPUT_LEFT, )); Utils.getInstance().setName(new
	 * ItemStack(Material.NAME_TAG, ConfigEntity.getInstance().getHealth(entity,
	 * spawnReason))
	 * 
	 * gui.open();
	 * 
	 * }
	 */
}
