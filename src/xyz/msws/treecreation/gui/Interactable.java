package xyz.msws.treecreation.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import xyz.msws.treecreation.api.TreeAPI;

public abstract class Interactable implements Listener {
	final TreeAPI plugin;
	Inventory inv;
	private UUID player;
	private boolean open = false;

	public Interactable(TreeAPI plugin, UUID player) {
		this.plugin = plugin;
		this.player = player;

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	protected abstract void onClick(InventoryClickEvent event);

	protected abstract void onClose(InventoryCloseEvent event);

	@EventHandler
	public void onClickListen(InventoryClickEvent event) {
		if (!open || !event.getWhoClicked().getUniqueId().equals(player))
			return;
		onClick(event);
	}

	@EventHandler
	public void onCloseListen(InventoryCloseEvent event) {
		if (!open || !event.getPlayer().getUniqueId().equals(player))
			return;
		open = false;
		onClose(event);
	}

	public Inventory getInventory() {
		return inv;
	}

	public boolean openInventory() {
		if (!getPlayer().isOnline())
			return false;
		getPlayer().getPlayer().openInventory(inv);
		open = true;
		return true;
	}

	public boolean isOpen() {
		return open;
	}

	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(player);
	}

}
