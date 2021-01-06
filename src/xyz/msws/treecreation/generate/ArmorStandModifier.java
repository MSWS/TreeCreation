package xyz.msws.treecreation.generate;

import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import xyz.msws.treecreation.api.TreeAPI;

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
		if (stand == null || !stand.isValid())
			return;
		String name = plugin.getMSG().progressBar(generator.getProgress(), 8);
		stand.setCustomName(plugin.getMSG().color(plugin.getMSG().administrative + "Generating tree " + name));
	}

	@Override
	public void onStart() {
		stand = (ArmorStand) generator.origin.getWorld()
				.spawnEntity(generator.tree.getHighestBlockLocation(generator.origin), EntityType.ARMOR_STAND);

		stand.setGravity(false);
		stand.setVisible(false);
		stand.setCustomNameVisible(true);
	}

	@Override
	public void onComplete() {
		stand.remove();
	}

	@Override
	public void onPlace(Block block) {
	}

}
