package xyz.msws.treecreation.capture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.AbstractTree;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.data.TreeBlock.BlockType;
import xyz.msws.treecreation.data.TreeFactory;
import xyz.msws.treecreation.data.TreeYML;

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
		if (!(player.getUniqueId().equals(this.player)))
			return;
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			plugin.getMSG().tell(player, "Capturer", "Cancelled capturing of tree.");
			PlayerInteractEvent.getHandlerList().unregister(this);
		}

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (event.getHand() != EquipmentSlot.HAND)
			return;

		Block block = event.getClickedBlock();
		if (block == null || block.getType().isAir()) {
			plugin.getMSG().delayTell(player, 5000, "Capturer", "No valid block targetted.");
			return;
		}

		if (!target.getParentFile().exists()) {
			plugin.getMSG().delayTell(player, "Capturer",
					plugin.getMSG().error + "Parent file does not exist, creating...");
			target.getParentFile().mkdirs();
			plugin.getMSG().tell(player, "Capturer", "Successfully created parent file.");
		}

		if (target.exists()) {
			plugin.getMSG().tell(player, "Capturer", plugin.getMSG().error + "Target file already exists.");
			return;
		}

		plugin.getMSG().tell(player, "Capturer", "Capturing tree, please wait...");

		long time = System.currentTimeMillis();

		AbstractTree tree = new TreeFactory(block.getLocation()).build();
		plugin.addTreeTemplate(plugin.getMSG().simplify(target.getName().substring(0, target.getName().length() - 4)),
				tree);

		List<TreeBlock> blocks = tree.getBlocks();

		int size = blocks.size();

		plugin.getMSG().tell(player, "Capturer", "Successfully captured %d block%s. (Took %s%d%sms)", blocks.size(),
				blocks.size() == 1 ? "" : "s", plugin.getMSG().secondary, System.currentTimeMillis() - time,
				plugin.getMSG().primary);

		TreeYML file = new TreeYML();
		Map<BlockType, List<String>> bs = new HashMap<>();

		for (BlockType type : BlockType.values()) {
			List<String> ls = new ArrayList<>();
			for (TreeBlock tb : tree.getPart(type)) {
				ls.add(tb.toString(false));
			}
			bs.put(type, ls);
		}

		file.setObject(tree);

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

}
