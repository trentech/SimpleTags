package com.gmail.trentech.simpletags;

import java.util.Optional;

import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.World;

import com.gmail.trentech.simpletags.tags.ConsoleTag;
import com.gmail.trentech.simpletags.tags.DefaultTag;
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.tags.WorldTag;

public class EventListener {

	@Listener
	public void onMessageChannelEventChat(MessageChannelEvent.Chat event, @First Player player){
		Optional<Text> optionalMessage = event.getOriginalMessage();
		
		if(!optionalMessage.isPresent()){
			return;
		}
		Text message = TextSerializers.FORMATTING_CODE.deserialize(": " + optionalMessage.get().toPlain().substring(optionalMessage.get().toPlain().indexOf(" ") + 1));

		Text worldTag = Text.EMPTY;
		
		Builder groupTagBuilder = Text.builder();

		Builder playerTag = Text.builder().onHover(TextActions.showText(Text.of(player.getName())));

		Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);
		
		if(!optionalPlayerTag.isPresent()){
			playerTag.append(DefaultTag.get(player).get().getTag());
		}else{
			playerTag.append(PlayerTag.get(player).get().getTag());
		}

		worldTag = WorldTag.get(player.getWorld()).get().getTag();

		for(GroupTag groupTag : GroupTag.all()){
			String group = groupTag.getName();
			
			if(!player.hasPermission("simpletags.group." + group)){
				continue;
			}
			
			groupTagBuilder.append(groupTag.getTag());
		}

    	event.setMessage(Text.of(worldTag, groupTagBuilder.build(), playerTag.build(), message));
	}
	
	@Listener
	public void onSendCommandEvent(SendCommandEvent event, @First ConsoleSource sender){
		String command = event.getCommand();
		if(!command.equalsIgnoreCase("say")){
			return;
		}
		
		Optional<PluginContainer> plugin = Main.getGame().getPluginManager().getPlugin("SimpleChat");
		
		if(plugin.isPresent()){
			return;
		}
		
		Text message = TextSerializers.FORMATTING_CODE.deserialize(": " + event.getArguments());
		Text consoleTag = ConsoleTag.get().get().getTag();
		
		Main.getGame().getServer().getBroadcastChannel().send(Text.of(consoleTag, message));
		
		event.setCancelled(true);
	}
	
	@Listener
	public void onLoadWorldEvent(LoadWorldEvent event){
		World world = event.getTargetWorld();
		
		Optional<WorldTag> optionalWorldTag = WorldTag.get(world);
		
		if(!optionalWorldTag.isPresent()){
			new WorldTag(world, "&b[" + world.getName() + "]");
		}
	}
}
