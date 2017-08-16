package com.ethplugin.teleportplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TpPoint {
	private String name;
	private Location loc;
	private String playerName;
	
	TpPoint(String name, Location loc, Player player) {
		this.name = name;
		this.loc = loc;
		this.playerName = player.getName();
	}
	
	TpPoint(String name, Location loc, String playerName) {
		this.name = name;
		this.loc = loc;
		this.playerName = playerName;
	}
	
	/*
	 * Find the TpPoint in the List, matching name and player
	 * returns player if found, null otherwise
	 */
	public static TpPoint findTpPointInList(List<TpPoint> tpPointList, String name, Player player) {
		for (TpPoint item : tpPointList) {
			// If item matches name and player
			if (item.name.equalsIgnoreCase(name) && item.getPlayerName().equals(player.getName())) {
				return item;
			}
		}
		// No matching item found
		return null;
	}
	
	/*
	 * Save a list of TpPoints to config file
	 */
	public static void saveTpPointList(List<TpPoint> tpPointList, TeleportPlugin plugin) {
		String serialized = "";
		for (TpPoint item : tpPointList) {
			String itemSerialized = "";
			itemSerialized += item.getPlayerName() + ";";
			itemSerialized += item.getName() + ";";
			itemSerialized += item.getLocation().getWorld().getName() + ";";
			itemSerialized += item.getLocation().getX() + ";";
			itemSerialized += item.getLocation().getY() + ";";
			itemSerialized += item.getLocation().getZ() + ";";
			itemSerialized += item.getLocation().getYaw() + ";";
			itemSerialized += item.getLocation().getPitch();
			serialized += itemSerialized + "\n";
		}
		// Save to bukkit config file
		plugin.getConfig().set("TpPointListSerialized", serialized);
		plugin.saveConfig();
	}
	
	/*
	 * Load list of TpPoints from config
	 * returns List<TpPoints>
	 */
	public static List<TpPoint> loadTpPointsList(TeleportPlugin plugin) {
		List<TpPoint> tpPoints = new ArrayList<TpPoint>();
		String serialized = plugin.getConfig().getString("TpPointListSerialized");
		if (serialized != null) {
			plugin.getLogger().info("TpPointListSerialized found");
			// Si on a bien une config
			String [] lines = serialized.split("\n");
			for (String line : lines) {
				String [] element = line.split(";");
				if (line.equals("")) {
					// Empty line, skip
					continue;
				}
				else if (element.length != 8) {
					// We don't have the right number of elements in line
					plugin.getLogger().info("Error in reading TpPoints from config. Line do no contain right number of elements: " + line);
					continue;
				}
				else {
					// We have the right number of elements, get elements
					try {
						String playerName = element[0];
						String name = element[1];
						String worldName = element[2];
						double X = Double.parseDouble(element[3]);
						double Y = Double.parseDouble(element[4]);
						double Z = Double.parseDouble(element[5]);
						float yaw = Float.parseFloat(element[6]);
						float pitch = Float.parseFloat(element[7]);
						World w = Bukkit.getServer().getWorld(worldName);
						Location loc = new Location(w, X, Y, Z, yaw, pitch);
						TpPoint newTpPoint = new TpPoint(name, loc, playerName);
						tpPoints.add(newTpPoint);
					}
					catch (Exception e) {
						// Error in parsing element
						plugin.getLogger().info("[TeleportPlugin] Error in reading TpPoints from config. Cannot restore element from line: " + line);	
					}
					
				}
			}
		}
		return tpPoints;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPlayerName() {
		return playerName;
	}
}
