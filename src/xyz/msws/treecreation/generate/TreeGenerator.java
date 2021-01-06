package xyz.msws.treecreation.generate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.AbstractTree;

@SuppressWarnings("unused")
public abstract class TreeGenerator {
	protected AbstractTree tree;
	protected Location origin;

	private boolean generated;

	private long startTime, endTime;

	protected List<GeneratorModifier> genModifiers = new ArrayList<>();

	public TreeGenerator(AbstractTree tree, Location origin) {
		this.tree = tree;
		this.origin = origin;
	}

	public void generate(TreeAPI plugin, long period) {
		this.startTime = System.currentTimeMillis();
		new BukkitRunnable() {
			@Override
			public void run() {
				if (pass() >= 1f) {
					endTime = System.currentTimeMillis();
					this.cancel();
					return;
				}
				genModifiers.forEach(m -> m.modify(TreeGenerator.this));
			}
		}.runTaskTimer(plugin, 0, period);
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public boolean finished() {
		return generated;
	}

	public abstract float pass();

}
