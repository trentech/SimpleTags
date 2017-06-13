package com.gmail.trentech.simpletags.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.commands.CMDDefault;
import com.gmail.trentech.simpletags.commands.CMDGroup;
import com.gmail.trentech.simpletags.commands.CMDPlayer;
import com.gmail.trentech.simpletags.commands.CMDServer;
import com.gmail.trentech.simpletags.commands.CMDTag;
import com.gmail.trentech.simpletags.commands.CMDWorld;
import com.gmail.trentech.simpletags.commands.elements.GroupElement;

public class Commands {

	public static Map<CommandSpec,String[]> hash = new HashMap<>();
	private CommandElement element = GenericArguments.flags().flag("help").setAcceptsArbitraryLongFlags(true).buildWith(GenericArguments.none());
	
	private CommandSpec cmdTagDefault = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.player.default")
		    .arguments(element, GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDDefault())
		    .build();
	
	private CommandSpec cmdTagPlayer = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.player")
		    .child(cmdTagDefault, "default", "d")
		    .arguments(element, GenericArguments.player(Text.of("player")), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDPlayer())
		    .build();
	
	private CommandSpec cmdTagGroup = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.group")
		    .arguments(element, new GroupElement(Text.of("group")), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDGroup())
		    .build();
	
	private CommandSpec cmdTagWorld = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.world")
		    .arguments(element, GenericArguments.world(Text.of("world")), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDWorld())
		    .build();

	private CommandSpec cmdTagServer = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.server")
		    .arguments(element, GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDServer())
		    .build();

	public CommandSpec getCmd(){
		Main.registerCommand(cmdTagGroup, "group", "g");
		Main.registerCommand(cmdTagWorld, "world", "w");
		Main.registerCommand(cmdTagPlayer, "player", "p");
		Main.registerCommand(cmdTagServer, "server", "s");

		Builder builder = CommandSpec.builder().permission("simpletags.cmd.tag").executor(new CMDTag());
		
		for(Entry<CommandSpec, String[]> entry : hash.entrySet()) {
			builder.child(entry.getKey(), entry.getValue());
		}

		return builder.build();
	}
}
