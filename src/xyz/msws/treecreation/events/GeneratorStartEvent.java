package xyz.msws.treecreation.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import xyz.msws.treecreation.generate.TreeGenerator;

/**
 * Represents when a generator is triggered to begin
 * 
 * @author imodm
 *
 */
public class GeneratorStartEvent extends TreeEvent implements Cancellable {

	private TreeGenerator generator;
	private boolean cancel = false;

	public GeneratorStartEvent(TreeGenerator generator) {
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

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
