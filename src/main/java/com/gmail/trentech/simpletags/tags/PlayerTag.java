package com.gmail.trentech.simpletags.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class PlayerTag extends Tag {

	public static Map<String, PlayerTag> cache = new ConcurrentHashMap<>();

	PlayerTag(String playerUUID, String tag) {
		super(playerUUID, PlayerTag.class, tag);
	}

	PlayerTag(Tag tag) {
		super(tag);
	}

	public static Optional<PlayerTag> get(Player player) {
		String uuid = player.getUniqueId().toString();

		if (cache.containsKey(uuid)) {
			return Optional.of(cache.get(uuid));
		}

		return Optional.empty();
	}

	public static Optional<PlayerTag> create(String uuid, String tag) {
		if (cache.containsKey(uuid)) {
			return Optional.empty();
		}

		return Optional.of(new PlayerTag(uuid, tag));
	}

	public static Optional<PlayerTag> create(Player player, String tag) {
		String uuid = player.getUniqueId().toString();

		if (cache.containsKey(uuid)) {
			return Optional.empty();
		}

		return Optional.of(new PlayerTag(uuid, tag));
	}

	public static List<PlayerTag> getAll() {
		List<PlayerTag> list = new ArrayList<>();

		for (Entry<String, PlayerTag> entry : cache.entrySet()) {
			list.add(entry.getValue());
		}

		return list;
	}

	public static Text getDefault(Player player) {
		Tag tag = getDefault().get();
		String text = TextSerializers.FORMATTING_CODE.serialize(tag.getTag()).replace("%PLAYER%", player.getName());
		return TextSerializers.FORMATTING_CODE.deserialize(text);
	}

	public static Optional<PlayerTag> getDefault() {
		if (cache.containsKey("simpletags.DEFAULT")) {
			return Optional.of(cache.get("simpletags.DEFAULT"));
		}

		return Optional.empty();
	}

	private static void createDefault() {
		String name = "simpletags.DEFAULT";
		String tag = "&6[%PLAYER%]";

		if (cache.containsKey(name)) {
			return;
		}

		create(name, tag);
	}

	public static void init() {
		ConcurrentHashMap<String, PlayerTag> hash = new ConcurrentHashMap<>();

		for (Tag tag : getAll(PlayerTag.class)) {
			hash.put(tag.getName(), new PlayerTag(tag));
		}

		cache = hash;

		createDefault();
	}
}
