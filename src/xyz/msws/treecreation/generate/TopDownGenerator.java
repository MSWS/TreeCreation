package xyz.msws.treecreation.generate;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import xyz.msws.treecreation.trees.AbstractTree;
import xyz.msws.treecreation.trees.TreeBlock;

public class TopDownGenerator extends TreeGenerator {
	private List<TreeBlock> toBuild;

	public TopDownGenerator(AbstractTree tree, Location origin) {
		super(tree, origin);
		toBuild = tree.getBlocks();

		toBuild.sort(new Comparator<TreeBlock>() {
			@Override
			public int compare(TreeBlock a, TreeBlock b) {
				float av = calculate(a), bv = calculate(b);
				return av == bv ? 0 : av > bv ? -1 : 1;
			}

			private float calculate(TreeBlock block) {
				return block.getOffset().getBlockY();
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
