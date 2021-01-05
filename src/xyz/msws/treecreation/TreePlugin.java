package xyz.msws.treecreation;

import java.io.File;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.msws.treecreation.api.TreeAPI;
import xyz.msws.treecreation.commands.CaptureCommand;
import xyz.msws.treecreation.trees.TreeBlock;
import xyz.msws.treecreation.trees.TreeFactory;
import xyz.msws.treecreation.utils.MSG;

public class TreePlugin extends JavaPlugin implements TreeAPI {

	private File trees;
	private MSG msg;

	@Override
	public void onEnable() {
		trees = new File(getDataFolder(), "trees");
		msg = new MSG();

		if (!trees.exists())
			trees.mkdirs();

		getCommand("capture").setExecutor(new CaptureCommand(this));

		ConfigurationSerialization.registerClass(TreeBlock.class);
	}

	@Override
	public File getTreeFile() {
		return trees;
	}

	@Override
	public MSG getMSG() {
		return msg;
	}
}
