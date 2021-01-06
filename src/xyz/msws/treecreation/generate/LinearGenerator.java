package xyz.msws.treecreation.generate;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import xyz.msws.treecreation.trees.AbstractTree;
import xyz.msws.treecreation.trees.TreeBlock;

public class LinearGenerator extends TreeGenerator {
	private List<TreeBlock> toBuild;

	public LinearGenerator(AbstractTree tree, Location origin) {
		super(tree, origin);
		toBuild = tree.getBlocks();

		toBuild.sort(new Comparator<TreeBlock>() {
			@Override
			public int compare(TreeBlock o1, TreeBlock o2) {
				double a = o1.getOffset().lengthSquared(), b = o2.getOffset().lengthSquared();
				return (a == b) ? 0 : a > b ? 1 : -1;
			}
		});
	}

	@Override
	public float pass() {
		if (toBuild == null || toBuild.isEmpty())
			return 1.0f;

		Block b = toBuild.get(0).place(origin);
		genModifiers.forEach(gen -> gen.onPlace(b));
		toBuild.remove(0);
		return getProgress();
	}

	@Override
	public float getProgress() {
		return (float) ((float) tree.getBlocks().size() - toBuild.size()) / (float) tree.getBlocks().size();
	}

}
