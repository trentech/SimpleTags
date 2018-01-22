package com.gmail.trentech.simpletags.commands.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class GroupElement extends CommandElement {

    public GroupElement(Text key) {
        super(key);
    }

    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        final String next = args.next();

        PermissionService permissionService = Sponge.getServiceManager().provide(PermissionService.class).get();
        
		for (Subject subject : permissionService.getGroupSubjects().getLoadedSubjects()) {
			String group = subject.getIdentifier();

			if (group.equalsIgnoreCase("op_0") || group.equalsIgnoreCase("op_1") || group.equalsIgnoreCase("op_2") || group.equalsIgnoreCase("op_3") || group.equalsIgnoreCase("op_4")) {
				group = "op";
			}

			if (next.equalsIgnoreCase(group)) {
				return next;
			}
		}
		
		throw args.createError(Text.of(TextColors.RED, "Group not found"));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
    	List<String> list = new ArrayList<>();
    	
    	PermissionService permissionService = Sponge.getServiceManager().provide(PermissionService.class).get();
        
    	Optional<String> next = args.nextIfPresent();
    	
    	if(next.isPresent()) {
    		for (Subject subject : permissionService.getGroupSubjects().getLoadedSubjects()) {
    			String group = subject.getIdentifier();

    			if (group.equalsIgnoreCase("op_0") || group.equalsIgnoreCase("op_1") || group.equalsIgnoreCase("op_2") || group.equalsIgnoreCase("op_3") || group.equalsIgnoreCase("op_4")) {
    				group = "op";
    			}
    			
            	if(group.startsWith(next.get())) {
            		list.add(group);
            	}
    		}
    	} else {
    		for (Subject subject : permissionService.getGroupSubjects().getLoadedSubjects()) {
    			String group = subject.getIdentifier();

    			if (group.equalsIgnoreCase("op_0") || group.equalsIgnoreCase("op_1") || group.equalsIgnoreCase("op_2") || group.equalsIgnoreCase("op_3") || group.equalsIgnoreCase("op_4")) {
    				group = "op";
    			}

            	list.add(group);
    		}
    	}
		
        return list;
    }

    @Override
    public Text getUsage(CommandSource src) {
        return Text.of(getKey());
    }
}
