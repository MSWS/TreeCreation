package xyz.msws.treecreation.generate.modifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.generate.TreeGenerator;

public class ChristmasModifier extends GeneratorModifier {

	private ThreadLocalRandom rnd;
	private int height;
	private double range;
	private static final List<Color> colorOptions = Arrays.asList(Color.RED, Color.GREEN, Color.WHITE,
			Color.fromRGB(179, 0, 12), Color.fromRGB(228, 0, 16), Color.fromRGB(31, 213, 55),
			Color.fromRGB(137, 52, 187), Color.fromRGB(10, 83, 222), Color.fromRGB(216, 216, 216));
	private int maxFireworks = 5;

	private List<Location> fws = new ArrayList<>();

	private static List<FireworkEffect> effects = new ArrayList<>();

	public ChristmasModifier(TreeAPI plugin, TreeGenerator generator) {
		super(plugin, generator);
		this.rnd = ThreadLocalRandom.current();
		this.height = generator.getTree().getHighestBlockLocation(generator.getOrigin()).getBlockY()
				- generator.getOrigin().getBlockY();
		this.range = generator.getTree().getRadius();

		if (effects.isEmpty()) {
			for (int i = 0; i < 100; i++) {
				Builder builder = FireworkEffect.builder();
				builder.with(Type.values()[rnd.nextInt(Type.values().length)]);
				Color[] colors = new Color[rnd.nextInt(1, 6)];
				for (int j = 0; j < colors.length; j++) {
					colors[j] = colorOptions.get(rnd.nextInt(colorOptions.size()));
				}

				builder.withColor(colors);
				colors = new Color[rnd.nextInt(1, 6)];

				for (int j = 0; j < colors.length; j++) {
					colors[j] = colorOptions.get(rnd.nextInt(colorOptions.size()));
				}
				builder.withFade(colors);
				if (rnd.nextBoolean())
					builder.withTrail();
				if (rnd.nextBoolean())
					builder.withFlicker();
				effects.add(builder.build());
			}
		}
	}

	@Override
	public void onComplete() {
	}

	@Override
	public void onPass() {
		spawnFireworkParticles();
		spawnSnowballParticles();
		processFireworkLocations();

		tryFirework();

	}

	private void spawnFireworkParticles() {
		double off = (System.currentTimeMillis() - generator.getStartTime()) / (500 - (generator.getProgress() * 400));

		for (double t = 0; t <= height; t += .5) {
			Location l = generator.getOrigin().clone().add(Math.cos(off + t) * range, t, Math.sin(off + t) * range);
			l.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 0, 0, 0, 0);
		}
	}

	private void spawnSnowballParticles() {
		double off = (System.currentTimeMillis() - generator.getStartTime()) / (200 - (generator.getProgress() * 100));

		for (double t = 0; t <= Math.PI * 2; t += 1) {
			for (double r = 0; r <= range; r += range / 2) {
				Location l = generator.getOrigin().clone().add(Math.cos(t + off / 5) * (Math.cos(off / 10 + r) * range),
						height, Math.sin(t + off / 5) * (Math.cos(off / 10 + r) * range));
				l.getWorld().spawnParticle(Particle.SNOWBALL, l, 5);
			}
		}
	}

	private void tryFirework() {
		if (rnd.nextFloat() < fws.size() / maxFireworks || rnd.nextFloat() > .05f)
			return;
		Location origin = generator.getOrigin();
		Location l = origin.clone();
		Location target = l.add(rnd.nextDouble(-range / 1.5, range / 1.5), 0,
				rnd.nextDouble(-range / 1.5, range / 1.5));
		if (target.getWorld().getHighestBlockAt(target).getY() > origin.clone().getY() + height / 5)
			return;
		target = target.getWorld().getHighestBlockAt(target).getLocation();
		fws.add(target);
	}

	private void processFireworkLocations() {
		Iterator<Location> it = fws.iterator();
		double inc = .5;
		while (it.hasNext()) {
			Location l = it.next();
			Location block = l.getWorld().getHighestBlockAt(l).getLocation();
			int h = l.getBlockY() - block.getBlockY();

			if (h > (double) height * 4 / 3 || (rnd.nextFloat() < .05f && h > height / 2)) {
				Firework work = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
				FireworkMeta meta = work.getFireworkMeta();
				for (int i = 0; i < Math.min(rnd.nextInt(1, effects.size()), 5); i++) {
					FireworkEffect e = effects.get(rnd.nextInt(effects.size()));
					meta.addEffect(e);
				}
				work.setFireworkMeta(meta);
				work.detonate();
				it.remove();
				return;
			}

			for (double y = l.getY(); y < l.getY() + inc; y += .1) {
				Particle.DustOptions data = new Particle.DustOptions(colorOptions.get(rnd.nextInt(colorOptions.size())),
						rnd.nextFloat() * 3);
				Location n = l.clone();
				n.setY(y);
				n.getWorld().spawnParticle(Particle.REDSTONE, n, 0, 0, 0, 0, data);
				n.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, n, 0, 0, 0, 0);
			}

			l.setY(l.getY() + inc);
		}
	}

	@Override
	public void onPlace(TreeBlock block) {
		Location l = block.getTargetLocation(generator.getOrigin()).getBlock().getLocation().clone();
		l.add(.5, .5, .5);
		l.getWorld().spawnParticle(Particle.TOTEM, l, rnd.nextInt(10, 20), .5, .5, .5);

		if (rnd.nextFloat() > .2f)
			return;

		l.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 1);
		l.getWorld().playSound(l, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 2f, generator.getProgress() * 2f);
	}

}
