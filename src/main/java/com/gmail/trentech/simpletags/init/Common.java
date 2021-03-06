package com.gmail.trentech.simpletags.init;

import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.help.Argument;
import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.pjc.help.Usage;
import com.gmail.trentech.simpletags.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Common {
	public static void init() {
		initConfig(Main.getPlugin().getId());
		initHelp();
		initData();
	}
	
	public static void initData() {

	}
	
	public static void initHelp() {
		Usage usageGroup = new Usage(Argument.of("<group>", "Specifies the name of a group"))
				.addArgument(Argument.of("<tag>", "Set custom tag. Accepts color codes"));
		
		Help tagGroup = new Help("tag group", "group", "View and edit group tags")
				.setPermission("simpletags.cmd.tag.group")
				.setUsage(usageGroup)
				.addExample("/tag group admin")
				.addExample("/tag group admin &e[BOSS]")
				.addExample("/tag group admin reset");				
		
		Usage usagePlayer = new Usage(Argument.of("<player>", "Specifies the name of a player"))
				.addArgument(Argument.of("<tag>", "Set custom tag. Accepts color codes"));
		
		Help tagPlayer = new Help("tag player", "player", "View and edit player tags")
				.setPermission("simpletags.cmd.tag.player")
				.setUsage(usagePlayer)
				.addExample("/tag player Notch")
				.addExample("/tag player Notch &e[Notch]")
				.addExample("/tag player default &b[%PLAYER%]");
		
		Usage usageServer = new Usage(Argument.of("<tag>", "Set custom tag. Accepts color codes"));
		
		Help tagServer = new Help("tag server", "server", "View and edit server tag")
				.setPermission("simpletags.cmd.tag.server")
				.setUsage(usageServer)
				.addExample("/tag server")
				.addExample("/tag server &7[CONSOLE]");
		
		Usage usageWorld = new Usage(Argument.of("<world>", "Specifies the name of a world"))
				.addArgument(Argument.of("<tag>", "Set custom tag. Accepts color codes"));
		
		Help tagWorld = new Help("tag world", "world", "View and edit world tags")
				.setPermission("simpletags.cmd.tag.world")
				.setUsage(usageWorld)
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
	
	public static void initConfig(String configName) {
		ConfigManager configManager = ConfigManager.init(Main.getPlugin(), configName);
		CommentedConfigurationNode config = configManager.getConfig();

		if (config.getNode("settings", "sql", "database").isVirtual()) {
			config.getNode("settings", "sql", "database").setValue(Main.getPlugin().getId());
		}
		configManager.save();
	}
}
