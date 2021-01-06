package xyz.msws.treecreation.commands;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.capture.Capturer;

public class CaptureCommand extends BukkitCommand {

	private TreeAPI plugin;

	public CaptureCommand(TreeAPI plugin) {
		super("capture");
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {

		if (!(sender instanceof Player)) {
			plugin.getMSG().tell(sender, "Capturer", "You must be a player to capture trees");
			return true;
		}

		Player player = (Player) sender;
		plugin.getMSG().tell(sender, "Capturer", plugin.getMSG().emphasis + "Right-Click " + plugin.getMSG().primary
				+ "on the bottom of the tree to capture it.");

		File file = new File(plugin.getTreeFile().getPath() + File.separator + "captured",
				player.getUniqueId() + "" + System.currentTimeMillis() + ".yml");

		new Capturer(plugin, file, player);
		return true;

	}

}
