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

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.tags.DefaultTag;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDPlayer implements CommandExecutor {

	public CMDPlayer(){
		Help help = new Help("player", "player", " View and edit player tags");
		help.setSyntax(" /tag player <player> <tag>\n /t p <player> <tag>");
		help.setExample(" /tag player Notch\n /tag player Notch &e[Notch]\n /tag player @p &e[Me]\n /tag player default &b[%PLAYER%]\n /tag player @p reset");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag player <player> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String>getOne("name").get();
		
		if(src instanceof Player && name.equalsIgnoreCase("@p")){
			name = ((Player) src).getName();
		}
		
		Optional<Player> optionalPlayer = Main.getGame().getServer().getPlayer(name);

		if(!optionalPlayer.isPresent()){
			src.sendMessage(Text.of(TextColors.DARK_RED, name, "does not exist!"));
			return CommandResult.empty();
		}
		
		Player player = optionalPlayer.get();

		if(src instanceof Player && !((Player) src).equals(player) && !src.hasPermission("simpletags.cmd.tag.player.others")){
			src.sendMessage(Text.of(TextColors.DARK_RED, "You only have permission to tag yourself!"));
			return CommandResult.empty();
		}

		Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);
		
		if(!args.hasAny("tag")) {
			PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Player")).build());
			
			List<Text> list = new ArrayList<>();

			if(optionalPlayerTag.isPresent()){
				list.add(Text.of(TextColors.AQUA, "Current Tag: ", optionalPlayerTag.get().getTag()));
			}else{
				list.add(Text.of(TextColors.AQUA, "Current Tag: ", DefaultTag.get(player).get()));
			}
			
			list.add(Text.of(TextColors.AQUA, "Update Tag: ", TextColors.GREEN, "/tag player <player> <tag>"));
			
			pages.contents(list);
			pages.sendTo(src);
			
			return CommandResult.empty();
		}

		String tag = args.<String>getOne("tag").get();
		
		if(tag.equalsIgnoreCase("reset")){
			if(optionalPlayerTag.isPresent()){
				optionalPlayerTag.get().delete();
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag Reset!"));
			
			return CommandResult.success();
		}
		
		if(optionalPlayerTag.isPresent()){
			optionalPlayerTag.get().setTag(tag);
		}else{
			new PlayerTag(player, DefaultTag.get(player).get().getTag());
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag Changed!"));
		
		return CommandResult.success();
	}

}
