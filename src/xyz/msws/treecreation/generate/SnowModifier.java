package xyz.msws.treecreation.generate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.TreeBlock;

public class SnowModifier extends GeneratorModifier {

	private ThreadLocalRandom rnd;

	public SnowModifier(TreeAPI plugin, TreeGenerator generator) {
		super(plugin, generator);
		this.rnd = ThreadLocalRandom.current();
	}

	List<Location> snow = new ArrayList<>();

	@Override
	public void onStart() {
	}

	@Override
	public void onPass() {
		for (Location l : snow) {
			if (rnd.nextFloat() > (float) .5f / (float) snow.size())
				continue;
			l.getWorld().spawnParticle(rnd.nextBoolean() ? Particle.SNOW_SHOVEL : Particle.SNOWBALL, l, rnd.nextInt(5),
					.5, 1, .5);
			l.getWorld().playSound(l, Sound.BLOCK_SNOW_BREAK, (float) l.distanceSquared(generator.origin) / 2.0f, .5f);
		}
	}

	@Override
	public void onComplete() {
	}

	@Override
	public void onPlace(TreeBlock tb) {
		Block block = tb.getTargetLocation(generator.origin).getBlock();
		if (!block.getRelative(BlockFace.UP).getType().isAir())
			return;
		if (!block.getType().isSolid())
			return;

		if (rnd.nextFloat() > .8f)
			return;

		int snow = 0;
		int count = 0;

		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockData b = block.getLocation().clone().add(x, 1, z).getBlock().getBlockData();
				if (!(b instanceof Snow)) {
					continue;
				}
				snow += ((Snow) b).getLayers();
				count++;
			}
		}
		if (count > 0) {
			snow /= count;
			if (rnd.nextFloat() > ((float) 8 / (float) snow + .1f) || rnd.nextFloat() > .8f)
				return;
		} else {
			if (rnd.nextFloat() > .5f)
				return;
		}

		int level = snow + rnd.nextInt(-1, 2);
		if (level <= 0)
			level = 1;
		Block sb = block.getRelative(BlockFace.UP);
		sb.setType(Material.SNOW);
		Snow s = (Snow) sb.getBlockData();
		s.setLayers(level);
		sb.setBlockData(s);
		this.snow.add(sb.getLocation().clone().add(.5, 0, .5));
	}

}
