package xyz.msws.treecreation.api;

import java.io.File;
import java.util.Map;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import xyz.msws.treecreation.data.AbstractTree;
import xyz.msws.treecreation.generate.GeneratorFactory;
import xyz.msws.treecreation.utils.MSG;

public interface TreeAPI extends Plugin {
	PluginCommand getCommand(String cmd);

	File getTreeFile();

	Map<String, AbstractTree> getTreeTemplates();

	void addTreeTemplate(String name, AbstractTree tree);

	void refreshTreeTemplates();

	void removeTemplate(String name);

	GeneratorFactory getFactory();

	MSG getMSG();
}
