package xyz.msws.treecreation.trees;

import java.util.ArrayList;
import java.util.List;

import xyz.msws.treecreation.trees.modifiers.Modifier;

public class TreeFactory {
	private final AbstractTree rawTree;
	private AbstractTree tree;

	private List<Modifier> modifiers = new ArrayList<>();

	public TreeFactory(AbstractTree tree) {
		this.tree = tree;
		this.rawTree = tree;
	}

	public TreeFactory modify(Modifier modify) {
		this.tree = modify.modify(tree);
		modifiers.add(modify);
		return this;
	}

	public AbstractTree getOriginalTree() {
		return rawTree;
	}

	public AbstractTree build() {
		return tree;
	}
}