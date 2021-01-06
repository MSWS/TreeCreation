package xyz.msws.treecreation.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.generate.LinearGenerator;
import xyz.msws.treecreation.generate.TreeGenerator;
import xyz.msws.treecreation.trees.AbstractTree;
import xyz.msws.treecreation.trees.NativeTree;

public class CreateCommand implements CommandExecutor, TabCompleter {

	private TreeAPI plugin;
	private Map<String, AbstractTree> trees = new HashMap<>();

	public CreateCommand(TreeAPI plugin) {
		this.plugin = plugin;

		register(plugin.getTreeFile(), trees);
	}

	private void register(File file, Map<String, AbstractTree> trees) {
		if (!file.isDirectory()) {
			if (!file.getName().endsWith(".yml"))
				return;
			YamlConfiguration tree = YamlConfiguration.loadConfiguration(file);
			NativeTree t = NativeTree.deserialize(tree.getValues(true));
			trees.put(plugin.getMSG().simplify(file.getName().substring(0, file.getName().length() - 3)), t);
			return;
		}
		for (File f : file.listFiles()) {
			register(f, trees);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			plugin.getMSG().tell(sender, "Create", "You must be a player to generate a tree.");
			return true;
		}

		if (args.length == 0) {
			plugin.getMSG().tell(sender, "Create", "Please specify a tree file name.");
			return true;
		}

		String s = plugin.getMSG().simplify(String.join("", args));
		if (!trees.containsKey(s)) {
			plugin.getMSG().tell(sender, "Create", "Unknown tree file.");
			return true;
		}

		AbstractTree tree = trees.get(s);

		Player player = (Player) sender;
		Block target = player.getTargetBlockExact(10);

		TreeGenerator gen = new LinearGenerator(tree, target.getLocation());
		gen.generate(plugin, 5);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> result = new ArrayList<>();
		String s = plugin.getMSG().simplify(String.join("", args));
		for (String name : trees.keySet()) {
			if (plugin.getMSG().simplify(name).startsWith(s)) {
				result.add(plugin.getMSG().simplify(s));
			}
		}
		return result;
	}

}
