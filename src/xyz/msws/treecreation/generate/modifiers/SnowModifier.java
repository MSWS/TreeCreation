package xyz.msws.treecreation.generate.modifiers;

import java.util.ArrayList;
import java.util.Iterator;
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
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.generate.TreeGenerator;

public class SnowModifier extends GeneratorModifier {

	private ThreadLocalRandom rnd;

	public SnowModifier(TreeAPI plugin, TreeGenerator generator) {
		super(plugin, generator);
		this.rnd = ThreadLocalRandom.current();
	}

	List<Location> snow = new ArrayList<>();
	List<Location> radial = new ArrayList<>();
	private double range, rangeSquared;

	@Override
	public void onStart() {
		rangeSquared = generator.getTree().getRadiusSquared() * 4 / 3;
		range = Math.sqrt(rangeSquared);

		for (double x = -range; x < range; x++) {
			for (double z = -range; z < range; z++) {
				Location b = generator.getOrigin().clone().add(x, -1, z);
				if (b.distanceSquared(generator.getOrigin()) > Math.pow(range, 2))
					continue;

				int i = 0;
				while (b.getBlock().getType().isAir() && i < 3) {
					b.subtract(0, 1, 0);
					i++;
				}
				b.add(0, 1, 0);
				radial.add(b);
			}
		}
	}

	@Override
	public void onPass() {
		Iterator<Location> it = radial.iterator();
		while (it.hasNext()) {
			Location l = it.next();
			if (rnd.nextFloat() > .005f) {
				continue;
			}
			if (!l.getBlock().getType().isAir() && !(l.getBlock().getBlockData() instanceof Snow)) {
				it.remove();
				return;
			}
			float snow = getSnow(l.getBlock().getRelative(BlockFace.DOWN));
			double chance = 1 - ((l.distanceSquared(generator.getOrigin()) / rangeSquared));
			chance += snow / (9 * 8);
			if (rnd.nextFloat() > chance)
				continue;

			l.getBlock().setType(Material.SNOW);
			Snow s = (Snow) l.getBlock().getBlockData();
			s.setLayers(Math.min(Math.max((int) (snow / 9 + rnd.nextInt(1, 2)) + s.getLayers(), s.getLayers()),
					s.getMaximumLayers()));
			l.getBlock().setBlockData(s);
		}

		for (Location l : snow) {
			if (rnd.nextFloat() > (float) .5f / (float) snow.size())
				continue;
			l.getWorld().spawnParticle(rnd.nextBoolean() ? Particle.SNOW_SHOVEL : Particle.SNOWBALL, l, rnd.nextInt(5),
					.5, 1, .5);
			l.getWorld().playSound(l, Sound.BLOCK_SNOW_BREAK, (float) l.distanceSquared(generator.getOrigin()) / 2.0f,
					.5f);
		}
	}

	@Override
	public void onComplete() {
	}

	@Override
	public void onPlace(TreeBlock tb) {
		Block block = tb.getTargetLocation(generator.getOrigin()).getBlock();
		if (!block.getRelative(BlockFace.UP).getType().isAir())
			return;
		if (!block.getType().isSolid())
			return;

		if (rnd.nextFloat() > .8f)
			return;

		float snow = getSnow(block) / (9 * 8);

		if (snow > 0) {
			if (rnd.nextFloat() > ((float) 8 / snow + .1f) || rnd.nextFloat() > .8f)
				return;
		} else {
			if (rnd.nextFloat() > .5f)
				return;
		}

		int level = (int) (snow + rnd.nextInt(-1, 2));
		if (level <= 0)
			level = 1;
		Block sb = block.getRelative(BlockFace.UP);
		sb.setType(Material.SNOW);
		Snow s = (Snow) sb.getBlockData();
		s.setLayers(Math.max(Math.min(level, s.getMaximumLayers()), s.getMinimumLayers()));
		sb.setBlockData(s);
		this.snow.add(sb.getLocation().clone().add(.5, 0, .5));
	}

	private int getSnow(Block block) {
		int s = 0;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockData b = block.getLocation().clone().add(x, 1, z).getBlock().getBlockData();
				if (!(b instanceof Snow)) {
					continue;
				}
				s += ((Snow) b).getLayers();
			}
		}

		return s;
	}

}
