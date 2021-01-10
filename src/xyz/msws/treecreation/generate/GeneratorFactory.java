package xyz.msws.treecreation.generate;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.data.AbstractTree;

public class GeneratorFactory {
	private TreeAPI plugin;
	private Map<String, Class<? extends TreeGenerator>> generators = new HashMap<>();

	public GeneratorFactory(TreeAPI plugin) {
		this.plugin = plugin;
	}

	public void addGenerator(String id, Class<? extends TreeGenerator> clazz) {
		generators.put(id, clazz);
	}

	public Class<? extends TreeGenerator> getGenerator(String id) {
		return generators.get(id);
	}

	public Set<String> getGeneratorIDs() {
		return generators.keySet();
	}

	public Map<String, Class<? extends TreeGenerator>> getGenerators() {
		return generators;
	}

	@SuppressWarnings("unchecked")
	public <T extends TreeGenerator> T newInstance(String id, AbstractTree tree, Location location) {
		Class<? extends TreeGenerator> result = getGenerator(id);
		if (result == null)
			return null;

		try {
			Object r = result.getConstructor(TreeAPI.class, AbstractTree.class, Location.class).newInstance(plugin,
					tree, location);
			if (!(r instanceof TreeGenerator))
				return null;
			return (T) r;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}