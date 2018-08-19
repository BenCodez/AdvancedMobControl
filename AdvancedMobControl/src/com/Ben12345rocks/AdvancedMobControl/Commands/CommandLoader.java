package com.Ben12345rocks.AdvancedMobControl.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedCore.Objects.TabCompleteHandle;
import com.Ben12345rocks.AdvancedCore.Objects.TabCompleteHandler;
import com.Ben12345rocks.AdvancedCore.UserManager.UserManager;
import com.Ben12345rocks.AdvancedCore.Util.Misc.ArrayUtils;
import com.Ben12345rocks.AdvancedCore.Util.Misc.StringUtils;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Commands.GUI.EntityGUI;

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

		plugin.advancedMobControlCommands.add(new CommandHandler(new String[] { "Configure" },
				"AdvancedMobControl.Configure", "Edit EntityHandles", false) {

			@Override
			public void execute(CommandSender sender, String[] args) {
				EntityGUI.getInstance().openGUI((Player) sender);
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
