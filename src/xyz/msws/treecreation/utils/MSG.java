package xyz.msws.treecreation.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Responsible for sending and delaying messages
 * 
 * @author imodm
 *
 */
public class MSG {

	private Map<CommandSender, UUID> uuids = new HashMap<>();

	@SuppressWarnings("deprecation")
	private UUID getUUID(CommandSender sender) {
		if (uuids.containsKey(sender))
			return uuids.get(sender);

		UUID uuid = UUID.randomUUID();

		if (sender instanceof Entity) {
			uuid = ((Entity) sender).getUniqueId();
		} else if (sender instanceof CommandBlock) {
			NamespacedKey key = new NamespacedKey("tree", "uuid");
			CommandBlock block = (CommandBlock) sender;
			PersistentDataContainer pdc = block.getPersistentDataContainer();
			if (!pdc.has(key, PersistentDataType.STRING)) {
				pdc.set(key, PersistentDataType.STRING, (uuid = UUID.randomUUID()).toString());
			}
		}
		uuids.put(sender, uuid);

		return uuid;
	}

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

	public boolean delayTell(CommandSender sender, long cd, String msg, Object... format) {
		Map<String, Long> msgs = messages.getOrDefault(getUUID(sender), new HashMap<>());
		String simp = simplify(msg);
		long d = msgs.getOrDefault(simp, 0L);
		if (System.currentTimeMillis() - d < cd)
			return false;
		tell(sender, msg, format);
		msgs.put(simp, System.currentTimeMillis());
		messages.put(getUUID(sender), msgs);
		return true;
	}

	public boolean delayTell(CommandSender sender, String msg, Object... format) {
		return delayTell(sender, 5000, msg, format);
	}

	public boolean delayTell(CommandSender sender, String module, String msg) {
		return delayTell(sender, 5000, module, msg);
	}

	public boolean delayTell(CommandSender sender, String module, String msg, Object... format) {
		return delayTell(sender, 5000, module, msg, format);
	}

	public boolean delayTell(CommandSender sender, long cd, String module, String msg) {
		return delayTell(sender, cd, this.module + module + secondary + " > " + primary + msg);
	}

	public boolean delayTell(CommandSender sender, long cd, String module, String msg, Object... format) {
		return delayTell(sender, cd, this.module + module + secondary + " > " + primary + msg, format);
	}

	public String simplify(String s) {
		s = ChatColor.stripColor(color(s)).trim().replace(" ", "").toLowerCase();
		return s;
	}

	public String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public String progressBar(float progress, int length) {
		return progressBar("\u25a0", progress, length);
	}

	public String progressBar(String c, float progress, int length) {
		String s = StringUtils.repeat(c, length);
		int index = (int) ((float) progress * (float) length);
		s = ChatColor.GREEN + s.substring(0, index) + ChatColor.RED + s.substring(index);
		return s;
	}

	public String progressbar(String a, String b, float progress, int length) {
		int index = (int) ((float) progress * (float) length);
		return StringUtils.repeat(a, index) + StringUtils.repeat(b, length - index);
	}
}
