package xyz.msws.treecreation.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import xyz.msws.treecreation.data.TreeBlock.BlockType;
import xyz.msws.treecreation.exceptions.InvalidBlockException;

@SerializableAs("CompressedTree")
public class CompressedTree extends AbstractTree {

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> blocks = new HashMap<>();

		for (BlockType type : BlockType.values()) {
			Map<String, StringJoiner> blockMap = new HashMap<>();
			for (TreeBlock tb : this.getPart(type)) {
				StringJoiner bs = blockMap.getOrDefault(tb.getBlock().getAsString(true), new StringJoiner(";"));
				Vector off = tb.getOffset();
				bs.add(String.format("%d,%d,%d", off.getBlockX(), off.getBlockY(), off.getBlockZ()));
				blockMap.put(tb.getBlock().getAsString(true), bs);
			}
			if (blockMap.isEmpty())
				continue;
			Map<String, String> blockStrings = new HashMap<>();
			blockMap.forEach((entry, value) -> blockStrings.put(entry, value.toString()));
			blocks.put(type.toString(), blockStrings);
		}
		data.put("Blocks", blocks);
		return data;
	}

	public static CompressedTree deserialize(TreeYML yml) {
		return (CompressedTree) yml.getObject();
	}

	public static CompressedTree deserialize(Map<String, Object> data) throws InvalidBlockException {
		if (!data.containsKey("Blocks"))
			throw new InvalidBlockException("No block data");

		CompressedTree tree = new CompressedTree();

		Object keys = data.get("Blocks");
		if (!(keys instanceof LinkedHashMap<?, ?>))
			throw new InvalidBlockException("Malformed block data");

		LinkedHashMap<?, ?> dat = (LinkedHashMap<?, ?>) keys;

		for (BlockType type : BlockType.values()) {
			Object bdm = dat.get(type.toString());
			if (bdm == null || !(bdm instanceof LinkedHashMap<?, ?>))
				continue;

			LinkedHashMap<?, ?> blockMap = (LinkedHashMap<?, ?>) bdm;

			for (Entry<?, ?> entry : blockMap.entrySet()) {
				BlockData blockData = Bukkit.createBlockData(entry.getKey().toString());
				String offsets = entry.getValue().toString();

				for (String s : offsets.split(";")) {
					String[] args = s.split(",");
					if (args.length != 3)
						continue;
					int x = Integer.parseInt(args[0]), y = Integer.parseInt(args[1]), z = Integer.parseInt(args[2]);

					tree.addBlock(new TreeBlock(blockData, new BlockVector(x, y, z), type));
				}
			}
		}
		return tree;
	}

}
