package xyz.msws.treecreation.generate.modifiers;

import org.bukkit.Location;
import org.bukkit.Particle;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.generate.TreeGenerator;

public class EffectModifier extends GeneratorModifier {

	public EffectModifier(TreeAPI plugin, TreeGenerator generator) {
		super(plugin, generator);
	}

	@Override
	public void onPlace(TreeBlock block) {
		Location l = block.getTargetLocation(generator.getOrigin());
		l.getWorld().spawnParticle(Particle.BLOCK_DUST, l, 4, 0, 0, 0, block.getBlock());
	}

	@Override
	public void onComplete() {
	}

}
