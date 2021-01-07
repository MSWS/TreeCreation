package xyz.msws.treecreation.data;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import xyz.msws.treecreation.exceptions.InvalidBlockException;
import xyz.msws.treecreation.trees.modifiers.Modifier;

/**
 * Responsible for encapsulating a tree's block type, offset, and data.
 * 
 * @author imodm
 *
 */
@SerializableAs("TreeBlock")
public class TreeBlock implements ConfigurationSerializable {
	private BlockType type = BlockType.DEFAULT;
	private BlockData data;
	private Vector offset;

	private final static EnumSet<Material> trunks = EnumSet.noneOf(Material.class),
			leaves = EnumSet.noneOf(Material.class), decor = EnumSet.noneOf(Material.class);

	static {
		decor.addAll(Tag.WOOL.getValues());
		decor.addAll(Tag.BANNERS.getValues());
		leaves.addAll(Tag.LEAVES.getValues());
		trunks.addAll(Tag.LOGS.getValues());
		trunks.addAll(Tag.PLANKS.getValues());

		for (Material m : Material.values()) {
			if (m.toString().contains("GLASS"))
				decor.add(m);
		}

		decor.addAll(Arrays.asList(Material.SKELETON_SKULL, Material.SKELETON_WALL_SKULL));
	}

	/**
	 * Categorizes blocks based on where they are on the tree
	 * 
	 * @author imodm
	 *
	 */
	public enum BlockType {
		DEFAULT, TRUNK, LEAF, DECOR, OTHER;
	}

	/**
	 * Clones and adds the offset and returns the result
	 * 
	 * @param origin
	 * @return
	 */
	public Location getTargetLocation(Location origin) {
		return origin.clone().add(offset);
	}

	/**
	 * Returns the offset that the block has
	 * 
	 * @return
	 */
	public Vector getOffset() {
		return offset;
	}

	/**
	 * Creates a new TreeBlock and attempts to assign a {@link BlockType} according
	 * to {@link TreeBlock#guessType(Material)}
	 * 
	 * @param mat    Block material to use, use tertiary constructor to specify data
	 * @param offset Offset location relative to origin
	 */
	public TreeBlock(Material mat, Vector offset) {
		this(Bukkit.createBlockData(mat), offset);
	}

	/**
	 * Creates a new TreeBlock and attempts to assign a {@link BlockType} according
	 * to {@link TreeBlock#guessType(Material)}
	 * 
	 * @param mat    Block data to use
	 * @param offset Offset location relative to origin
	 */
	public TreeBlock(BlockData data, Vector offset) {
		this(data, offset, guessType(data.getMaterial()));
		this.data = data;
		this.offset = offset;
		this.type = guessType(data.getMaterial());
	}

	/**
	 * Creates a new TreeBlock and attempts to assign a {@link BlockType} according
	 * to {@link TreeBlock#guessType(Material)}
	 * 
	 * @param mat    Block data to use
	 * @param offset Offset location relative to origin
	 * @param type   Block type
	 */
	public TreeBlock(BlockData data, Vector offset, BlockType type) {
		this.data = data;
		this.offset = offset;
		this.type = type;
	}

	/**
	 * Provides support for <b>temporary</b> modificastion from {@link Modifier}
	 * 
	 * @param data
	 */
	public void modify(BlockData data) {
		this.data = data;
	}

	/**
	 * Provides support for <b>temporary</b> modificastion from {@link Modifier}
	 * 
	 * @param data
	 */
	public void modify(Material type) {
		modify(Bukkit.createBlockData(type));
	}

