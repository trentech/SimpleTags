package com.gmail.trentech.simpletags.commands;

import java.util.Optional;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.simpletags.Main;

public class CommandManager {

	private CommandSpec cmdTagDefault = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.player")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDDefault())
		    .build();
	
	private CommandSpec cmdTagPlayer = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.player")
		    .child(cmdTagDefault, "default", "d")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDPlayer())
		    .build();
	
	private CommandSpec cmdTagGroup = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.group")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDGroup())
		    .build();
	
	private CommandSpec cmdTagWorld = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.world")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDWorld())
		    .build();

	private CommandSpec cmdTagServer = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.server")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDServer())
		    .build();
	
	private CommandSpec cmdTagChannel = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.channel")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDChannel())
		    .build();

	private CommandSpec cmdTagBroadcast = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.server")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDBroadcast())
		    .build();

	private CommandSpec cmdTagReload = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.reload")
		    .executor(new CMDReload())
		    .build();
	
	public CommandSpec getCmd(){
		Builder tagBuilder = CommandSpec.builder().permission("simpletags.cmd.tag").child(cmdTagGroup, "group", "g").child(cmdTagWorld, "world", "w")
			    .child(cmdTagPlayer, "player", "p").child(cmdTagServer, "server", "s").child(cmdTagReload, "reload", "r").executor(new CMDTag());
		
		Optional<PluginContainer> plugin = Main.getGame().getPluginManager().getPlugin("SimpleChat");
		
		if(plugin.isPresent()){
			tagBuilder.child(cmdTagBroadcast, "broadcast", "b").child(cmdTagChannel, "channel", "c");
		}
		
		return tagBuilder.build();
	}
}
