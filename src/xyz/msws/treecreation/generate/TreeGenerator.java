package xyz.msws.treecreation.generate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.AbstractTree;

public abstract class TreeGenerator implements Listener {
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
					LeavesDecayEvent.getHandlerList().unregister(TreeGenerator.this);
					this.cancel();
					return;
				}
				genModifiers.forEach(m -> m.modify(TreeGenerator.this));
			}
		}.runTaskTimer(plugin, 0, period);

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onBlockDecay(LeavesDecayEvent event) {
		Block block = event.getBlock();
		if (tree.getBlocks().parallelStream().anyMatch(b -> b.getTargetLocation(origin).equals(block.getLocation())))
			event.setCancelled(true);
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
