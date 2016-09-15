package com.gmail.trentech.simpletags.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Help {

	private final String id;
	private final String command;
	private final String description;
	private Optional<String> syntax = Optional.empty();
	private Optional<String> example = Optional.empty();

	private static ConcurrentHashMap<String,Help> list = new ConcurrentHashMap<>();

	public Help(String id, String command, String description) {
		this.id = id;
		this.command = command;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public Optional<String> getSyntax() {
		return syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = Optional.of(syntax);
	}

	public Optional<String> getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = Optional.of(example);
	}

	public String getCommand() {
		return command;
	}

	public void save() {
		list.put(getCommand(), this);
	}

	public void execute(CommandSource src) {
		List<Text> list = new ArrayList<>();

		list.add(Text.of(TextColors.GREEN, "Description:"));
		list.add(Text.of(TextColors.WHITE, getDescription()));

		if (getSyntax().isPresent()) {
			list.add(Text.of(TextColors.GREEN, "Syntax:"));
			list.add(Text.of(TextColors.WHITE, getSyntax().get()));
		}
		if (getExample().isPresent()) {
			list.add(Text.of(TextColors.GREEN, "Example:"));
			list.add(Text.of(TextColors.WHITE, getExample().get(), TextColors.DARK_GREEN));
		}

		if (src instanceof Player) {
			PaginationList.Builder pages = Sponge.getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, getCommand().toLowerCase())).build());

			pages.contents(list);

			pages.sendTo(src);
		} else {
			for (Text text : list) {
				src.sendMessage(text);
			}
		}
	}
	
	public static Optional<Help> get(String command) {
		if(list.containsKey(command)) {
			return Optional.of(list.get(command));
		}
		
		return Optional.empty();
	}
	
	public static Consumer<CommandSource> getHelp(String command) {
		return (CommandSource src) -> {
			if(list.containsKey(command)) {
				Help help = list.get(command);
				help.execute(src);
			}
		};
	}
	
	public static Map<String, Help> all() {
		return list;
	}
}
