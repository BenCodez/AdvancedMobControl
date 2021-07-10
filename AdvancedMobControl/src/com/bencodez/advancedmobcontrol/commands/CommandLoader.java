package com.bencodez.advancedmobcontrol.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.bencodez.advancedcore.api.command.CommandHandler;
import com.bencodez.advancedcore.api.command.TabCompleteHandle;
import com.bencodez.advancedcore.api.command.TabCompleteHandler;
import com.bencodez.advancedcore.api.messages.StringParser;
import com.bencodez.advancedcore.api.misc.ArrayUtils;
import com.bencodez.advancedcore.api.user.UserManager;
import com.bencodez.advancedmobcontrol.AdvancedMobControlMain;
import com.bencodez.advancedmobcontrol.commands.gui.EntityGUI;

import net.md_5.bungee.api.chat.TextComponent;

/**
 * The Class CommandLoader.
 */
public class CommandLoader {

	/** The plugin. */
	public AdvancedMobControlMain plugin = AdvancedMobControlMain.plugin;

	/**
	 * Instantiates a new command loader.
	 */
	public CommandLoader() {
		loadCommands();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				com.bencodez.advancedcore.thread.Thread.getInstance().run(new Runnable() {

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
		plugin.setAdvancedMobControlCommands(new ArrayList<CommandHandler>());
		plugin.getAdvancedMobControlCommands()
				.add(new CommandHandler(new String[] { "Reload" }, "AdvancedMobControl.Reload", "Reload the plugin") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						plugin.reload();
						sender.sendMessage(StringParser.getInstance().colorize(
								"&c" + plugin.getName() + " v" + plugin.getDescription().getVersion() + " reloaded"));
					}
				});
		plugin.getAdvancedMobControlCommands()
				.add(new CommandHandler(new String[] { "Help" }, "AdvancedMobControl.Help", "View this page") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						ArrayList<TextComponent> msg = new ArrayList<TextComponent>();
						msg.add(StringParser.getInstance().stringToComp("&c" + plugin.getName() + " help"));
						for (CommandHandler cmdHandle : plugin.getAdvancedMobControlCommands()) {
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

		plugin.getAdvancedMobControlCommands().add(new CommandHandler(new String[] { "Configure" },
				"AdvancedMobControl.Configure", "Edit EntityHandles", false) {

			@Override
			public void execute(CommandSender sender, String[] args) {
				EntityGUI.getInstance().openGUI((Player) sender);
			}
		});

		plugin.getAdvancedMobControlCommands()
				.add(new CommandHandler(new String[] { "Perms" }, "AdvancedMobControl.Perms", "View permissions list") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						ArrayList<String> msg = new ArrayList<String>();
						msg.add("&c" + plugin.getName() + " permissions");
						for (CommandHandler cmdHandle : plugin.getAdvancedMobControlCommands()) {
							msg.add(cmdHandle.getPerm());
						}
						if (sender instanceof Player) {
							UserManager.getInstance().getUser((Player) sender).sendMessage(msg);
						} else {
							sender.sendMessage(ArrayUtils.getInstance().convert(msg));
						}
					}
				});

		ArrayList<CommandHandler> advancedCoreCommands = new ArrayList<CommandHandler>();
		advancedCoreCommands.addAll(com.bencodez.advancedcore.command.CommandLoader.getInstance()
				.getBasicAdminCommands(AdvancedMobControlMain.plugin.getName()));
		advancedCoreCommands.addAll(com.bencodez.advancedcore.command.CommandLoader.getInstance()
				.getBasicCommands(AdvancedMobControlMain.plugin.getName()));
		for (CommandHandler handle : advancedCoreCommands) {
			String[] args = handle.getArgs();
			String[] newArgs = new String[args.length + 1];
			newArgs[0] = "AdvancedCore";
			for (int i = 0; i < args.length; i++) {
				newArgs[i + 1] = args[i];
			}
			handle.setArgs(newArgs);
			plugin.getAdvancedMobControlCommands().add(handle);
		}

	}

	public void loadTabComplete() {
		ArrayList<String> optionsEntity = new ArrayList<String>();
		for (EntityType entityType : EntityType.values()) {
			String entity = entityType.toString();
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

		TabCompleteHandler.getInstance().addTabCompleteOption(new TabCompleteHandle("(Entity)", optionsEntity) {

			@Override
			public void reload() {

			}

			@Override
			public void updateReplacements() {

			}
		});
		TabCompleteHandler.getInstance()
				.addTabCompleteOption(new TabCompleteHandle("(EntitySpawnReason)", optionsSpawn) {

					@Override
					public void reload() {

					}

					@Override
					public void updateReplacements() {

					}
				});
		TabCompleteHandler.getInstance()
				.addTabCompleteOption(new TabCompleteHandle("(EntityDamageCause)", optionsDamage) {

					@Override
					public void reload() {

					}

					@Override
					public void updateReplacements() {

					}
				});
	}
}
