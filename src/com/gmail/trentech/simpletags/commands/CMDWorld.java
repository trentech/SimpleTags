package com.gmail.trentech.simpletags.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.tags.WorldTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDWorld implements CommandExecutor {

	public CMDWorld(){
		Help help = new Help("world", "world", " View and edit world tags");
		help.setSyntax(" /tag world <world> <tag>\n /t w <world> <tag>");
		help.setExample(" /tag world DIM-1\n /tag world DIM-1 &4[NETHER]\n /tag world @w &6[MyWorld]");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag world <world> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String>getOne("name").get();
		
		if(src instanceof Player && name.equalsIgnoreCase("@w")){
			name = ((Player) src).getWorld().getName();
		}
		
		Optional<World> optionalWorld = Main.getGame().getServer().getWorld(name);
		
    	if(!optionalWorld.isPresent()){
			src.sendMessage(Text.of(TextColors.DARK_RED, "World does not exist!"));
			return CommandResult.empty();
    	}
    	World world = optionalWorld.get();

    	WorldTag worldTag = WorldTag.get(world).get();
    	
		if(!args.hasAny("tag")) {
			PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Server")).build());
			
			List<Text> list = new ArrayList<>();

			list.add(Text.of(TextColors.AQUA, "Current Tag: ", worldTag.getTag()));
			list.add(Text.of(TextColors.AQUA, "Update Tag: ", TextColors.GREEN, "/tag world <world> <tag>"));
			
			pages.contents(list);
			pages.sendTo(src);
			
			return CommandResult.empty();
		}
		String tag = args.<String>getOne("tag").get();
		
		worldTag.setTag(tag);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag Changed!"));
		
		return CommandResult.success();
	}

}
