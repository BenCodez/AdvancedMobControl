package com.Ben12345rocks.AdvancedMobControl.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedCore.Objects.Reward;
import com.Ben12345rocks.AdvancedCore.Objects.RewardHandler;
import com.Ben12345rocks.AdvancedCore.UserManager.UserManager;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventory.ClickEvent;
import com.Ben12345rocks.AdvancedCore.Util.Inventory.BInventoryButton;
import com.Ben12345rocks.AdvancedCore.Util.Misc.ArrayUtils;
import com.Ben12345rocks.AdvancedCore.Util.Misc.PlayerUtils;
import com.Ben12345rocks.AdvancedCore.Util.Misc.StringUtils;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.ValueRequest;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.Listeners.BooleanListener;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.Listeners.NumberListener;
import com.Ben12345rocks.AdvancedCore.Util.ValueRequest.Listeners.StringListener;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Config.ConfigEntity;

import net.md_5.bungee.api.chat.TextComponent;

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
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				com.Ben12345rocks.AdvancedCore.Thread.Thread.getInstance().run(new Runnable() {

					@Override
					public void run() {
						loadTabComplete();
					}
				});
			}
		});
	}

	/**
	 * Load commands.
	 */
	public void loadCommands() {
		plugin.advancedMobControlCommands = new ArrayList<CommandHandler>();
		plugin.advancedMobControlCommands
				.add(new CommandHandler(new String[] { "Reload" }, "AdvancedMobControl.Reload", "Reload the plugin") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						plugin.reload();
						sender.sendMessage(StringUtils.getInstance().colorize(
								"&c" + plugin.getName() + " v" + plugin.getDescription().getVersion() + " reloaded"));
					}
				});
		plugin.advancedMobControlCommands
				.add(new CommandHandler(new String[] { "Help" }, "AdvancedMobControl.Help", "View this page") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						ArrayList<TextComponent> msg = new ArrayList<TextComponent>();
						msg.add(StringUtils.getInstance().stringToComp("&c" + plugin.getName() + " help"));
						for (CommandHandler cmdHandle : plugin.advancedMobControlCommands) {
							msg.add(cmdHandle.getHelpLine("/advancedmobcontrol"));
						}
						if (sender instanceof Player) {
							UserManager.getInstance().getUser((Player) sender).sendJson(msg);
						} else {
							sender.sendMessage(
									ArrayUtils.getInstance().convert(ArrayUtils.getInstance().comptoString(msg)));
						}
					}
				});
		plugin.advancedMobControlCommands
				.add(new CommandHandler(new String[] { "Perms" }, "AdvancedMobControl.Perms", "View permissions list") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						ArrayList<String> msg = new ArrayList<String>();
						msg.add("&c" + plugin.getName() + " permissions");
						for (CommandHandler cmdHandle : plugin.advancedMobControlCommands) {
							msg.add(cmdHandle.getPerm());
						}
						if (sender instanceof Player) {
							UserManager.getInstance().getUser((Player) sender).sendMessage(msg);
						} else {
							sender.sendMessage(ArrayUtils.getInstance().convert(msg));
						}
					}
				});

		plugin.advancedMobControlCommands.add(
				new CommandHandler(new String[] { "Entity" }, "AdvancedMobControl.Entity", "open list of entities") {

					@Override
					public void execute(CommandSender sender, String[] args) {

						if (sender instanceof Player) {
							BInventory inv = new BInventory("Entities");
							int count = 0;
							for (String entity : ConfigEntity.getInstance().getEntitysNames()) {
								inv.addButton(count,
										new BInventoryButton(entity, new String[0], new ItemStack(Material.STONE)) {

											@Override
											public void onClick(ClickEvent event) {
												if (event.getWhoClicked() instanceof Player) {
													String entity = event.getCurrentItem().getItemMeta()
															.getDisplayName();
													Player player = (Player) event.getWhoClicked();
													player.closeInventory();
													player.performCommand("advancedmobcontrol Entity " + entity);
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

		plugin.advancedMobControlCommands.add(new CommandHandler(new String[] { "Entity", "(Entity)" },
				"AdvancedMobControl.Entity", "Open GUI to edit entities", false) {

			@Override
			public void execute(CommandSender sender, String[] args) {
				PlayerUtils.getInstance().setPlayerMeta((Player) sender, "Entity", args[1]);

				BInventory inv = new BInventory("EntityGUI: " + args[1]);

				ArrayList<String> lore = new ArrayList<String>();
				lore.add("&cCurrently: &c&l" + ConfigEntity.getInstance().getMoney(args[1]));

				inv.addButton(inv.getNextSlot(), new BInventoryButton("SetMoney",
						ArrayUtils.getInstance().convert(lore), new ItemStack(Material.GOLDEN_APPLE)) {

					@Override
					public void onClick(ClickEvent event) {
						if (!(event.getWhoClicked() instanceof Player)) {
							return;
						}
						String entity = event.getInventory().getTitle().split(" ")[1];
						Player player = (Player) event.getWhoClicked();
						player.closeInventory();
						new ValueRequest().requestNumber(player, "" + ConfigEntity.getInstance().getMoney(entity), null,
								new NumberListener() {

									@Override
									public void onInput(Player player, Number value) {
										String entity = event.getInventory().getTitle().split(" ")[1];

										ConfigEntity.getInstance().setMoney(entity, value.intValue());
										player.sendMessage("Value set");

										plugin.reload();
									}
								});
					}
				});

				inv.addButton(inv.getNextSlot(),
						new BInventoryButton("SetMoneyDamage", new String[0], new ItemStack(Material.GOLDEN_APPLE)) {

							@Override
							public void onClick(ClickEvent event) {
								if (!(event.getWhoClicked() instanceof Player)) {
									return;
								}
								Player player = (Player) event.getWhoClicked();
								String entity = event.getInventory().getTitle().split(" ")[1];
								BInventory inv = new BInventory("EntityGUI: " + entity);
								int count = 0;
								for (DamageCause damage : DamageCause.values()) {
									if (count < 54) {
										ArrayList<String> lore = new ArrayList<String>();
										lore.add("&cCurrently: &c&l"
												+ ConfigEntity.getInstance().getMoney(entity, damage.toString()));

										inv.addButton(count, new BInventoryButton(damage.toString(),
												ArrayUtils.getInstance().convert(lore), new ItemStack(Material.STONE)) {

											@Override
											public void onClick(ClickEvent event) {
												String entity = event.getInventory().getTitle().split(" ")[1];
												Player player = (Player) event.getWhoClicked();
												player.closeInventory();

												new ValueRequest().requestNumber(player, "" + ConfigEntity.getInstance()
														.getMoney(entity, damage.toString()), null,
														new NumberListener() {

															@Override
															public void onInput(Player player, Number value) {
																String entity = event.getInventory().getTitle()
																		.split(" ")[1];

																ConfigEntity.getInstance().setMoney(entity,
																		damage.toString(), value.intValue());
																player.sendMessage("Value set");

																plugin.reload();
															}
														});

											}
										});
										count++;
									}

								}
								inv.openInventory(player);

							}

						});

				inv.addButton(inv.getNextSlot(),
						new BInventoryButton("SetHealth", new String[0], new ItemStack(Material.GOLDEN_APPLE)) {

							@Override
							public void onClick(ClickEvent event) {
								if (!(event.getWhoClicked() instanceof Player)) {
									return;
								}
								Player player = (Player) event.getWhoClicked();
								String entity = event.getInventory().getTitle().split(" ")[1];
								BInventory inv = new BInventory("EntityGUI: " + entity);

								for (SpawnReason spawnReason : SpawnReason.values()) {

									ArrayList<String> lore = new ArrayList<String>();
									lore.add("&cCurrently: &c&l"
											+ ConfigEntity.getInstance().getHealth(entity, spawnReason.toString()));

									inv.addButton(inv.getNextSlot(), new BInventoryButton(spawnReason.toString(),
											ArrayUtils.getInstance().convert(lore), new ItemStack(Material.STONE)) {

										@Override
										public void onClick(ClickEvent event) {
											String entity = event.getInventory().getTitle().split(" ")[1];
											Player player = (Player) event.getWhoClicked();
											player.closeInventory();

											new ValueRequest()
													.requestNumber(player,
															"" + ConfigEntity.getInstance().getHealth(entity,
																	spawnReason.toString()),
															null, new NumberListener() {

																@Override
																public void onInput(Player player, Number value) {
																	String entity = event.getInventory().getTitle()
																			.split(" ")[1];

																	ConfigEntity.getInstance().setHealth(entity,
																			spawnReason.toString(), value.intValue());
																	player.sendMessage("Value set");

																	plugin.reload();

																}
															});
										}
									});

								}
								inv.openInventory(player);

							}
						});

				lore = new ArrayList<String>();
				lore.add("&cCurrently: &c&l" + ConfigEntity.getInstance().getExp(args[1], 0));

				inv.addButton(inv.getNextSlot(), new BInventoryButton("SetEXP", ArrayUtils.getInstance().convert(lore),
						new ItemStack(Material.EXP_BOTTLE)) {

					@Override
					public void onClick(ClickEvent event) {
						if (!(event.getWhoClicked() instanceof Player)) {
							return;
						}
						String entity = event.getInventory().getTitle().split(" ")[1];
						Player player = (Player) event.getWhoClicked();
						new ValueRequest().requestNumber(player, "" + ConfigEntity.getInstance().getExp(entity, 0),
								null, new NumberListener() {

									@Override
									public void onInput(Player player, Number value) {
										String entity = event.getInventory().getTitle().split(" ")[1];

										ConfigEntity.getInstance().setExp(entity, 0, value.intValue());
										player.sendMessage("Value set");

										plugin.reload();

									}
								});
					}
				});

				inv.addButton(inv.getNextSlot(),
						new BInventoryButton("Add reward file", new String[] {}, new ItemStack(Material.PAPER)) {

							@Override
							public void onClick(ClickEvent clickEvent) {
								ArrayList<String> rewardNames = new ArrayList<String>();
								for (Reward reward : RewardHandler.getInstance().getRewards()) {
									rewardNames.add(reward.getRewardName());
								}
								new ValueRequest().requestString(clickEvent.getPlayer(), "",
										ArrayUtils.getInstance().convert(rewardNames), true, new StringListener() {

											@Override
											public void onInput(Player player, String value) {
												String entity = clickEvent.getEvent().getInventory().getTitle()
														.split(" ")[1];
												List<String> rewards = ConfigEntity.getInstance().getRewards(entity);
												rewards.add(value);
												ConfigEntity.getInstance().setRewards(entity, rewards);
												player.sendMessage("Reward added");
												plugin.reload();
											}
										});

							}
						});

				inv.addButton(inv.getNextSlot(),
						new BInventoryButton("Remove reward file", new String[] {}, new ItemStack(Material.PAPER)) {

							@Override
							public void onClick(ClickEvent clickEvent) {
								String entity = clickEvent.getEvent().getInventory().getTitle().split(" ")[1];
								new ValueRequest().requestString(clickEvent.getPlayer(), "",
										ArrayUtils.getInstance().convert(
												(ArrayList<String>) ConfigEntity.getInstance().getRewards(entity)),
										false, new StringListener() {

											@Override
											public void onInput(Player player, String value) {
												String entity = clickEvent.getEvent().getInventory().getTitle()
														.split(" ")[1];
												List<String> rewards = ConfigEntity.getInstance().getRewards(entity);
												rewards.remove(value);
												ConfigEntity.getInstance().setRewards(entity, rewards);
												player.sendMessage("Reward removed");
												plugin.reload();
											}
										});

							}
						});

				inv.addButton(inv.getNextSlot(),
						new BInventoryButton("Add reward file damage", new String[] {}, new ItemStack(Material.PAPER)) {

							@Override
							public void onClick(ClickEvent clickEvent) {
								ArrayList<String> damages = new ArrayList<String>();
								for (DamageCause d : DamageCause.values()) {
									damages.add(d.toString());
								}
								new ValueRequest().requestString(clickEvent.getPlayer(), "",
										ArrayUtils.getInstance().convert(damages), new StringListener() {

											@Override
											public void onInput(Player player, String damage) {
												ArrayList<String> rewardNames = new ArrayList<String>();
												for (Reward reward : RewardHandler.getInstance().getRewards()) {
													rewardNames.add(reward.getRewardName());
												}
												new ValueRequest().requestString(player, "",
														ArrayUtils.getInstance().convert(rewardNames), true,
														new StringListener() {

															@Override
															public void onInput(Player player, String value) {
																String entity = clickEvent.getEvent().getInventory()
																		.getTitle().split(" ")[1];
																List<String> rewards = ConfigEntity.getInstance()
																		.getRewards(entity, damage);
																rewards.add(value);
																ConfigEntity.getInstance().setRewards(entity, damage,
																		rewards);
																player.sendMessage("Reward added");
																plugin.reload();
															}
														});

											}
										});

							}
						});

				inv.addButton(inv.getNextSlot(), new BInventoryButton("Remove reward file damage", new String[] {},
						new ItemStack(Material.PAPER)) {

					@Override
					public void onClick(ClickEvent clickEvent) {
						ArrayList<String> damages = new ArrayList<String>();
						for (DamageCause d : DamageCause.values()) {
							damages.add(d.toString());
						}
						new ValueRequest().requestString(clickEvent.getPlayer(), "",
								ArrayUtils.getInstance().convert(damages), new StringListener() {

									@Override
									public void onInput(Player player, String damage) {
										String entity = clickEvent.getEvent().getInventory().getTitle().split(" ")[1];
										new ValueRequest().requestString(player, "",
												ArrayUtils.getInstance()
														.convert((ArrayList<String>) ConfigEntity.getInstance()
																.getRewards(entity, damage)),
												false, new StringListener() {

													@Override
													public void onInput(Player player, String value) {
														String entity = clickEvent.getEvent().getInventory().getTitle()
																.split(" ")[1];
														List<String> rewards = ConfigEntity.getInstance()
																.getRewards(entity, damage);
														rewards.remove(value);
														ConfigEntity.getInstance().setRewards(entity, damage, rewards);
														player.sendMessage("Reward removed");
														plugin.reload();
													}
												});

									}
								});

					}
				});

				inv.addButton(inv.getNextSlot(),
						new BInventoryButton("DisableRightClick",
								new String[] {
										"Currently: " + ConfigEntity.getInstance().getDisableNormalClick(args[1]) },
								new ItemStack(Material.STONE)) {

							@Override
							public void onClick(ClickEvent clickEvent) {
								new ValueRequest().requestBoolean(clickEvent.getPlayer(), new BooleanListener() {

									@Override
									public void onInput(Player player, boolean value) {
										String entity = (String) PlayerUtils.getInstance().getPlayerMeta(player,
												"Entity");
										ConfigEntity.getInstance().setDisableNormalClick(entity, value);
										plugin.reload();
										player.sendMessage("Set disable right click to " + value);
									}
								});

							}
						});

				inv.addButton(inv.getNextSlot(),
						new BInventoryButton("Add right click reward", new String[] {}, new ItemStack(Material.STONE)) {

							@Override
							public void onClick(ClickEvent clickEvent) {
								Player player = clickEvent.getPlayer();
								ArrayList<String> rewardNames = new ArrayList<String>();
								for (Reward reward : RewardHandler.getInstance().getRewards()) {
									rewardNames.add(reward.getRewardName());
								}
								new ValueRequest().requestString(player, "",
										ArrayUtils.getInstance().convert(rewardNames), true, new StringListener() {

											@Override
											public void onInput(Player player, String value) {
												String entity = (String) PlayerUtils.getInstance().getPlayerMeta(player,
														"Entity");
												ArrayList<String> rewards = ConfigEntity.getInstance()
														.getRightClickedRewards(entity);
												rewards.add(value);
												ConfigEntity.getInstance().setRightClickedRewards(entity, rewards);
												player.sendMessage("Reward added");
												plugin.reload();
											}
										});
							}
						});

				inv.addButton(inv.getNextSlot(), new BInventoryButton("Remove right click reward", new String[] {},
						new ItemStack(Material.STONE)) {

					@Override
					public void onClick(ClickEvent clickEvent) {
						Player player = clickEvent.getPlayer();

						new ValueRequest()
								.requestString(player, "",
										ArrayUtils.getInstance()
												.convert(ConfigEntity.getInstance()
														.getRightClickedRewards((String) PlayerUtils.getInstance()
																.getPlayerMeta(player, "Entity"))),
										true, new StringListener() {

											@Override
											public void onInput(Player player, String value) {
												String entity = (String) PlayerUtils.getInstance().getPlayerMeta(player,
														"Entity");
												ArrayList<String> rewards = ConfigEntity.getInstance()
														.getRightClickedRewards(entity);
												rewards.remove(value);
												ConfigEntity.getInstance().setRightClickedRewards(entity, rewards);
												player.sendMessage("Reward removed");
												plugin.reload();
											}
										});
					}
				});

				inv.openInventory((Player) sender);

			}
		});
	}

	public void loadTabComplete() {
		ArrayList<String> optionsEntity = new ArrayList<String>();
		for (String entity : ConfigEntity.getInstance().getEntitysNames()) {
			if (!optionsEntity.contains(entity)) {
				optionsEntity.add(entity);
			}
		}

		ArrayList<String> optionsDamage = new ArrayList<String>();
		for (DamageCause damage : DamageCause.values()) {
			String str = damage.toString();
			if (!optionsDamage.contains(str)) {
				optionsDamage.add(str);
			}
		}

		ArrayList<String> optionsSpawn = new ArrayList<String>();
		for (SpawnReason spawn : SpawnReason.values()) {
			String str = spawn.toString();
			if (!optionsSpawn.contains(str)) {
				optionsSpawn.add(str);
			}
		}

		for (int i = 0; i < plugin.advancedMobControlCommands.size(); i++) {
			plugin.advancedMobControlCommands.get(i).addTabCompleteOption("(Entity)", optionsEntity);
			plugin.advancedMobControlCommands.get(i).addTabCompleteOption("(EntitySpawnReason)", optionsSpawn);
			plugin.advancedMobControlCommands.get(i).addTabCompleteOption("(EntityDamageCause)", optionsDamage);
		}
	}
}
