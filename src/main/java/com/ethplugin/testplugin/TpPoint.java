package com.ethplugin.testplugin;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TpPoint {
	String name;
	Location loc;
	Player player;
	
	TpPoint(String name, Location loc, Player player) {
		this.name = name;
		this.loc = loc;
		this.player = player;
	}
	
	/*
	 * Find the TpPoint in the List, matching name and player
	 * returns player if found, null otherwise
	 */
	public static TpPoint findTpPointInList(List<TpPoint> tpPointList, String name, Player player) {
		for (TpPoint item : tpPointList) {
			// If item matches name and player
			if (item.name.equalsIgnoreCase(name) && item.player.getName().equals(player.getName())) {
				return item;
			}
		}
		// No matching item found
		return null;
	}
	
	public Location getLocation() {
		return loc;
	}
}
