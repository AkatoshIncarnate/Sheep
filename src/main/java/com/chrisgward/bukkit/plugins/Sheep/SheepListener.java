package com.chrisgward.bukkit.plugins.Sheep;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.rmi.MarshalledObject;
import java.util.*;

public class SheepListener implements Listener {
    HashMap<UUID, Collection<ItemStack>> sheepMaterialHashMap = new HashMap<UUID, Collection<ItemStack>>();
    SheepPlugin inst;
    public SheepListener(SheepPlugin instance)
    {
        inst = instance;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Location loc  = e.getBlock().getLocation();
        Sheep sheep = (Sheep)loc.getWorld().spawnCreature(loc, CreatureType.SHEEP);
        int a = (new Random()).nextInt(DyeColor.values().length - 1);
        sheep.setColor(DyeColor.values()[a]);
        sheepMaterialHashMap.put(sheep.getUniqueId(), e.getBlock().getDrops());
        for(int i = 0; i < e.getBlock().getDrops().size(); i++)
        {
            e.getBlock().getDrops().remove(i);
        }
        loc.getBlock().setType(Material.AIR);
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e)
    {
        if(e.getEntity() instanceof Sheep)
        {
            if(sheepMaterialHashMap.containsKey(e.getEntity().getUniqueId()))
            {
                e.setDroppedExp(0);
                e.getEntity().getLocation().getWorld().dropItem(e.getEntity().getLocation(), (ItemStack)sheepMaterialHashMap.get(e.getEntity().getUniqueId()).toArray()[0]);
                for(int i = 0; i < e.getDrops().size(); i++)
                {
                    e.getDrops().remove(i);
                }
            }
        }
    }

}
