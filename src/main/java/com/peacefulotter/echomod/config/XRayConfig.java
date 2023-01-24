package com.peacefulotter.echomod.config;


import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;

public class XRayConfig extends Config
{
    public static final List<Item> BANNED_ITEMS = List.of(
        // Overworld
        Items.DIRT,
        Items.CLAY,
        Items.TUFF,
        Items.STONE,
        Items.GRAVEL,
        Items.DIORITE,
        Items.GRANITE,
        Items.CALCITE,
        Items.ANDESITE,
        Items.DEEPSLATE,
        Items.COPPER_ORE,
        Items.COBBLESTONE,
        Items.SMOOTH_BASALT,
        Items.REINFORCED_DEEPSLATE,
        // Nether
        Items.BASALT,
        Items.NETHERRACK,
        Items.BLACKSTONE,
        Items.POLISHED_BASALT,
        Items.POLISHED_BLACKSTONE_BRICKS
    );

    public static final List<Item> INTERESTED_ITEMS = List.of(
        // Overworld
        Items.CHEST,
        Items.COAL_ORE,
        Items.IRON_ORE,
        Items.GOLD_ORE,
        Items.DIAMOND_ORE,
        Items.EMERALD_ORE,
        Items.REDSTONE_ORE,
        Items.MOSSY_COBBLESTONE,
        Items.DEEPSLATE_COAL_ORE,
        Items.DEEPSLATE_IRON_ORE,
        Items.DEEPSLATE_GOLD_ORE,
        Items.DEEPSLATE_DIAMOND_ORE,
        Items.DEEPSLATE_EMERALD_ORE,
        Items.DEEPSLATE_REDSTONE_ORE,
        // Nether
        Items.ANCIENT_DEBRIS,
        Items.NETHERITE_BLOCK,
        Items.NETHER_GOLD_ORE,
        Items.NETHER_QUARTZ_ORE
    );

    XRayConfig()
    {
        super( "X-Ray", false );
    }
}
