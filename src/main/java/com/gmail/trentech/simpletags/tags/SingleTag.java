package com.gmail.trentech.simpletags.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SingleTag extends Tag {

	public static ConcurrentHashMap<String, SingleTag> cache = new ConcurrentHashMap<>();

	SingleTag(String id, String name, String tag){
		super(id + "_" + name, SingleTag.class, tag);	
	}

	SingleTag(Tag tag) {
		super(tag);
	}

	public static Optional<SingleTag> get(String id, String name){
		name = id + "_" + name;
		
		if(cache.containsKey(name)) {
			return Optional.of(cache.get(name));
		}
		
		return Optional.empty();
	}
	
	public static Optional<SingleTag> create(String id, String name, String tag) {
		String newName = id + "_" + name;
		
		if(cache.containsKey(newName)) {
			return Optional.empty();
		}

		return Optional.of(new SingleTag(id, name, tag));
	}

	public static List<SingleTag> getAll() {
		List<SingleTag> list = new ArrayList<>();
		
		for(Entry<String, SingleTag> entry : cache.entrySet()) {
			list.add(entry.getValue());
		}
		
		return list;
	}
	
	public static void init() {
		ConcurrentHashMap<String, SingleTag> hash = new ConcurrentHashMap<>();
		
		for(Tag tag : getAll(SingleTag.class)) {
			hash.put(tag.getName(), new SingleTag(tag));
		}

		cache = hash;
	}
}
