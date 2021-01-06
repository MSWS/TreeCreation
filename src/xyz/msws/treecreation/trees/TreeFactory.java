package xyz.msws.treecreation.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.msws.treecreation.trees.TreeBlock.BlockType;
import xyz.msws.treecreation.trees.modifiers.Modifier;

public class TreeFactory {
	private final AbstractTree rawTree;
	private AbstractTree tree;

	private List<Modifier> modifiers = new ArrayList<>();

	public TreeFactory(AbstractTree tree) {
		this.tree = tree;
		this.rawTree = tree;
	}

	public TreeFactory(Location origin) {
		this.rawTree = new NativeTree();
		this.tree = new NativeTree();
		List<TreeBlock> blocks = new ArrayList<>();
		Map<BlockType, List<TreeBlock>> map = new HashMap<>();

		parseTree(origin, origin, blocks);

		for (TreeBlock tb : blocks) {
			List<TreeBlock> bs = map.getOrDefault(tb.getType(), new ArrayList<>());
			bs.add(tb);
			map.put(tb.getType(), bs);
		}

		this.rawTree.blocks = map;
		this.tree.blocks = map;
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

	private void parseTree(Location origin, Location current, List<TreeBlock> list) {
		if (current == null)
			current = origin;
		if (list == null)
			list = new ArrayList<>();
		if (list.size() > 500)
			return;
		for (int x = -1; x <= 1; x++) {
			for (int y = 0; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					Location c = current.clone().add(x, y, z);
					TreeBlock block = new TreeBlock(c.getBlock().getBlockData(),
							c.clone().toVector().subtract(origin.clone().toVector()));
					Material m = block.getBlock().getMaterial();
					if (list.contains(block) || m.isAir())
						continue;
					if (m == Material.GRASS || m == Material.GRASS_BLOCK || m == Material.DIRT || m == Material.STONE)
						continue;
					if (c.getBlock().isLiquid())
						continue;

					list.add(block);
					parseTree(origin, c, list);
				}
			}
		}
	}
}