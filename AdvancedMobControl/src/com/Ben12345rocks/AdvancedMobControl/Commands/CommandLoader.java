package com.Ben12345rocks.AdvancedMobControl.Commands;

import java.util.ArrayList;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.Ben12345rocks.AdvancedCore.Utils;
import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedCore.Objects.User;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventoryButton;
import com.Ben12345rocks.AdvancedCore.Util.Request.InputListener;
import com.Ben12345rocks.AdvancedCore.Util.Request.RequestManager;
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

		plugin.advancedMobControlCommands.add(new CommandHandler(
				new String[] { "Entity" }, "AdvancedMobControl.Entity",
				"open list of entities") {

			@Override
			public void execute(CommandSender sender, String[] args) {

				if (sender instanceof Player) {
					BInventory inv = new BInventory("Entities");
					int count = 0;
					for (String entity : ConfigEntity.getInstance()
							.getEntitysNames()) {
						inv.addButton(count, new BInventoryButton(entity,
								new String[0], new ItemStack(Material.STONE)) {

							@Override
							public void onClick(InventoryClickEvent event) {
								if (event.getWhoClicked() instanceof Player) {
									String entity = event.getCurrentItem()
											.getItemMeta().getDisplayName();
									Player player = (Player) event.getWhoClicked();
									player.closeInventory();
									player.performCommand("advancedmobcontrol Entity "
											+ entity);
								}

							}
						});
						count++;
					}
					inv.openInventory((Player) sender);
				} else {
					sender.sendMessage("Must be player");
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
						player.closeInventory();
						User user = new User(Main.plugin, player);
						new RequestManager(
								(Conversable) player,
								user.getInputMethod(),
								new InputListener() {

									@Override
									public void onInput(
											Conversable conversable,
											String input) {
										String entity = event.getInventory()
												.getTitle().split(" ")[1];
										if (Utils.getInstance().isInt(input)) {
											ConfigEntity
													.getInstance()
													.setMoney(
															entity,
															Integer.parseInt(input));
											conversable
													.sendRawMessage("Value set");
										} else {
											conversable
													.sendRawMessage("Must be a interger");
										}

										plugin.reload();

									}
								}

								,
								"Type value in chat to send, cancel by typing cancel",
								""
										+ ConfigEntity.getInstance().getMoney(
												entity));

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
										String entity = event.getInventory()
												.getTitle().split(" ")[1];
										Player player = (Player) event
												.getWhoClicked();
										player.closeInventory();
										User user = new User(Main.plugin,
												player);
										new RequestManager(
												(Conversable) player,
												user.getInputMethod(),
												new InputListener() {

													@Override
													public void onInput(
															Conversable conversable,
															String input) {
														String entity = event
																.getInventory()
																.getTitle()
																.split(" ")[1];
														if (Utils.getInstance()
																.isInt(input)) {
															ConfigEntity
																	.getInstance()
																	.setMoney(
																			entity,
																			damage.toString(),
																			Integer.parseInt(input));
															conversable
																	.sendRawMessage("Value set");
														} else {
															conversable
																	.sendRawMessage("Must be a interger");
														}

														plugin.reload();

													}
												}

												,
												"Type value in chat to send, cancel by typing cancel",
												""
														+ ConfigEntity
																.getInstance()
																.getMoney(
																		entity,
																		damage.toString()));

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
												String entity = event
														.getInventory()
														.getTitle().split(" ")[1];
												Player player = (Player) event
														.getWhoClicked();
												player.closeInventory();
												User user = new User(
														Main.plugin, player);
												new RequestManager(
														(Conversable) player,
														user.getInputMethod(),
														new InputListener() {

															@Override
															public void onInput(
																	Conversable conversable,
																	String input) {
																String entity = event
																		.getInventory()
																		.getTitle()
																		.split(" ")[1];
																if (Utils
																		.getInstance()
																		.isInt(input)) {
																	ConfigEntity
																			.getInstance()
																			.setMoney(
																					entity,
																					spawnReason
																							.toString(),
																					Integer.parseInt(input));
																	conversable
																			.sendRawMessage("Value set");
																} else {
																	conversable
																			.sendRawMessage("Must be a interger");
																}

																plugin.reload();

															}
														}

														,
														"Type value in chat to send, cancel by typing cancel",
														""
																+ ConfigEntity
																		.getInstance()
																		.getMoney(
																				entity,
																				spawnReason
																						.toString()));

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

						player.closeInventory();
						User user = new User(Main.plugin, player);
						new RequestManager(
								(Conversable) player,
								user.getInputMethod(),
								new InputListener() {

									@Override
									public void onInput(
											Conversable conversable,
											String input) {
										String entity = event.getInventory()
												.getTitle().split(" ")[1];
										if (Utils.getInstance().isInt(input)) {
											ConfigEntity.getInstance().setExp(
													entity,
													Integer.parseInt(input));
											conversable
													.sendRawMessage("Value set");
										} else {
											conversable
													.sendRawMessage("Must be a interger");
										}

										plugin.reload();

									}
								}

								,
								"Type value in chat to send, cancel by typing cancel",
								"" + ConfigEntity.getInstance().getExp(entity));

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
