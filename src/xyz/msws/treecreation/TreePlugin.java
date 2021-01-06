package xyz.msws.treecreation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.commands.TreeCommand;
import xyz.msws.treecreation.trees.AbstractTree;
import xyz.msws.treecreation.trees.NativeTree;
import xyz.msws.treecreation.trees.TreeBlock;
import xyz.msws.treecreation.trees.TreeYML;
import xyz.msws.treecreation.utils.MSG;

public class TreePlugin extends JavaPlugin implements TreeAPI {

	private File trees;
	private MSG msg;
	private Map<String, AbstractTree> templates;

	@Override
	public void onLoad() {
		ConfigurationSerialization.registerClass(TreeBlock.class);
		ConfigurationSerialization.registerClass(NativeTree.class);
	}

	@Override
	public void onEnable() {
		trees = new File(getDataFolder(), "trees");
		msg = new MSG();
		templates = new HashMap<>();

		if (!trees.exists())
			trees.mkdirs();

		register(this.getTreeFile(), templates);

		getCommand("tree").setExecutor(new TreeCommand(this));
	}

	public void refreshTreeTemplates() {
		templates = new HashMap<>();
		register(this.getTreeFile(), templates);
	}

	public void addTreeTemplate(String name, AbstractTree tree) {
		templates.put(name, tree);
	}

	@Override
	public File getTreeFile() {
		return trees;
	}

	@Override
	public MSG getMSG() {
		return msg;
	}

	private void register(File file, Map<String, AbstractTree> trees) {
		if (!file.isDirectory()) {
			if (!file.getName().endsWith(".yml"))
				return;
			TreeYML tree = TreeYML.loadConfiguration(file);
			NativeTree t = NativeTree.deserialize(tree);
			if (t == null)
				return;
			trees.put(getMSG().simplify(file.getName().substring(0, file.getName().length() - 4)), t);
			return;
		}
		for (File f : file.listFiles()) {
			register(f, trees);
		}
	}

	@Override
	public Map<String, AbstractTree> getTreeTemplates() {
		return templates;
	}

	@Override
	public void removeTemplate(String name) {
		templates.remove(name);
		delete(name, getTreeFile());
	}

	private void delete(String name, File current) {
		if (current.isDirectory()) {
			for (File f : current.listFiles()) {
				delete(name, f);
			}
			return;
		}

		if (getMSG().simplify(current.getName().substring(0, current.getName().length() - 4)).equals(name)) {
			current.delete();
		}
	}
}
