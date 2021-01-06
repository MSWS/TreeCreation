package xyz.msws.treecreation.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;

import xyz.msws.treecreation.api.TreeAPI;

public class TreeCommand implements CommandExecutor, TabCompleter {

	private Map<String, BukkitCommand> commands = new HashMap<>();

	private TreeAPI plugin;

	public TreeCommand(TreeAPI plugin) {
		this.plugin = plugin;

		commands.put("capture", new CaptureCommand(plugin));
		commands.put("create", new CreateCommand(plugin));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			plugin.getMSG().tell(sender, "Tree", "Please specify a sub-command.");
			return true;
		}

		BukkitCommand cmd = commands.get(args[0].toLowerCase());

		if (cmd == null) {
			plugin.getMSG().tell(sender, "Tree", plugin.getMSG().error + "Unknown sub-command.");
			return true;
		}

		String[] newArgs = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			newArgs[i - 1] = args[i];
		}

		cmd.execute(sender, args[0], newArgs);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> result = new ArrayList<>();
		if (args.length <= 1) {
			for (String s : commands.keySet()) {
				if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(s);
				}
			}
			return result;
		}

		BukkitCommand cmd = commands.get(args[0].toLowerCase());
		if (cmd == null)
			return result;

		String[] newArgs = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			newArgs[i - 1] = args[i];
		}

		return cmd.tabComplete(sender, args[0], newArgs);
	}

}
