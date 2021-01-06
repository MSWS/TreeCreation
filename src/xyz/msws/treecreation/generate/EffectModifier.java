package xyz.msws.treecreation.generate;

import org.bukkit.Particle;
import org.bukkit.block.Block;

import xyz.msws.treecreation.api.TreeAPI;

public class EffectModifier extends GeneratorModifier {

	public EffectModifier(TreeAPI plugin, TreeGenerator generator) {
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
		block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 4, 0, 0, 0, block.getBlockData());
	}

}
