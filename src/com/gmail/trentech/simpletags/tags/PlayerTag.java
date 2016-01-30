package com.gmail.trentech.simpletags.tags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.utils.SQLUtils;

public class PlayerTag extends SQLUtils {

	private final String name;
	private Text tag;

	private PlayerTag(String player, String tag){
		this.name = player;
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);
	}
	
	public PlayerTag(Player player, Text tag){
		this.name = player.getUniqueId().toString();;
		this.tag = tag;
		save();
	}
	
	public String getName(){
		return name;
	}
	
	public Text getTag(){
		return tag;
	}
	
	public void setTag(String tag){
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);
		update();
	}
	
	public void setTag(Text tag){
		this.tag = tag;
		update();
	}
	
	public static Optional<PlayerTag> get(Player player){
		
		Optional<PlayerTag> optionalPlayerTag = Optional.empty();
		
		String name = player.getUniqueId().toString();

		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Players");
		    
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				if(result.getString("Name").equalsIgnoreCase(name)){
					optionalPlayerTag = Optional.of(new PlayerTag(name, result.getString("Tag").replace("%PLAYER%", player.getName())));		
					break;
				}			
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return optionalPlayerTag;
	}
	
	private void save(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("INSERT into Players (Name, Tag) VALUES (?, ?)");	
			
		    statement.setString(1, this.name);
		    statement.setString(2, TextSerializers.FORMATTING_CODE.serialize(this.tag));

			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void update(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("UPDATE Players SET Tag = ? WHERE Name = ?");	
			
		    statement.setString(1, TextSerializers.FORMATTING_CODE.serialize(this.tag));
		    statement.setString(2, this.name);

			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("DELETE from Players WHERE Name = ?");
		    
			statement.setString(1, this.name);
			statement.executeUpdate();
			
			connection.close();
			
			for(Task task : Main.getGame().getScheduler().getScheduledTasks()){
				if(task.getName().contains(this.name)){
					task.cancel();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
