package xyz.msws.treecreation.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.serialization.SerializableAs;

import xyz.msws.treecreation.trees.TreeBlock.BlockType;

@SerializableAs("NativeTree")
public class NativeTree extends AbstractTree {

	public static NativeTree deserialize(Map<String, Object> data) {
		NativeTree tree = new NativeTree();

		for (Entry<String, Object> d : data.entrySet()) {
			if (!(d.getValue() instanceof TreeBlock))
				continue;
			TreeBlock block = (TreeBlock) d.getValue();
			List<TreeBlock> bs = tree.blocks.getOrDefault(block.getType(), new ArrayList<>());
			tree.blocks.put(block.getType(), bs);
		}

		return tree;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> blocks = new HashMap<>();

		for (Entry<BlockType, List<TreeBlock>> entry : this.blocks.entrySet()) {
			blocks.put("Blocks." + entry.getKey().toString(), entry.getValue());
		}

		return blocks;
	}

}
