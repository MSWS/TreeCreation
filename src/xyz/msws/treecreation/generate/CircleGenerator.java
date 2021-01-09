package xyz.msws.treecreation.generate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import xyz.msws.treecreation.data.AbstractTree;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.generate.modifiers.GeneratorModifier;

public class CircleGenerator extends TreeGenerator {
	private List<TreeBlock> toBuild = new ArrayList<>();
	private Map<Integer, Map<TreeBlock, Vector>> targets = new HashMap<>();
	private double time;
	private int height;

	public CircleGenerator(AbstractTree tree, Location origin) {
		super(tree, origin);
		toBuild = tree.getBlocks();

		toBuild.sort(new Comparator<TreeBlock>() {
			@Override
			public int compare(TreeBlock o1, TreeBlock o2) {
				double a = o1.getOffset().lengthSquared(), b = o2.getOffset().lengthSquared();
				return (a == b) ? 0 : a > b ? -1 : 1;
			}
		});

		height = tree.getHighestTreeBlock().getOffset().getBlockY();
	}

	@Override
	public float pass() {
		if (toBuild == null || toBuild.isEmpty())
			return 1.0f;
		time = (System.currentTimeMillis() - getStartTime()) / 200.0;

		for (int y = height; y >= 0; y--) {
			int fy = y;

			Vector off = new Vector(Math.sin(time + y / 5.0), 0, Math.cos(time + y / 5.0));

			if (!targets.containsKey(y)) {
				TreeBlock next = toBuild.stream().filter(b -> b.getOffset().getBlockY() == fy).findFirst().orElse(null);
				if (next == null) {
					targets.put(y, null);
				} else {
					Map<TreeBlock, Vector> query = new HashMap<>();
					toBuild.remove(next);

					off.normalize().multiply(next.getHorizontalLength());
					off.setY(y);

					query.put(next, off.toBlockVector());
					targets.put(y, query);
				}
			}

			Map<TreeBlock, Vector> vert = targets.get(y);
			if (vert == null)
				continue;

			Iterator<Entry<TreeBlock, Vector>> it = vert.entrySet().iterator();
			Map<TreeBlock, Vector> nextSet = new HashMap<>();
			while (it.hasNext()) {
				off = new BlockVector(Math.sin(time + y / 5.0), 0, Math.cos(time + y / 5.0));
				Entry<TreeBlock, Vector> entry = it.next();
				Vector target = entry.getKey().getOffset(), current = entry.getValue();
				if (target.getBlockX() == current.getBlockX() && target.getBlockZ() == current.getBlockZ()) {
					it.remove();
					placeBlock(entry.getKey());
					continue;
				}

				off.normalize().multiply(entry.getKey().getHorizontalLength());
				off.setY(y);
				off.add(new Vector(.5, 0, .5));

				Block b = origin.clone().add(entry.getValue()).getBlock();
				if (b.getBlockData().equals(entry.getKey().getBlock()))
					b.setType(Material.AIR);

				b = origin.clone().add(off).getBlock();
				if (b.getType().isAir() || !b.getType().isSolid())
					b.setBlockData(entry.getKey().getBlock());
				nextSet.put(entry.getKey(), off);
			}

			if (vert.entrySet().isEmpty() || nextSet.isEmpty()) {
				targets.remove(y);
			} else {
				targets.put(y, nextSet);
			}
		}

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
}
