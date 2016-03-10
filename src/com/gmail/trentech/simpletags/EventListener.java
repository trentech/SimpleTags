package com.gmail.trentech.simpletags;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.tags.ConsoleTag;
import com.gmail.trentech.simpletags.tags.DefaultTag;
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.tags.WorldTag;

public class EventListener {

	@Listener
	public void onMessageChannelEventChat(MessageChannelEvent.Chat event, @First Player player){
		String messageOrig = event.getOriginalMessage().toPlain();

		Text message = TextSerializers.FORMATTING_CODE.deserialize(": " + messageOrig.substring(messageOrig.indexOf(" ") + 1));

		Text worldTag = Text.EMPTY;
		
		Builder groupTagBuilder = Text.builder();

		Builder playerTag = Text.builder().onHover(TextActions.showText(Text.of(player.getName())));

		Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);
		
		if(!optionalPlayerTag.isPresent()){
			playerTag.append(DefaultTag.get(player).get().getTag());
		}else{
			playerTag.append(PlayerTag.get(player).get().getTag());
		}

		Optional<WorldTag> optionalWorldTag = WorldTag.get(player.getWorld());
		
		if(optionalWorldTag.isPresent()){
			worldTag = WorldTag.get(player.getWorld()).get().getTag();
		}

		for(Entry<Set<Context>, List<Subject>> parent : player.getSubjectData().getAllParents().entrySet()){
			for(Subject subject : parent.getValue()){
				String group = subject.getIdentifier();
				
				if(group.equalsIgnoreCase("op_0") || group.equalsIgnoreCase("op_1") || group.equalsIgnoreCase("op_2") || group.equalsIgnoreCase("op_3") || group.equalsIgnoreCase("op_4")){
					 group = "op";
				}
				
				Optional<GroupTag> optionalGroupTag = GroupTag.get(group);
				
				if(optionalGroupTag.isPresent()){
					groupTagBuilder.append(optionalGroupTag.get().getTag());
				}			
			}
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

}
