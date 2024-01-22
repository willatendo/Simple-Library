package willatendo.simplelibrary.server.block;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import willatendo.simplelibrary.server.util.SimpleUtils;

public class PlantType {
	private static final Map<String, PlantType> VALUES = new ConcurrentHashMap<>();

	public static final PlantType PLAINS = create("plains");
	public static final PlantType DESERT = create("desert");
	public static final PlantType BEACH = create("beach");
	public static final PlantType CAVE = create("cave");
	public static final PlantType WATER = create("water");
	public static final PlantType NETHER = create("nether");
	public static final PlantType CROP = create("crop");

	public static PlantType create(String id) {
		return VALUES.computeIfAbsent(id, name -> {
			if (SimpleUtils.INVALID_CHARACTERS.matcher(name).find()) {
				throw new IllegalArgumentException("PlantType.get() called with invalid name: " + id);
			}
			return new PlantType(name);
		});
	}

	private final String name;

	private PlantType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
