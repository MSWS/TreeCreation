package xyz.msws.treecreation.trees.modifiers;

import xyz.msws.treecreation.data.AbstractTree;

public interface Modifier {
	AbstractTree modify(AbstractTree tree);
}
