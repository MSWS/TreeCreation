package xyz.msws.treecreation.generate;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Particle;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.TreeBlock;

public class ChristmasModifier extends GeneratorModifier {

	private ThreadLocalRandom rnd;
	private int height;
	private double range;

	public ChristmasModifier(TreeAPI plugin, TreeGenerator generator) {
		super(plugin, generator);
		this.rnd = ThreadLocalRandom.current();
		this.height = generator.tree.getHighestBlockLocation(generator.origin).getBlockY()
				- generator.origin.getBlockY();
		this.range = generator.tree.getFurthestBlockLocation(generator.origin).distance(generator.origin) * (2.0 / 3.0);
	}

	@Override
	public void onComplete() {
	}

	@Override
	public void onPass() {
		Location origin = generator.origin.clone();
		double off = (System.currentTimeMillis() - generator.getStartTime()) / (500 - (generator.getProgress() * 400));

		for (double t = 0; t <= height; t += .1) {
			Location l = origin.clone().add(Math.cos(off + t) * range, t, Math.sin(off + t) * range);
			l.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 0, 0, 0, 0);
		}
	}

	@Override
	public void onPlace(TreeBlock block) {
		Location l = block.getTargetLocation(generator.origin).getBlock().getLocation().clone();
		l.add(.5, .5, .5);
		l.getWorld().spawnParticle(Particle.TOTEM, l, rnd.nextInt(2, 6), .5, .5, .5);

		if (rnd.nextFloat() > .2f)
			return;

		l.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 1);
	}

}
