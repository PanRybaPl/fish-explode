/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.explode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author PanRyba.pl
 */
public class PluginApi {

    private final int tntRange;
    private final Map<Material, Integer> chances;
    private final Random random;

    public PluginApi() {
        this.tntRange = 4;

        this.chances = new HashMap<>();
        this.chances.put(Material.WATER, 50000);
        this.chances.put(Material.STATIONARY_WATER, 80000);
        this.chances.put(Material.OBSIDIAN, 40000);

        this.random = new Random();
    }

    public void handleTnt(Location loc, List<Block> blockList) {
        Bukkit.getLogger().info("explosion: " + loc.toString());
        
        int minx = loc.getBlockX() - this.tntRange;
        int maxx = loc.getBlockX() + this.tntRange;
        int miny = loc.getBlockY() - this.tntRange;
        int maxy = loc.getBlockY() + this.tntRange;
        int minz = loc.getBlockZ() - this.tntRange;
        int maxz = loc.getBlockZ() + this.tntRange;

        World world = loc.getWorld();

        for (int x = minx; x <= maxx; ++x) {
            int xdist = Math.abs(loc.getBlockX() - x);

            for (int z = minz; z <= maxz; ++z) {
                int zdist = Math.abs(loc.getBlockZ() - z);

                for (int y = miny; y <= maxy; ++y) {
                    int dist = Math.abs(loc.getBlockY() - y) + zdist + xdist;

                    Block block = world.getBlockAt(x, y, z);
                    Integer chance = this.chances.get(block.getType());

                    if (chance != null) {
                        if (dist > 1) {
                            chance = chance / dist;
                        }
                        int roll = random.nextInt(100000);
                        if (roll < chance) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
        
        for(Block block : blockList) {
            switch(block.getType()) {
                case BEACON:
                    ItemStack beacon = new ItemStack(Material.BEACON);
                    world.dropItemNaturally(block.getLocation(), beacon);
                    block.setType(Material.AIR);
                    break;
            }
        }
    }
}
