package xyz.msws.treecreation.trees;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import xyz.msws.treecreation.trees.TreeBlock.BlockType;

public abstract class AbstractTree implements ConfigurationSerializable {
	protected Map<BlockType, List<TreeBlock>> blocks = new HashMap<>();

	public List<TreeBlock> getPart(BlockType type) {
		return blocks.getOrDefault(type, new ArrayList<>());
	}

	public List<TreeBlock> getBlocks() {
		List<TreeBlock> result = new ArrayList<>();
		blocks.values().forEach(list -> result.addAll(list));
		return result;
	}

	public Location getHighestBlockLocation(Location origin) {
		List<TreeBlock> bs = getBlocks();
		if (bs.isEmpty())
			return origin;
		bs.sort(new Comparator<TreeBlock>() {
			@Override
			public int compare(TreeBlock a, TreeBlock b) {

				if (a.getOffset().getY() != b.getOffset().getY()) {
					return a.getOffset().getY() > b.getOffset().getY() ? -1 : 1;
				}

				Vector av = a.getOffset(), bv = b.getOffset();
				double ad = Math.pow(av.getX(), 2) + Math.pow(av.getZ(), 2);
				double bd = Math.pow(bv.getX(), 2) + Math.pow(bv.getZ(), 2);

				return ad == bd ? 0 : ad > bd ? 1 : -1;
			}
		});
		return bs.get(0).getTargetLocation(origin);
	}
}
