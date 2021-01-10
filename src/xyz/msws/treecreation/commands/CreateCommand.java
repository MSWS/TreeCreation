package xyz.msws.treecreation.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.gui.TreeSelectionGUI;

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

		new TreeSelectionGUI(plugin, ((Player) sender).getUniqueId()).openInventory();

//		if (args.length == 0) {
//			plugin.getMSG().tell(sender, "Create", "Please specify a tree file name.");
//			return true;
//		}
//
//		String s = plugin.getMSG().simplify(String.join("", args));
//		if (!plugin.getTreeTemplates().containsKey(s)) {
//			plugin.getMSG().tell(sender, "Create", "Unknown tree file.");
//			return true;
//		}
//
//		AbstractTree tree = plugin.getTreeTemplates().get(s);
//
//		Player player = (Player) sender;
//		Block target = player.getTargetBlockExact(10);
//
//		if (target == null || target.getType().isAir()) {
//			plugin.getMSG().tell(sender, "Create", "Please look at the target block");
//			return true;
//		}
//
//		for (BlockFace face : new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST }) {
//			if (target.getRelative(face).getType().isSolid()) {
//				target = target.getRelative(BlockFace.UP);
//				break;
//			}
//		}
//
//		tree = new TreeFactory(tree).modify(new xyz.msws.treecreation.trees.modifiers.ChristmasModifier()).build();
//
//		TreeGenerator gen = new RVGenerator(plugin, tree, target.getLocation());
////		TreeGenerator gen = new TopDownGenerator(tree, target.getLocation());
//		gen.addModifier(new ArmorStandModifier(plugin, gen));
//		gen.addModifier(new EffectModifier(plugin, gen));
//		gen.addModifier(new SnowModifier(plugin, gen));
//		gen.addModifier(new ChristmasModifier(plugin, gen));
//		gen.generate(plugin, 1);
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
