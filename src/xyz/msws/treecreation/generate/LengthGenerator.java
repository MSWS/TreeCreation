package xyz.msws.treecreation.generate;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.AbstractTree;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.generate.modifiers.GeneratorModifier;

public class LengthGenerator extends TreeGenerator {
	private List<TreeBlock> toBuild;

	public LengthGenerator(TreeAPI plugin, AbstractTree tree, Location origin) {
		super(plugin, tree, origin);
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

		placeBlock(toBuild.get(0));
		toBuild.remove(0);
		return getProgress();
	}

	@Override
	public float getProgress() {
		return (float) ((float) tree.getBlocks().size() - toBuild.size()) / (float) tree.getBlocks().size();
	}

	@Override
	public void onStopped() {
		genModifiers.forEach(GeneratorModifier::onStopped);
		toBuild.forEach(b -> b.place(origin));
		toBuild = null;
	}

	@Override
	public String getName() {
		return "Length Generator";
	}
}
