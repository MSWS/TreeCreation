package xyz.msws.treecreation;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.AbstractTree;

@SuppressWarnings("unused")
public abstract class TreeGenerator {
	protected AbstractTree tree;
	protected Location origin;

	private boolean generated;

	private long startTime, endTime;

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
					this.cancel();
					endTime = System.currentTimeMillis();
				}
			}
		}.runTaskTimer(plugin, 0, period);
	}

	public abstract float pass();

}
