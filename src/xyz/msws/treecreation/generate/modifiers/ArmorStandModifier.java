package xyz.msws.treecreation.generate.modifiers;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.generate.TreeGenerator;

public class ArmorStandModifier extends GeneratorModifier {

	private ArmorStand stand;
	private TreeAPI plugin;
	private TreeGenerator generator;

	public ArmorStandModifier(TreeAPI plugin, TreeGenerator generator) {
		super(plugin, generator);
		this.plugin = plugin;
		this.generator = generator;
	}

	@Override
	public void onPass() {
	}

	@Override
	public void onStart() {
		stand = (ArmorStand) generator.getOrigin().getWorld().spawnEntity(
				generator.getTree().getHighestBlockLocation(generator.getOrigin()), EntityType.ARMOR_STAND);
		NamespacedKey temp = new NamespacedKey(plugin, "temp");
		stand.getPersistentDataContainer().set(temp, PersistentDataType.INTEGER, 1);
		stand.setGravity(false);
		stand.setVisible(false);
		stand.setCustomName(ChatColor.GOLD + "Generating tree...");
		stand.setCustomNameVisible(true);
	}

	@Override
	public void onComplete() {
		stand.remove();
	}

	@Override
	public void onStopped() {
		if (stand == null || !stand.isValid())
			return;
		stand.remove();
	}

	@Override
	public void onPlace(TreeBlock block) {
		if (stand == null || !stand.isValid())
			return;
		String bar = plugin.getMSG().progressBar(generator.getProgress(), 10);
		String title = "&6Generating tree %s &f(&7%.2f%%&f)";

		stand.setCustomName(plugin.getMSG().color(String.format(title, bar, generator.getProgress() * 100)));
	}

}
