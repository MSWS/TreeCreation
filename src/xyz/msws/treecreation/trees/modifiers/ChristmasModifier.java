package xyz.msws.treecreation.trees.modifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import xyz.msws.treecreation.data.AbstractTree;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.data.TreeBlock.BlockType;

public class ChristmasModifier implements Modifier {

	private ThreadLocalRandom rnd;

	private Map<Material, Float> leafChances = new HashMap<>(), trunkChances = new HashMap<>();

	public ChristmasModifier() {
		this.rnd = ThreadLocalRandom.current();

		leafChances.put(Material.GLOWSTONE, .3f);
		leafChances.put(Material.RED_WOOL, .15f);
		leafChances.put(Material.GREEN_WOOL, .15f);

		trunkChances.put(Material.COAL_BLOCK, .05f);
		trunkChances.put(Material.COAL_ORE, .10f);
		trunkChances.put(Material.STONE, .15f);

	}

	@Override
	public AbstractTree modify(AbstractTree tree) {
		TreeBlock b = tree.getHighestTreeBlock();

		for (TreeBlock block : tree.getPart(BlockType.LEAF)) {
			double dst = block.getOffset().distanceSquared(b.getOffset());

			if (dst < 8) {
				if (rnd.nextFloat() < dst / 7 || block.equals(b))
					block.modify(Material.GOLD_BLOCK);
			}

			if (rnd.nextFloat() > .8f)
				continue;
			for (Entry<Material, Float> entry : leafChances.entrySet()) {
				if (rnd.nextFloat() > entry.getValue())
					continue;
				block.modify(Bukkit.createBlockData(entry.getKey()));
			}
		}

		for (TreeBlock block : tree.getPart(BlockType.TRUNK)) {
			if (rnd.nextFloat() > .1f)
				continue;
			for (Entry<Material, Float> entry : trunkChances.entrySet()) {
				if (rnd.nextFloat() > entry.getValue())
					continue;
				block.modify(Bukkit.createBlockData(entry.getKey()));
			}
		}

		return tree;
	}

}
