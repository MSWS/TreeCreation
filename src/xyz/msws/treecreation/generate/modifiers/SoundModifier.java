package xyz.msws.treecreation.generate.modifiers;

import org.bukkit.block.Block;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.generate.TreeGenerator;

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
	public void onPlace(TreeBlock tb) {
		Block block = tb.getTargetLocation(generator.getOrigin()).getBlock();

		block.getLocation().getWorld().playSound(block.getLocation(),
				block.getBlockData().getSoundGroup().getPlaceSound(), 1.0f, 2f - generator.getProgress() * 2.0f);
	}

}
