package xyz.msws.treecreation.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.generate.ArmorStandModifier;
import xyz.msws.treecreation.generate.EffectModifier;
import xyz.msws.treecreation.generate.LinearGenerator;
import xyz.msws.treecreation.generate.SoundModifier;
import xyz.msws.treecreation.generate.TreeGenerator;
import xyz.msws.treecreation.trees.AbstractTree;

public class CreateCommand extends BukkitCommand {

	private TreeAPI plugin;

	public CreateCommand(TreeAPI plugin) {
		super("create");
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!(sender instanceof Player)) {
			plugin.getMSG().tell(sender, "Create", "You must be a player to generate a tree.");
			return true;
		}

		if (args.length == 0) {
			plugin.getMSG().tell(sender, "Create", "Please specify a tree file name.");
			return true;
		}

		String s = plugin.getMSG().simplify(String.join("", args));
		if (!plugin.getTreeTemplates().containsKey(s)) {
			plugin.getMSG().tell(sender, "Create", "Unknown tree file.");
			return true;
		}

		AbstractTree tree = plugin.getTreeTemplates().get(s);

		Player player = (Player) sender;
		Block target = player.getTargetBlockExact(10);

		if (target == null || target.getType().isAir()) {
			plugin.getMSG().tell(sender, "Create", "Please look at the target block");
			return true;
		}

		TreeGenerator gen = new LinearGenerator(tree, target.getLocation());
		gen.addModifier(new ArmorStandModifier(plugin, gen));
		gen.addModifier(new SoundModifier(plugin, gen));
		gen.addModifier(new EffectModifier(plugin, gen));
		gen.generate(plugin, 1);
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
