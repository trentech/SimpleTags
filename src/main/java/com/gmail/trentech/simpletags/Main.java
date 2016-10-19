package com.gmail.trentech.simpletags;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.helpme.Help;
import com.gmail.trentech.simpletags.commands.CommandManager;
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.tags.SingleTag;
import com.gmail.trentech.simpletags.tags.Tag;
import com.gmail.trentech.simpletags.tags.WorldTag;
import com.gmail.trentech.simpletags.utils.Resource;
import com.gmail.trentech.simpletags.utils.SQLUtils;
import com.google.inject.Inject;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = Resource.NAME, repoOwner = Resource.AUTHOR, version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, dependencies = { @Dependency(id = "Updatifier", optional = true), @Dependency(id = "helpme", optional = true) })
public class Main {

	@Inject @ConfigDir(sharedRoot = false)
    private Path path;

	@Inject
	private Logger log;
	private List<Class<? extends Tag>> tags = new ArrayList<>();
	
	private static PluginContainer plugin;
	private static Main instance;
	
	@Listener
	public void onPreInitializationEvent(GamePreInitializationEvent event) {
		plugin = Sponge.getPluginManager().getPlugin(Resource.ID).get();
		instance = this;
		
		try {			
			Files.createDirectories(path);		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		registerTag(GroupTag.class);
		registerTag(PlayerTag.class);
		registerTag(WorldTag.class);
		registerTag(SingleTag.class);
		
		if(Sponge.getPluginManager().getPlugin("helpme").isPresent()) {
			Help tagGroup = new Help("tag group", "group", "View and edit group tags")
					.setPermission("simpletags.cmd.tag.group")
					.addUsage("/tag group <group> <tag>")
					.addUsage("/t g <group> <tag>")
					.addExample("/tag group admin")
					.addExample("/tag group admin &e[BOSS]")
					.addExample("/tag group admin reset");				
			
			Help tagPlayer = new Help("tag player", "player", "View and edit player tags")
					.setPermission("simpletags.cmd.tag.player")
					.addUsage("/tag player <player> <tag>")
					.addUsage("/t p <player> <tag>")
					.addExample("/tag player Notch")
					.addExample("/tag player Notch &e[Notch]")
					.addExample("/tag player default &b[%PLAYER%]");
			
			Help tagServer = new Help("tag server", "server", "View and edit server tag")
					.setPermission("simpletags.cmd.tag.server")
					.addUsage("/tag server <tag>\n /t s <tag>")
					.addUsage("/t s <tag>")
					.addExample("/tag server")
					.addExample("/tag server &7[CONSOLE]");
			
			Help tagWorld = new Help("tag world", "world", "View and edit world tags")
					.setPermission("simpletags.cmd.tag.world")
					.addUsage("/tag world <world> <tag>")
					.addUsage("/t w <world> <tag>")
					.addExample("/tag world DIM-1")
					.addExample("/tag world DIM-1 &4[NETHER]")
					.addExample("/tag world world reset");
			
			Help tag = new Help("tag", "tag", "Base SimpleTags command")
					.setPermission("simpletags.cmd.tag")
					.addChild(tagWorld)
					.addChild(tagServer)
					.addChild(tagPlayer)
					.addChild(tagGroup);
			
			Help.register(tag);			
		}
	}

	@Listener
	public void onPostInitializationEvent(GamePostInitializationEvent event) {
		Sponge.getEventManager().registerListeners(this, new EventListener());
		Sponge.getCommandManager().register(this, new CommandManager().getCmd(), "tag", "t");

		for (Class<? extends Tag> clazz : tags) {
			SQLUtils.createTable(clazz.getSimpleName());
		}

		GroupTag.init();
		PlayerTag.init();
		WorldTag.init();
		SingleTag.init();
	}

	public Logger getLog() {
		return log;
	}

	public static PluginContainer getPlugin() {
		return plugin;
	}

	public static Main instance() {
		return instance;
	}
	
	public static void registerTag(Class<? extends Tag> clazz) {
		instance().tags.add(clazz);
	}

	public static void registerCommand(CommandSpec spec, String... aliases) {
		CommandManager.hash.put(spec, aliases);
	}
}
