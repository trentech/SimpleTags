package com.gmail.trentech.simpletags.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GroupTag extends Tag {

	public static ConcurrentHashMap<String, GroupTag> cache = new ConcurrentHashMap<>();

	GroupTag(String group, String tag){
		super(group, GroupTag.class, tag);	
	}

	GroupTag(Tag tag) {
		super(tag);
	}

	public static Optional<GroupTag> get(String group){
		if(cache.containsKey(group)) {
			return Optional.of(cache.get(group));
		}
		
		return Optional.empty();
	}
	
	public static Optional<GroupTag> create(String group, String tag) {
		if(cache.containsKey(group)) {
			return Optional.empty();
		}

		return Optional.of(new GroupTag(group, tag));
	}

	public static List<GroupTag> getAll() {
		List<GroupTag> list = new ArrayList<>();
		
		for(Entry<String, GroupTag> entry : cache.entrySet()) {
			list.add(entry.getValue());
		}
		
		return list;
	}
	
	public static void init() {
		ConcurrentHashMap<String, GroupTag> hash = new ConcurrentHashMap<>();
		
		for(Tag tag : getAll(GroupTag.class)) {
			hash.put(tag.getName(), new GroupTag(tag));
		}

		cache = hash;
	}
}
