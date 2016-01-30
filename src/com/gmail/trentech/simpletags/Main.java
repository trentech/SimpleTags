package com.gmail.trentech.simpletags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;

import com.gmail.trentech.simpletags.commands.CommandManager;
import com.gmail.trentech.simpletags.tags.BroadcastTag;
import com.gmail.trentech.simpletags.tags.ConsoleTag;
import com.gmail.trentech.simpletags.tags.DefaultTag;
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.utils.Resource;
import com.gmail.trentech.simpletags.utils.SQLUtils;

@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION)
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
		
		initGroups();
		
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

	public static void initGroups(){
		PermissionService permissionService = Main.getGame().getServiceManager().provide(PermissionService.class).get();
		
		List<String> groups = new ArrayList<>();
		
		for(Subject subject : permissionService.getGroupSubjects().getAllSubjects()){
			String group = subject.getIdentifier();

			if(group.equalsIgnoreCase("op_0") || group.equalsIgnoreCase("op_1") || group.equalsIgnoreCase("op_2") || group.equalsIgnoreCase("op_3") || group.equalsIgnoreCase("op_4")){
				group = "operator";
			}
			
			Optional<GroupTag> optionalGroupTag = GroupTag.get(group);
			
			if(!optionalGroupTag.isPresent()){
				new GroupTag(group, "&a[" + group + "]");
			}
			groups.add(group);
		}

		for(GroupTag groupTag : GroupTag.all()){
			if(!groups.contains(groupTag.getName())){
				groupTag.delete();
			}
		}
	}
}
