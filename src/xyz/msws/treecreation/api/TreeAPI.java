package xyz.msws.treecreation.api;

import java.io.File;

import org.bukkit.plugin.Plugin;

import xyz.msws.treecreation.utils.MSG;

public interface TreeAPI extends Plugin {

	File getTreeFile();

	MSG getMSG();
}
