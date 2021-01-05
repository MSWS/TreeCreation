package xyz.msws.treecreation.capture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.trees.TreeBlock;
import xyz.msws.treecreation.trees.TreeBlock.BlockType;

public class Capturer implements Listener {
	private File target;
	private UUID player;
	private TreeAPI plugin;

	public Capturer(TreeAPI plugin, File target, Player player) {
		this.target = target;
		this.plugin = plugin;
		this.player = player.getUniqueId();

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!(player.getUniqueId().equals(this.player)) || event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Block block = event.getClickedBlock();
		if (block == null || block.getType().isAir()) {
			plugin.getMSG().delayTell(player, 5000, "Capturer", "No valid block targetted.");
			return;
		}

		if (!target.getParentFile().exists()) {
			plugin.getMSG().delayTell(player, 5000, "Capturer", plugin.getMSG().error + "Parent file does not exist.");
			return;
		}

		if (target.exists()) {
			plugin.getMSG().delayTell(player, 5000, "Capturer", plugin.getMSG().error + "Target file already exists.");
			return;
		}

		plugin.getMSG().tell(player, "Capturer", "Capturing tree, please wait...");

		List<TreeBlock> blocks = new ArrayList<>();

		long time = System.currentTimeMillis();
		parseTree(block.getLocation(), null, blocks);

		int size = blocks.size();

		plugin.getMSG().tell(player, "Capturer", "Successfully captured %d block%s. (Took %s%d%sms)", blocks.size(),
				blocks.size() == 1 ? "" : "s", plugin.getMSG().secondary, System.currentTimeMillis() - time,
				plugin.getMSG().primary);

		YamlConfiguration file = YamlConfiguration.loadConfiguration(target);
		Map<BlockType, List<TreeBlock>> bs = new HashMap<>();
		for (TreeBlock tb : blocks) {
			List<TreeBlock> b = bs.getOrDefault(tb.getType(), new ArrayList<>());
			b.add(tb);
			bs.put(tb.getType(), b);
		}

		for (Entry<BlockType, List<TreeBlock>> entry : bs.entrySet()) {
			file.set("Blocks." + entry.getKey().toString(), entry.getValue());
		}

		try {
			file.save(target);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.reverse(blocks);

		new BukkitRunnable() {
			int index = 0;

			@Override
			public void run() {
				for (int i = 0; i < 5 && index + i < blocks.size(); i++) {
					TreeBlock tb = blocks.get(index);
					Location l = tb.getTargetLocation(block.getLocation());

					l.getWorld().playSound(l, l.getBlock().getBlockData().getSoundGroup().getBreakSound(), 0.5f,
							((float) blocks.size() / (float) size) * 2.0f);
					l.getWorld().spawnParticle(Particle.BLOCK_DUST, l, 4, 0, 0, 0, tb.getBlock());
					index++;
				}
				if (index >= blocks.size())
					this.cancel();
			}
		}.runTaskTimer(plugin, 0, 1);

		PlayerInteractEvent.getHandlerList().unregister(this);
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
