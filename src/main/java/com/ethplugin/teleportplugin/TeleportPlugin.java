package com.ethplugin.teleportplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportPlugin extends JavaPlugin {
	List<TpPoint> tpPoints = new ArrayList<TpPoint>();
	
    @Override
    public void onEnable(){
        // When plugin starts
        //   - At server startup
        //   - After /reload
        getLogger().info("[TeleportPlugin] Starting!");
        // Load tp points list from config file
        this.tpPoints = TpPoint.loadTpPointsList(this);
        getLogger().info("[TeleportPlugin] Location list loaded from config: " + this.tpPoints.size() + " points.");
    }

    @Override
    public void onDisable(){
        // When plugin is disabled
        //   - At server shutdown
        //   - After /reload
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    	// ********************************************************
    	// "setposition" command (save the current player position)
    	// ********************************************************
    	
    	if (cmd.getName().equalsIgnoreCase("settpposition")) {
    		getLogger().info("[TeleportPlugin] settpposition command entered");
    	    if(sender instanceof Player) {
	    		// Command issued by player
    	    	// We get the player object
    	        Player p = (Player)sender;
    	    	if (args.length > 0) {
    	    		// Get player location
        	        Location newPosition = p.getLocation();
        	        // Get point name from entered args
        	        String name = args[0];
        	        // Check if point name already exists for this player
    	        	TpPoint tpPoint = TpPoint.findTpPointInList(tpPoints, name, p);
    	        	if (tpPoint != null) {
    	        		// Error, point name already exists for this player
        	    		p.sendMessage("You already have a saved point with this name.");
            	        getLogger().info("[TeleportPlugin] The player has already got a point with this name."); 
    	        	}
    	        	else {
    	        		// Create new point
            	        TpPoint newTpPoint = new TpPoint(args[0], newPosition, p);
            	        // Add point to list
            	        tpPoints.add(newTpPoint);
            	        // Save tpPoints list in the config file
            	        TpPoint.saveTpPointList(tpPoints, this);
            	        
            	        getLogger().info("[TeleportPlugin] Position [" + args[0] + "] saved!"); 
            	        p.sendMessage("Position [" + args[0] + "] saved!");     	        		
    	        	}
    	    	}
    	    	else {
    	    		p.sendMessage("You must specify point name. Example /settpposition home");
        	        getLogger().info("[TeleportPlugin] The player has not specified the point name."); 
    	    	}
    	           	        
    	    } else {
    	        // Command issued from server
        		getLogger().info("[TeleportPlugin] Command issued from server.");    	        
    	    }
    	    // Valid command, returns true
    		return true;
    	}

    	// ******************************************************
    	// "gotoposition" command (teleport player to a position)
    	// ******************************************************

    	else if (cmd.getName().equalsIgnoreCase("gototpposition")) {
    		getLogger().info("Commande gototpposition recue.");
    	    if(sender instanceof Player) {
    	        // Command issued by player, get the player
    	        Player p = (Player)sender;
    	        // Check that we have arguments in the command
    	        if (args.length > 0) {
    	        	String name = args[0];
    	        	// Find point in list
    	        	TpPoint tpPoint = TpPoint.findTpPointInList(tpPoints, name, p);
    	        	if (tpPoint == null) {
        	    		p.sendMessage("There is no saved point with this name.");
            	        getLogger().info("[TeleportPlugin] Player tries to teleport to an unknown point."); 
    	        	}
    	        	else {
    	        		p.teleport(tpPoint.getLocation());
            	        getLogger().info("Player has been teleported!");    	            	        	
        	    		p.sendMessage("You have been teleported!");
    	        	}
    	        }
    	        else {
    	    		p.sendMessage("You must specify point name. Example /gototpposition home");
        	        getLogger().info("[TeleportPlugin] The player has not specified point name."); 
    	    	}
    	    } else {
    	        // Command issued from server
        		getLogger().info("[TeleportPlugin] Command issued from server.");    	        
    	    }
    	    // Valid command, returns true
    		return true;
    	}

    	// *********************************************************
    	// "removetpposition" command (removes a position from list)
    	// *********************************************************
    	
    	else if (cmd.getName().equalsIgnoreCase("removetpposition")) {
    		getLogger().info("[TeleportPlugin] Command removetpposition entered.");
    	    if(sender instanceof Player) {
	    		// Command issued by player, get player
    	        Player p = (Player)sender;
    	    	// Check if there are arguments with command
    	        if (args.length > 0) {
    	        	// Get name in command arguments
        	        String name = args[0];
        	        // Find tp point
    	        	TpPoint tpPoint = TpPoint.findTpPointInList(tpPoints, name, p);
    	        	if (tpPoint == null) {
    	        		// Error, point does not exist
    	        		p.sendMessage("This point does not exist.");
            	        getLogger().info("[TeleportPlugin] Player tries to remove a point that does not exist."); 
    	        	}
    	        	else {
    	        		// Remove the point from list
            	        tpPoints.remove(tpPoint);
            	        // Save tp points list in config file
            	        TpPoint.saveTpPointList(tpPoints, this);
      
            	        getLogger().info("[TeleportPlugin] Point [" + args[0] + "] has been removed."); 
            	        p.sendMessage("Point [" + args[0] + "] has been removed.");     	        		
    	        	}
    	    	}
    	    	else {
    	    		p.sendMessage("You must specify point name. Example /removetppoint home");
        	        getLogger().info("[TeleportPlugin] The player has not specified the point name to remove."); 
    	    	}
    	           	        
    	    } else {
    	        // Command issued from server
        		getLogger().info("[TeleportPlugin] Command issued from server.");    	        
    	    }
    	    // Valid command, returns true
    		return true;

    	}
    	
    	// ********************************************************
    	// "listtpposition" command
    	//   - from game: shows list of player saved position
    	//   - from server: shows all saved positions
    	// ********************************************************
    	
    	else if (cmd.getName().equalsIgnoreCase("listtpposition")) {
    		getLogger().info("[TeleportPlugin] listtpposition command entered.");
    	    if(sender instanceof Player) {
	    		// Command issued by player
    	    	// Get the player
    	        Player p = (Player)sender;
    	        String result = "";
    	        for (TpPoint item : tpPoints) {
    	        	if (item.getPlayerName().equals(p.getName())) {
    	        		result += item.getName();
    	        		result += " ";
    	        	}
    	        }
    	        p.sendMessage("Saved points: " + result);
    	    } else {
    	    	// Command issued from server
    	    	String result = "";
    	        for (TpPoint item : tpPoints) {
	        		result += item.getPlayerName();
	        		result += ":";
    	        	result += item.getName();
	        		result += " ";
    	        }
    	        getLogger().info("[TeleportPlugin] Saved points: " + result);
    	    }
    	    // Valid command, returns true
    		return true;

    	}

    	// Invalid command, returns false
    	return false;
    }
	
}
