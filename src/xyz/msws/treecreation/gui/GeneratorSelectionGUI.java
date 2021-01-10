package xyz.msws.treecreation.gui;

import java.util.UUID;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.AbstractTree;
import xyz.msws.treecreation.generate.GeneratorFactory;

public class GeneratorSelectionGUI extends Interactable {

	private AbstractTree tree;
	private GeneratorFactory factory;

	public GeneratorSelectionGUI(TreeAPI plugin, UUID player, AbstractTree tree) {
		super(plugin, player);
		this.tree = tree;
		this.factory = plugin.getFactory();

	}

	@Override
	protected void onClick(InventoryClickEvent event) {

	}

	@Override
	protected void onClose(InventoryCloseEvent event) {

	}

}
