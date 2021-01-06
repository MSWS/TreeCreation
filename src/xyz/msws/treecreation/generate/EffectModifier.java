package xyz.msws.treecreation.generate;

import org.bukkit.Location;
import org.bukkit.Particle;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.TreeBlock;

public class EffectModifier extends GeneratorModifier {

	public EffectModifier(TreeAPI plugin, TreeGenerator generator) {
		super(plugin, generator);
	}

	@Override
	public void onPlace(TreeBlock block) {
		Location l = block.getTargetLocation(generator.origin);
		l.getWorld().spawnParticle(Particle.BLOCK_DUST, l, 4, 0, 0, 0, l.getBlock().getBlockData());
	}

	@Override
	public void onComplete() {
	}

}
