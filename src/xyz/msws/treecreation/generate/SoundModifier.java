package xyz.msws.treecreation.generate;

import org.bukkit.block.Block;

import xyz.msws.treecreation.api.TreeAPI;

public class SoundModifier extends GeneratorModifier {

	public SoundModifier(TreeAPI plugin, TreeGenerator generator) {
		super(plugin, generator);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onPass() {
	}

	@Override
	public void onComplete() {
	}

	@Override
	public void onPlace(Block block) {
		block.getLocation().getWorld().playSound(block.getLocation(),
				block.getBlockData().getSoundGroup().getPlaceSound(), 1.0f, 2f - generator.getProgress() * 2.0f);
	}

}
