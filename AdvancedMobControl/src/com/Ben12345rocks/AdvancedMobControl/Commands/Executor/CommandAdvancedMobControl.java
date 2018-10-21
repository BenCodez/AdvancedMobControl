package com.Ben12345rocks.AdvancedMobControl.Commands.Executor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Ben12345rocks.AdvancedCore.CommandAPI.CommandHandler;
import com.Ben12345rocks.AdvancedMobControl.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandAdvancedMobControl.
 */
public class CommandAdvancedMobControl implements CommandExecutor {

	/** The instance. */
	private static CommandAdvancedMobControl instance = new CommandAdvancedMobControl();

	/** The plugin. */
	private static Main plugin;

	/**
	 * Gets the single instance of CommandAdvancedMobControl.
	 *
	 * @return single instance of CommandAdvancedMobControl
	 */
	public static CommandAdvancedMobControl getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new command advanced mob control.
	 */
	private CommandAdvancedMobControl() {
	}

	/**
	 * Instantiates a new command advanced mob control.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public CommandAdvancedMobControl(Main plugin) {
		CommandAdvancedMobControl.plugin = plugin;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender
	 * , org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		for (CommandHandler commandHandler : plugin.getAdvancedMobControlCommands()) {
			if (commandHandler.runCommand(sender, args)) {
				return true;
			}
		}

		// invalid command
		sender.sendMessage(ChatColor.RED + "No valid arguments, see /mobcontrol help!");
		return true;
	}

}
