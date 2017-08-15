package com.ethplugin.testplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
	Location positionForTP;
	List<TpPoint> tpPoints = new ArrayList<TpPoint>();

    @Override
    public void onEnable(){
        // Actions à effectuer au démarrage du plugin, c'est-à-dire :
        //   - Au démarrage du serveur
        //   - Après un /reload
        getLogger().info("Plugin demarre !");
    }

    @Override
    public void onDisable(){
        // Actions à effectuer à la désactivation du plugin
        //   - A l'extinction du serveur
        //   - Pendant un /reload
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("commandetest")) { // Si c'est la commande "exemple" qui a été tapée:
    		// On fait quelque chose
    		getLogger().info("La commande a bien ete recue par le plugin !");
    	    if(sender instanceof Player) {
    	        // C'est un joueur qui a effectué la commande
    	        Player p = (Player)sender;// On récupère le joueur.
        		getLogger().info("La commande a ete envoyee par un joueur.");    	        
    	    } else {
    	        // C'est la console du serveur qui a effectuée la commande.
        		getLogger().info("La commande a ete envoyee depuis la console du serveur.");    	        
    	    }
    		return true;//On renvoie "true" pour dire que la commande était valide
    	}
    	else if (cmd.getName().equalsIgnoreCase("settpposition")) { // Si c'est la commande "exemple" qui a été tapée:
    		// On fait quelque chose
    		getLogger().info("Commande settpposition recue.");
    		if (args.length > 0) {
    			getLogger().info("Recu argument : " + args[0]);
    		}
    	    if(sender instanceof Player) {
	    		// C'est un joueur qui a effectué la commande
    	        Player p = (Player)sender;// On récupère le joueur.
    	    	if (args.length > 0) {
        	        Location newPosition = p.getLocation();
        	        positionForTP = newPosition;

        	        TpPoint newTpPoint = new TpPoint(args[0], newPosition, p);
        	        tpPoints.add(newTpPoint);
        	        
        	        getLogger().info("La position [" + args[0] + "] a ete sauvegardee."); 
        	        p.sendMessage("La position [" + args[0] + "] a ete sauvegardee."); 
    	    	}
    	    	else {
    	    		p.sendMessage("Vous devez spécifier le nom du point. Exemple /settpposition maison");
        	        getLogger().info("Le joueur n'a pas mis de nom de tpPoint."); 
    	    	}
    	           	        
    	    } else {
    	        // C'est la console du serveur qui a effectuée la commande.
        		getLogger().info("La commande a ete envoyee depuis la console du serveur.");    	        
    	    }
    		return true;//On renvoie "true" pour dire que la commande était valide
    	}
    	else if (cmd.getName().equalsIgnoreCase("gototpposition")) { // Si c'est la commande "exemple" qui a été tapée:
    		// On fait quelque chose
    		getLogger().info("Commande gototpposition recue.");
    	    if(sender instanceof Player) {
    	        // C'est un joueur qui a effectué la commande
    	        Player p = (Player)sender;// On récupère le joueur.
    	        if (args.length > 0) {
    	        	String name = args[0];
    	        	TpPoint tpPoint = TpPoint.findTpPointInList(tpPoints, name, p);
    	        	if (tpPoint == null) {
        	    		p.sendMessage("Ce point n'existe pas.");
            	        getLogger().info("Le joueur demande un point qui n'existe pas."); 
    	        	}
    	        	else {
    	        		p.teleport(tpPoint.getLocation());
            	        getLogger().info("Le joueur a ete teleporte.");    	            	        	
        	    		p.sendMessage("Vous avez ete teleporte.");
    	        	}
    	        }
    	        else {
    	    		p.sendMessage("Vous devez spécifier le nom du point. Exemple /settpposition maison");
        	        getLogger().info("Le joueur n'a pas mis de nom de tpPoint."); 
    	    	}
/*    	        if (positionForTP != null) {
        	        p.teleport(positionForTP);
        	        getLogger().info("Le joueur a ete teleporte.");    	            	        	
    	        }
    	        else {
        	        getLogger().info("Vous n'avez pas sauvegarde de position. Utilisez /settpposition.");    	            	        	    	        	
    	        }*/
    	    } else {
    	        // C'est la console du serveur qui a effectuée la commande.
        		getLogger().info("La commande a ete envoyee depuis la console du serveur.");    	        
    	    }
    		return true;//On renvoie "true" pour dire que la commande était valide
    	}

    	return false;//Si une autre commande a été tapée on renvoie "false" pour dire qu'elle n'était pas valide.
    }


	
}
