package xyz.msws.treecreation.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import xyz.msws.treecreation.api.TreeAPI;

public class RemoveCommand extends BukkitCommand {

	private TreeAPI plugin;

	protected RemoveCommand(TreeAPI plugin) {
		super("remove");
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			plugin.getMSG().tell(sender, "Create", "Please specify a tree file name.");
			return true;
		}

		String s = plugin.getMSG().simplify(String.join("", args));
		if (!plugin.getTreeTemplates().containsKey(s)) {
			plugin.getMSG().tell(sender, "Create", "Unknown tree file.");
			return true;
		}

		plugin.removeTemplate(s);
		plugin.getMSG().tell(sender, "Create", "Successfully deleted %s%s%s.", plugin.getMSG().emphasis, s,
				plugin.getMSG().primary);
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<>();
		String s = plugin.getMSG().simplify(String.join("", args));
		for (String name : plugin.getTreeTemplates().keySet()) {
			if (plugin.getMSG().simplify(name).startsWith(s)) {
				result.add(plugin.getMSG().simplify(name));
			}
		}
		return result;
	}

}
