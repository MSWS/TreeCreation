package xyz.msws.treecreation.generate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.events.GeneratorFinishEvent;
import xyz.msws.treecreation.events.GeneratorStartEvent;

/**
 * Monitors and listens to {@link GeneratorStartEvent} and
 * {@link GeneratorFinishEvent} to properly handle server shutdowns before
 * generators are finished
 * 
 * @author imodm
 *
 */
public class GeneratorMonitor implements Listener {
	private List<TreeGenerator> generators = new ArrayList<>();

	public GeneratorMonitor(TreeAPI plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void onDisable() {
		generators.forEach(TreeGenerator::onStopped);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onGenStart(GeneratorStartEvent event) {
		if (event.isCancelled())
			return;
		generators.add(event.getGenerator());
	}

	@EventHandler
	public void onGenFinish(GeneratorFinishEvent event) {
		generators.remove(event.getGenerator());
	}
}
