package xyz.msws.treecreation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.commands.TreeCommand;
import xyz.msws.treecreation.data.AbstractTree;
import xyz.msws.treecreation.data.CompressedTree;
import xyz.msws.treecreation.data.NativeTree;
import xyz.msws.treecreation.data.TreeBlock;
import xyz.msws.treecreation.data.TreeYML;
import xyz.msws.treecreation.generate.GeneratorFactory;
import xyz.msws.treecreation.generate.GeneratorMonitor;
import xyz.msws.treecreation.utils.MSG;

public class TreePlugin extends JavaPlugin implements TreeAPI {

	private File trees;
	private MSG msg;
	private Map<String, AbstractTree> templates;
	private GeneratorMonitor monitor;
	private GeneratorFactory genFac;

	@Override
	public void onLoad() {
		ConfigurationSerialization.registerClass(TreeBlock.class);
		ConfigurationSerialization.registerClass(NativeTree.class);
		ConfigurationSerialization.registerClass(CompressedTree.class);
	}

	@Override
	public void onEnable() {
		trees = new File(getDataFolder(), "trees");
		msg = new MSG();
		templates = new HashMap<>();

		if (!trees.exists())
			trees.mkdirs();

		monitor = new GeneratorMonitor(this);

		register(this.getTreeFile(), templates);

		genFac = new GeneratorFactory(this);
		getCommand("tree").setExecutor(new TreeCommand(this));
	}

	@Override
	public void onDisable() {
		monitor.onDisable();
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
			AbstractTree t = (AbstractTree) tree.getObject();
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

	@Override
	public GeneratorFactory getFactory() {
		return genFac;
	}
}
