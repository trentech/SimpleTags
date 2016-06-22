package com.gmail.trentech.simpletags;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.simpletags.commands.CommandManager;
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.tags.SingleTag;
import com.gmail.trentech.simpletags.tags.Tag;
import com.gmail.trentech.simpletags.tags.WorldTag;
import com.gmail.trentech.simpletags.utils.Resource;
import com.gmail.trentech.simpletags.utils.SQLUtils;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = "SimpleTags", repoOwner = "TrenTech", version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, authors = Resource.AUTHOR, url = Resource.URL, dependencies = { @Dependency(id = "Updatifier", optional = true) })
public class Main {

	private static Game game;
	private static Logger log;
	private static PluginContainer plugin;

	private static List<Class<? extends Tag>> tags = new ArrayList<>();

	@Listener
	public void onPreInitializationEvent(GamePreInitializationEvent event) {
		game = Sponge.getGame();
		plugin = getGame().getPluginManager().getPlugin(Resource.ID).get();
		log = getPlugin().getLogger();
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		registerTag(GroupTag.class);
		registerTag(PlayerTag.class);
		registerTag(WorldTag.class);
		registerTag(SingleTag.class);
	}

	@Listener
	public void onPostInitializationEvent(GamePostInitializationEvent event) {
		getGame().getEventManager().registerListeners(this, new EventListener());
		getGame().getCommandManager().register(this, new CommandManager().getCmd(), "tag", "t");

		for (Class<? extends Tag> clazz : tags) {
			SQLUtils.createTable(clazz.getSimpleName());
		}

		GroupTag.init();
		PlayerTag.init();
		WorldTag.init();
		SingleTag.init();
	}

	public static Game getGame() {
		return game;
	}

	public static Logger getLog() {
		return log;
	}

	public static PluginContainer getPlugin() {
		return plugin;
	}

	public static void registerTag(Class<? extends Tag> clazz) {
		tags.add(clazz);
	}

	public static void registerCommand(CommandSpec spec, String... aliases) {
		CommandManager.hash.put(spec, aliases);
	}
}
