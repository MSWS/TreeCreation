package xyz.msws.treecreation.generate;

import org.bukkit.block.Block;

import xyz.msws.treecreation.api.TreeAPI;

public abstract class GeneratorModifier {
	protected TreeGenerator generator;
	protected TreeAPI plugin;

	public GeneratorModifier(TreeAPI plugin, TreeGenerator generator) {
		this.plugin = plugin;
		this.generator = generator;
	}

	public abstract void onStart();

	public abstract void onPass();

	public void onPlace(Block block) {
	}

	public abstract void onComplete();
}
