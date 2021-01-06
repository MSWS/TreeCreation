package xyz.msws.treecreation.commands;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.AbstractTree;

public class ListCommand extends BukkitCommand {

	private TreeAPI plugin;

	public ListCommand(TreeAPI plugin) {
		super("list");
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		plugin.getMSG().tell(sender, "Tree List", "Listing %s%s %stree%s", ChatColor.YELLOW,
				plugin.getTreeTemplates().size(), plugin.getMSG().primary,
				plugin.getTreeTemplates().size() == 1 ? "" : "s");

		for (Entry<String, AbstractTree> tree : plugin.getTreeTemplates().entrySet()) {
			TextComponent msg = new TextComponent(tree.getKey() + " (" + ChatColor.YELLOW
					+ tree.getValue().getBlocks().size() + ChatColor.GRAY + ")");
			msg.setColor(net.md_5.bungee.api.ChatColor.GRAY);
			msg.setHoverEvent(new HoverEvent(Action.SHOW_TEXT,
					new Text(plugin.getMSG().color(tree.getValue().getDescription() + "\nClick to Create"))));
			msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tree create " + tree.getKey()));
			sender.spigot().sendMessage(msg);
		}
		return true;
	}

}
