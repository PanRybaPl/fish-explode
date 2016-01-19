/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.panryba.mc.explode;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author PanRyba.pl
 */
public class Plugin extends JavaPlugin implements Listener {
    
    private PluginApi api;

    @Override
    public void onEnable() {
        this.api = new PluginApi();
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplosion(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        
        if(entity == null) {
            return;
        }
        
        switch(event.getEntityType()) {
            case PRIMED_TNT:
                TNTPrimed tnt = (TNTPrimed)entity;
                this.api.handleTnt(tnt.getLocation(), event.blockList());
                break;
                
            case MINECART_TNT:
                ExplosiveMinecart cart = (ExplosiveMinecart)entity;
                this.api.handleTnt(cart.getLocation(), new ArrayList<Block>());
                break;
        }
        
    }
}
