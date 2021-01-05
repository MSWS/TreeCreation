package xyz.msws.treecreation.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

/**
 * Responsible for sending and delaying messages
 * 
 * @author imodm
 *
 */
public class MSG {

	public String prefix = "&9", primary = "&7", secondary = "&8", emphasis = "&e", administrative = "&4",
			module = "&9", error = "&c";

	private Map<UUID, Map<String, Long>> messages = new HashMap<>();

	public void tell(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void tell(CommandSender sender, String msg, Object... args) {
		tell(sender, String.format(msg, args));
	}

	public void tell(CommandSender sender, String module, String msg) {
		tell(sender, this.module + module + secondary + " > " + primary + msg);
	}

	public void tell(CommandSender sender, String module, String msg, Object... args) {
		tell(sender, String.format(this.module + module + secondary + " > " + primary + msg, args));
	}

	public void tellraw(CommandSender sender, String msg) {
		sender.sendMessage(msg);
	}

	public boolean delayTell(Entity sender, long cd, String msg) {
		Map<String, Long> msgs = messages.getOrDefault(messages, new HashMap<>());
		String simp = simplify(msg);
		long d = msgs.getOrDefault(simp, 0L);
		if (System.currentTimeMillis() - d < cd)
			return false;
		tell(sender, msg);
		msgs.put(simp, System.currentTimeMillis());
		return true;
	}

	public boolean delayTell(Entity sender, String msg) {
		return delayTell(sender, 5000, msg);
	}

	public boolean delayTell(Entity sender, long cd, String module, String msg) {
		return delayTell(sender, cd, this.module + module + secondary + " > " + primary + msg);
	}

	public String simplify(String s) {
		s = ChatColor.stripColor(color(s)).trim().replace(" ", "").toLowerCase();
		return s;
	}

	public String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