	/**
	 * Constructor to support {@link ConfigurationSerializable}
	 * 
	 * @param data
	 * @throws InvalidBlockException
	 */
	public static TreeBlock deserialize(Map<String, Object> data) throws InvalidBlockException {
		for (String s : new String[] { "type", "offset", "data" }) {
			if (!data.containsKey("type"))
				throw new InvalidBlockException("Block " + s + " is missing");
		}

		String current = (String) data.get("type");
		BlockType type = BlockType.DEFAULT;
		try {
			type = BlockType.valueOf(current == null ? current : "DEFAULT");
		} catch (IllegalFormatException e) {
			type = BlockType.DEFAULT;
			Bukkit.getLogger().log(Level.SEVERE, "Could not parse block type " + type);
		}

		current = (String) data.get("offset");

		if (current == null || current.split(",").length != 3)
			throw new InvalidBlockException("Block offset is invalid");
		double x = Double.parseDouble(current.split(",")[0]), y = Double.parseDouble(current.split(",")[1]),
				z = Double.parseDouble(current.split(",")[2]);

		Vector offset = new Vector(x, y, z);

		current = (String) data.get("data");
		BlockData blockData = Bukkit.createBlockData(current);
		return new TreeBlock(blockData, offset, type);
	}

	/**
	 * Attempts to match the Material type to the probable part of the Tree If no
	 * valid match is found {@link BlockType#DEFAULT} is returned.
	 * 
	 * @param mat
	 * @return
	 */
	public static BlockType guessType(Material mat) {
		if (leaves.contains(mat))
			return BlockType.LEAF;
		if (trunks.contains(mat))
			return BlockType.TRUNK;
		return BlockType.DEFAULT;
	}

	/**
	 * Gets the {@link BlockType}
	 * 
	 * @return
	 */
	public BlockType getType() {
		return type;
	}

	/**
	 * Gets the blockdata
	 * 
	 * @return
	 */
	public BlockData getBlock() {
		return data;
	}

	/**
	 * Places the block relative to the origin
	 * 
	 * @param origin
	 */
	public Block place(Location origin) {
		Location target = getTargetLocation(origin);
		target.getBlock().setBlockData(data);
		return target.getBlock();
	}

	/**
	 * Returns a string representation of the block
	 */
	@Override
	public String toString() {
		return toString(true);
	}

	/**
	 * Returns a string representation of the block, depending on the data structure
	 * it may be desirable to ommit the {@link BlockType}
	 * 
	 * @param includeType True to include the blocktype, false to exclude
	 * @return
	 */
	public String toString(boolean includeType) {
		return String.format(includeType ? "TreeBlock{%s;%s;%s}" : "TreeBlock{%s;%s}", this.data.getAsString(true),
				this.offset.toString(), this.type.toString());
	}

	/**
	 * Parses and returns a {@link TreeBlock} instance from a string. See
	 * {@link TreeBlock#toString()}
	 * 
	 * @param s String to parse
	 * @return
	 * @throws InvalidBlockException
	 */
	public static TreeBlock fromString(String s) throws InvalidBlockException {
		return fromString(s, null);
	}

	/**
	 * Parses and returns a {@link TreeBlock} instance from a string. See
	 * {@link TreeBlock#toString()}
	 * 
	 * @param s String to parse
	 * @return
	 * @throws InvalidBlockException
	 */
	public static TreeBlock fromString(String s, BlockType type) throws InvalidBlockException {
		if (s == null || s.isEmpty() || !s.startsWith("TreeBlock{"))
			throw new InvalidBlockException(s + " is null or invalid data");
		if (s.split(";").length < 2)
			throw new InvalidBlockException("Invalid block data: " + s);

		s = s.substring("TreeBlock{".length(), s.length() - 1);

		BlockData data = Bukkit.createBlockData(s.split(";")[0]);
		String current = s.split(";")[1];
		if (current == null || current.split(",").length != 3)
			throw new InvalidBlockException("Block offset is invalid");
		double x = Double.parseDouble(current.split(",")[0]), y = Double.parseDouble(current.split(",")[1]),
				z = Double.parseDouble(current.split(",")[2]);
		Vector off = new Vector(x, y, z);
		BlockType t = type == null ? BlockType.DEFAULT : type;
		if (s.split(";").length == 3 && t == null) {
			t = BlockType.valueOf(s.split(";")[2]);
		}
		return new TreeBlock(data, off, t);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof TreeBlock))
			return false;
		TreeBlock block = (TreeBlock) obj;
		return block.getBlock().matches(this.getBlock()) && block.getOffset().equals(this.getOffset());
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<>();
		data.put("offset", offset.toString());
		data.put("data", this.data.getAsString(true));
		data.put("type", this.type.toString());
		return data;
	}

}
