package com.gmail.trentech.simpletags;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.simpletags.commands.CommandManager;
import com.gmail.trentech.simpletags.tags.BroadcastTag;
import com.gmail.trentech.simpletags.tags.ConsoleTag;
import com.gmail.trentech.simpletags.tags.DefaultTag;
import com.gmail.trentech.simpletags.utils.Resource;
import com.gmail.trentech.simpletags.utils.SQLUtils;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = Resource.ID, repoOwner = "TrenTech", version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, dependencies = "after: Updatifier")
public class Main {

	private static Game game;
	private static Logger log;
	private static PluginContainer plugin;

	@Listener
    public void onPreInitializationEvent(GamePreInitializationEvent event) {
		game = Sponge.getGame();
		plugin = getGame().getPluginManager().getPlugin(Resource.ID).get();
		log = getGame().getPluginManager().getLogger(plugin);
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		getGame().getEventManager().registerListeners(this, new EventListener());
		
		
		SQLUtils.createTables();
	}
	
	@Listener
	public void onStartedServerEvent(GameStartedServerEvent event) {
		getGame().getCommandManager().register(this, new CommandManager().getCmd(), "tag", "t");

		if(!ConsoleTag.get().isPresent()){
			new ConsoleTag();
		}
		if(!DefaultTag.get().isPresent()){
			new DefaultTag();
		}
		if(!BroadcastTag.get().isPresent()){
			new BroadcastTag();
		}
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

}
