package xyz.msws.treecreation.data;

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

import xyz.msws.treecreation.data.TreeBlock.BlockType;

/**
 * Represents a basic tree structure that holds block types and data
 * 
 * @author imodm
 *
 */
public abstract class AbstractTree implements ConfigurationSerializable {
	Map<BlockType, List<TreeBlock>> blocks = new HashMap<>();

	/**
	 * Returns all {@link TreeBlock} that are part of the {@link BlockType}
	 * 
	 * @param type The {@link BlockType} to get blocks from
	 * @return A List of {@link TreeBlock} or an empty arraylist
	 */
	public List<TreeBlock> getPart(BlockType type) {
		return blocks.getOrDefault(type, new ArrayList<>());
	}

	/**
	 * Returns all {@link TreeBlock} within the tree
	 * 
	 * @return A list of all blocks in an undefined order
	 */
	public List<TreeBlock> getBlocks() {
		List<TreeBlock> result = new ArrayList<>();
		blocks.values().forEach(list -> result.addAll(list));
		return result;
	}

	/**
	 * Gets the highest and centermost block
	 * 
	 * @param origin Where the tree's origin is
	 * @return The highest and centermost block if it exists, origin otherwise
	 */
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

	/**
	 * Gets the highest {@link TreeBlock} within the tree structure
	 * 
	 * @return The highest {@link TreeBlock} if it exists, null otherwise.
	 */
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

	/**
	 * Gets the furthest block from the tree's origin
	 * 
	 * @param origin The origin of the tree
	 * @return The futhest block if it exists, the origin otherwise.
	 */
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

	/**
	 * Adds a block to the tree's structure
	 * 
	 * @param block The {@link TreeBlock} to add
	 */
	public void addBlock(TreeBlock block) {
		List<TreeBlock> blocks = this.blocks.getOrDefault(block.getType(), new ArrayList<>());
		blocks.add(block);
		this.blocks.put(block.getType(), blocks);
	}

	/**
	 * Gets the underlying data structure for the tree
	 * 
	 * @return The map of data
	 */
	public Map<BlockType, List<TreeBlock>> getBlocksMap() {
		return blocks;
	}

	/**
	 * Gets a basic description of what blocks makeup the tree TODO
	 * 
	 * @return A multi-line string describing the tree
	 */
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

	/**
	 * Overrides native implementation to properly handle blocks
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof AbstractTree))
			return false;
		AbstractTree tree = (AbstractTree) obj;
		return tree.getBlocks().equals(this.getBlocks());
	}
}
