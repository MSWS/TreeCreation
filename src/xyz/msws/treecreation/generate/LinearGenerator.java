package xyz.msws.treecreation.generate;

import java.util.List;

import org.bukkit.Location;

import xyz.msws.treecreation.trees.AbstractTree;
import xyz.msws.treecreation.trees.TreeBlock;

public class LinearGenerator extends TreeGenerator {

	private List<TreeBlock> toBuild;

	public LinearGenerator(AbstractTree tree, Location origin) {
		super(tree, origin);
		toBuild = tree.getBlocks();
	}

	@Override
	public float pass() {
		if (toBuild == null || toBuild.isEmpty())
			return 1.0f;

		toBuild.get(0).place(origin);
		toBuild.remove(0);
		return (float) toBuild.size() / (float) tree.getBlocks().size();
	}

}
