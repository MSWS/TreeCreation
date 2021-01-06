package xyz.msws.treecreation.trees;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import xyz.msws.treecreation.trees.TreeBlock.BlockType;

public abstract class AbstractTree implements ConfigurationSerializable {
	Map<BlockType, List<TreeBlock>> blocks = new HashMap<>();

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

				Vector av = a.getOffset().clone(), bv = b.getOffset().clone();
				av.setY(0);
				bv.setY(0);
				double ad = av.lengthSquared();
				double bd = bv.lengthSquared();
				return ad == bd ? 0 : ad > bd ? 1 : -1;
			}
		});
		return bs.get(0).getTargetLocation(origin);
	}

	public TreeBlock getHighestTreeBlock() {
		List<TreeBlock> bs = getBlocks();
		if (bs.isEmpty())
			return null;
		bs.sort(new Comparator<TreeBlock>() {
			@Override
			public int compare(TreeBlock a, TreeBlock b) {

				if (a.getOffset().getY() != b.getOffset().getY()) {
					return a.getOffset().getY() > b.getOffset().getY() ? -1 : 1;
				}

				Vector av = a.getOffset().clone(), bv = b.getOffset().clone();
				av.setY(0);
				bv.setY(0);
				double ad = av.lengthSquared();
				double bd = bv.lengthSquared();
				return ad == bd ? 0 : ad > bd ? 1 : -1;
			}
		});
		return bs.get(0);
	}

	public Location getFurthestBlockLocation(Location origin) {
		List<TreeBlock> bs = getBlocks();
		if (bs.isEmpty())
			return origin;
		bs.sort(new Comparator<TreeBlock>() {
			@Override
			public int compare(TreeBlock a, TreeBlock b) {
				Vector av = a.getOffset().clone(), bv = b.getOffset().clone();
				av.setY(0);
				bv.setY(0);
				double ad = av.lengthSquared();
				double bd = bv.lengthSquared();
				return ad == bd ? 0 : ad > bd ? -1 : 1;
			}
		});
		return bs.get(0).getTargetLocation(origin);
	}

	public void addBlock(TreeBlock block) {
		List<TreeBlock> blocks = this.blocks.getOrDefault(block.getType(), new ArrayList<>());
		blocks.add(block);
		this.blocks.put(block.getType(), blocks);
	}

	public Map<BlockType, List<TreeBlock>> getBlocksMap() {
		return blocks;
	}

	public String getDescription() {
		Map<Material, Integer> blocks = new HashMap<>();
		for (List<TreeBlock> bs : this.blocks.values()) {
			for (TreeBlock b : bs) {
				blocks.put(b.getBlock().getMaterial(), blocks.getOrDefault(b.getBlock().getMaterial(), 0) + 1);
			}
		}

		List<Entry<Material, Integer>> sorted = new ArrayList<Map.Entry<Material, Integer>>();
		sorted = new LinkedList<>(blocks.entrySet());

		sorted.sort(new Comparator<Entry<Material, Integer>>() {
			@Override
			public int compare(Entry<Material, Integer> a, Entry<Material, Integer> b) {
				return a.getValue() == b.getValue() ? 0 : a.getValue() > b.getValue() ? 1 : -1;
			}
		});

		StringJoiner result = new StringJoiner("\n");
		for (Entry<Material, Integer> entry : sorted) {
			result.add(ChatColor.YELLOW + "" + entry.getValue() + "x " + ChatColor.GRAY + entry.getKey());
		}
		return result.toString();
	}
}
