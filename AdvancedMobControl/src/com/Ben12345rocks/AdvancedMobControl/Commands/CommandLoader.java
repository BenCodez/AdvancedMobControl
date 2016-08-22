package com.Ben12345rocks.AdvancedMobControl.Commands;

import java.util.ArrayList;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Ben12345rocks.AdvancedCore.Utils;
import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.AdvancedCore.Objects.User;
import com.Ben12345rocks.AdvancedMobControl.Main;
import com.Ben12345rocks.AdvancedMobControl.Config.ConfigEntity;

public class CommandLoader {

	public Main plugin = Main.plugin;

	public CommandLoader() {
		loadCommands();
	}

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
				"Entity", "(entity)", "SetMoney", "(number)" },
				"AdvancedMobControl.Entity.SetMoney",
				"Set the amount of money for killing entity") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				ConfigEntity.getInstance().setMoney(args[1],
						Integer.parseInt(args[3]));
				sender.sendMessage(Utils.getInstance().colorize("&cMoney set"));
			}
		});

		plugin.advancedMobControlCommands
				.add(new CommandHandler(new String[] { "Entity", "(entity)",
						"SetMoney", "(number)", "(entitydamagecause)" },
						"AdvancedMobControl.Entity.SetMoney.Damage",
						"Set the amount of money for killing entity on specific damage") {

					@Override
					public void execute(CommandSender sender, String[] args) {
						ConfigEntity.getInstance().setMoney(args[1], args[4],
								Integer.parseInt(args[3]));
						sender.sendMessage(Utils.getInstance().colorize(
								"&cMoney set"));
					}
				});

		plugin.advancedMobControlCommands.add(new CommandHandler(new String[] {
				"Entity", "(entity)", "SetExp", "(number)" },
				"AdvancedMobControl.Entity.SetEXP",
				"Set the amount of exp dropped on death of entity") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				ConfigEntity.getInstance().setExp(args[1],
						Integer.parseInt(args[3]));
				sender.sendMessage(Utils.getInstance().colorize("&cEXP set"));
			}
		});

		plugin.advancedMobControlCommands.add(new CommandHandler(new String[] {
				"Entity", "(entity)", "SetHealth", "(entityspawnreason)",
				"(number)" }, "AdvancedMobControl.Entity.SetHealth",
				"Set the amount of exp dropped on death of entity") {

			@Override
			public void execute(CommandSender sender, String[] args) {
				ConfigEntity.getInstance().setHealth(args[1], args[3],
						Integer.parseInt(args[4]));
				sender.sendMessage(Utils.getInstance().colorize("&cHealth set"));
			}
		});
	}
}
