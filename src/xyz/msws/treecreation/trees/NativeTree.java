package xyz.msws.treecreation.trees;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.serialization.SerializableAs;

import xyz.msws.treecreation.exceptions.InvalidBlockException;
import xyz.msws.treecreation.trees.TreeBlock.BlockType;

@SerializableAs("NativeTree")
public class NativeTree extends AbstractTree {

	public static NativeTree deserialize(TreeYML yml) {
		return (NativeTree) yml.getObject();
	}

	public static NativeTree deserialize(Map<String, Object> data) {
		NativeTree tree = new NativeTree();

		for (BlockType type : BlockType.values()) {
			Object d = data.get("Blocks." + type.toString());
			if (d == null || !(d instanceof Collection<?>))
				continue;
			Collection<?> col = (Collection<?>) d;
			List<TreeBlock> blocks = new ArrayList<>();
			for (Object c : col) {
				if (!(c instanceof String))
					continue;
				try {
					TreeBlock block = TreeBlock.fromString(c.toString(), type);
					blocks.add(block);
				} catch (InvalidBlockException e) {
					e.printStackTrace();
					continue;
				}
			}
			tree.blocks.put(type, blocks);
		}

		return tree;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> blocks = new HashMap<>();

		for (Entry<BlockType, List<TreeBlock>> entry : this.blocks.entrySet()) {
			List<String> data = new ArrayList<>();
			for (TreeBlock tb : entry.getValue()) {
				data.add(tb.toString(false));
			}
			blocks.put("Blocks." + entry.getKey().toString(), data);
		}

		return blocks;
	}

}
