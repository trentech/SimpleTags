package com.gmail.trentech.simpletags.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.world.World;

public class WorldTag extends Tag {

	public static ConcurrentHashMap<String, WorldTag> cache = new ConcurrentHashMap<>();

	WorldTag(String world, String tag){
		super(world, WorldTag.class, tag);
	}

	WorldTag(Tag tag){
		super(tag);
	}
	
	public static Optional<WorldTag> get(World world){
		String name = world.getName();

		if(cache.containsKey(name)) {
			return Optional.of(cache.get(name));
		}
		
		return Optional.empty();
	}
	
	public static Optional<WorldTag> create(World world, String tag) {
		String name = world.getName();
		
		if(cache.containsKey(name)) {
			return Optional.empty();
		}

		return Optional.of(new WorldTag(name, tag));
	}

	public static List<WorldTag> getAll() {
		List<WorldTag> list = new ArrayList<>();
		
		for(Entry<String, WorldTag> entry : cache.entrySet()) {
			list.add(entry.getValue());
		}
		
		return list;
	}
	
	public static void init() {
		ConcurrentHashMap<String, WorldTag> hash = new ConcurrentHashMap<>();
		
		for(Tag tag : getAll(WorldTag.class)) {
			hash.put(tag.getName(), new WorldTag(tag));
		}

		cache = hash;
	}
}
