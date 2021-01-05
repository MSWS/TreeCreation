package xyz.msws.treecreation.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

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
}
