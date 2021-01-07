package xyz.msws.treecreation.events;

import org.bukkit.event.HandlerList;

import xyz.msws.treecreation.generate.TreeGenerator;

/**
 * Represents when a {@link TreeGenerator} has finished generating the tree
 * 
 * @author imodm
 *
 */
public class GeneratorFinishEvent extends TreeEvent {

	private TreeGenerator generator;

	public GeneratorFinishEvent(TreeGenerator generator) {
		super(generator.getTree());
		this.generator = generator;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public TreeGenerator getGenerator() {
		return generator;
	}

}
