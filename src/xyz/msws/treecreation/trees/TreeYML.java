package xyz.msws.treecreation.trees;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Adds support for saving a custom object to the root of a YML file.
 * 
 * Taken from
 * {@link https://www.spigotmc.org/threads/saving-serializable-to-root-node-in-yamlconfiguration.445374/}
 * 
 * @author imodm
 *
 */
public class TreeYML extends YamlConfiguration {
	protected static final String COMMENT_PREFIX = "# ";
	protected static final String BLANK_CONFIG = "{}\n";
	private final DumperOptions yamlOptions = new DumperOptions();
	private final Representer yamlRepresenter = new YamlRepresenter();
	private final Yaml yaml;
	private Object object;

	public TreeYML() {
		this.yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> getObjectAs() {
		try {
			return Optional.of((T) object);
		} catch (ClassCastException ex) {
			return Optional.empty();
		}
	}

	public String saveToString() {
		this.yamlOptions.setIndent(this.options().indent());
		this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		this.yamlOptions.setAllowUnicode(true); // lets allow unicode, no need to do anything else
		this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		String dump = this.yaml.dump(getObject()); // we can just dump the object itself now
		if (dump.equals(BLANK_CONFIG)) {
			dump = "";
		}

		return dump;
	}

	public void loadFromString(String contents) {
		Validate.notNull(contents, "Contents cannot be null");
		Object object = yaml.load(contents);
		setObject(object);
	}

	public static TreeYML loadConfiguration(File file) {
		Validate.notNull(file, "File cannot be null");
		TreeYML config = new TreeYML();
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
		}
		return config;
	}

	public static TreeYML loadConfiguration(Reader reader) {
		Validate.notNull(reader, "Stream cannot be null");
		TreeYML config = new TreeYML();
		try {
			config.load(reader);
		} catch (IOException | InvalidConfigurationException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
		}
		return config;
	}
}
