package xyz.msws.treecreation.generate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
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
		genModifiers.forEach(GeneratorModifier::onStart);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (pass() >= 1f) {
					endTime = System.currentTimeMillis();
					genModifiers.forEach(GeneratorModifier::onComplete);
					LeavesDecayEvent.getHandlerList().unregister(TreeGenerator.this);
					BlockPhysicsEvent.getHandlerList().unregister(TreeGenerator.this);
					this.cancel();
					return;
				}
				genModifiers.forEach(GeneratorModifier::onPass);
			}
		}.runTaskTimer(plugin, 0, period);

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void addModifier(GeneratorModifier modifier) {
		this.genModifiers.add(modifier);
	}

	@EventHandler
	public void onBlockDecay(LeavesDecayEvent event) {
		Block block = event.getBlock();
		if (!contains(block.getLocation()))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockPhysicsEvent event) {
		Block block = event.getBlock();
		if (!block.getWorld().equals(origin.getWorld()))
			return;
		if (contains(block.getLocation())) {
			event.setCancelled(true);
		}
	}

	public boolean contains(Location l) {
		return (tree.getBlocks().parallelStream().anyMatch(b -> b.getTargetLocation(origin).equals(l)));
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

	public abstract float getProgress();

}
