package xyz.msws.treecreation.generate;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.TreeBlock;

public abstract class GeneratorModifier {
	protected TreeGenerator generator;
	protected TreeAPI plugin;

	public GeneratorModifier(TreeAPI plugin, TreeGenerator generator) {
		this.plugin = plugin;
		this.generator = generator;
	}

	public void onStart() {
	}

	public void onPass() {
	}

	public void onPlace(TreeBlock block) {
	}

	public abstract void onComplete();
}
