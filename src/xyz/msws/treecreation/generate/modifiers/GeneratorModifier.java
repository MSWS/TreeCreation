package xyz.msws.treecreation.generate.modifiers;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.generate.TreeGenerator;

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
	
	public void onStopped() {
	}

	public abstract void onComplete();
}
