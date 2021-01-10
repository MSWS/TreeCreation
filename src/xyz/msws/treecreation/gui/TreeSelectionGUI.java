package xyz.msws.treecreation.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.AbstractTree;
import xyz.msws.treecreation.generate.Pager;

public class TreeSelectionGUI extends Interactable {

	private int page = 0, maxItems = 45;
	private Pager pager;

	public TreeSelectionGUI(TreeAPI plugin, UUID player) {
		super(plugin, player);

		inv = Bukkit.createInventory(null, (int) (Math.ceil(plugin.getTreeTemplates().size() / 9.0) * 9),
				"Tree Selection");
		refreshGUI();
	}

	private void refreshGUI() {
		List<ItemStack> items = new ArrayList<>();
		for (AbstractTree tree : plugin.getTreeTemplates().values())
			items.add(tree.getItemStack());
		this.pager = new Pager(items, inv);
		pager.fill();
	}

	@Override
	protected void onClick(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if (item == null || item.getType().isAir())
			return;
		int index = page * maxItems + event.getRawSlot();

		AbstractTree tree = plugin.getTreeTemplates().values().toArray(new AbstractTree[0])[index];

		plugin.getMSG().tell(event.getWhoClicked(), "Tree GUI", "You clicked %s", tree.getDescription().get(0));
		plugin.getMSG().tell(event.getWhoClicked(), "Tree Guess", "You clicked %s",
				pager.getClickedItem(event.getRawSlot()) + "");

	}

	@Override
	protected void onClose(InventoryCloseEvent event) {
		plugin.getMSG().tell(event.getPlayer(), "Tree GUI", "You exited");
	}

}