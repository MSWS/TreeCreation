package xyz.msws.treecreation.generate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Pager {
	private List<ItemStack> items;
	private Inventory inv;
	private int page;
	private List<Integer> slots = new ArrayList<>();

	public Pager(List<ItemStack> items, Inventory inv) {
		this.items = items;
		this.page = 0;
		this.inv = inv;
		for (int i = 0; i < inv.getSize(); i++)
			slots.add(i);
		slots.removeIf(s -> s >= inv.getSize());
	}

	public ItemStack getClickedItem(int rawSlot) {
		return items.get(page * slots.size() + slots.get(rawSlot % slots.size()));
	}

	public void clearSlots() {
		slots.clear();
	}

	public void addSlot(int... ints) {
		for (int i : ints) {
			if (i > inv.getSize())
				continue;
			slots.add(i);
		}
	}

	public void fill() {
		int start = page * slots.size();
		for (int slot : slots) {
			inv.setItem(slot, items.get(start));
			start++;
			if (start >= items.size())
				break;
		}
	}

	public void nextPage() {
		page++;
		fill();
	}

	public int getPages() {
		return (int) Math.ceil(items.size() / slots.size());
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}
}
