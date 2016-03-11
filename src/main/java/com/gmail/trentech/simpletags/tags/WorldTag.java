package com.gmail.trentech.simpletags.tags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.World;

import com.gmail.trentech.simpletags.Main;
import com.gmail.trentech.simpletags.utils.SQLUtils;

public class WorldTag extends SQLUtils {

	private final String name;
	private Text tag;
	
	private WorldTag(String world, String tag){
		this.name = world;
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);
	}
	
	public WorldTag(World world, String tag){
		this.name = world.getName();
		this.tag = TextSerializers.FORMATTING_CODE.deserialize(tag);
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
	
	public static Optional<WorldTag> get(World world){
		Optional<WorldTag> optionalWorldTag = Optional.empty();
		
		String name = world.getName();
		
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Worlds");
		    
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				if(result.getString("Name").equalsIgnoreCase(name)){
					optionalWorldTag = Optional.of(new WorldTag(name, result.getString("Tag")));		
					break;
				}			
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return optionalWorldTag;
	}
	
	private void save(){
		try {
		    Connection connection = getDataSource().getConnection();
		    
		    PreparedStatement statement = connection.prepareStatement("INSERT into Worlds (Name, Tag) VALUES (?, ?)");	
			
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
		    
		    PreparedStatement statement = connection.prepareStatement("UPDATE Worlds SET Tag = ? WHERE Name = ?");	
			
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
		    
		    PreparedStatement statement = connection.prepareStatement("DELETE from Worlds WHERE Name = ?");
		    
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
