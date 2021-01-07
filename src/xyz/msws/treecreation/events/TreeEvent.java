package xyz.msws.treecreation.events;

import org.bukkit.event.Event;

import xyz.msws.treecreation.data.AbstractTree;

/**
 * Represents an event involving an {@link AbstractTree}
 * 
 * @author imodm
 *
 */
public abstract class TreeEvent extends Event {

	private AbstractTree tree;

	public TreeEvent(AbstractTree tree) {
		this.tree = tree;
	}

	/**
	 * Gets the tree invovled in the event
	 * 
	 * @return The tree invovled in the event
	 */
	public AbstractTree getTree() {
		return tree;
	}

}
