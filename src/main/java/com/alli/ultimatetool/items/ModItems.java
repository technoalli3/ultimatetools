package com.alli.ultimatetool.items;

import com.alli.ultimatetool.UltimateToolMain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    //items
    public static final Item DIAMOND_RING = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item GOLDEN_RING = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item IRON_RING = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item STONE_RING = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item WOODEN_RING = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    //tools
    public static final CustomToolItem NETHERITE_ULTIMATE_TOOL = new CustomToolItem(ToolMaterials.NETHERITE, 6f, -2.4F, new Item.Settings().group(ItemGroup.TOOLS).maxDamage(Items.NETHERITE_SWORD.getMaxDamage()*2));
    public static final CustomToolItem DIAMOND_ULTIMATE_TOOL = new CustomToolItem(ToolMaterials.DIAMOND, 5f, -2.4F, new Item.Settings().group(ItemGroup.TOOLS).maxDamage(Items.DIAMOND_SWORD.getMaxDamage()*2));
    public static final CustomToolItem GOLDEN_ULTIMATE_TOOL = new CustomToolItem(ToolMaterials.GOLD, 4f, -2.4f, new Item.Settings().group(ItemGroup.TOOLS).maxDamage(Items.GOLDEN_SWORD.getMaxDamage()*2));
    public static final CustomToolItem IRON_ULTIMATE_TOOL = new CustomToolItem(ToolMaterials.IRON, 3f, -2.4f, new Item.Settings().group(ItemGroup.TOOLS).maxDamage(Items.IRON_SWORD.getMaxDamage()*2));
    public static final CustomToolItem STONE_ULTIMATE_TOOL = new CustomToolItem(ToolMaterials.STONE, 2f, -2.4f, new Item.Settings().group(ItemGroup.TOOLS).maxDamage(Items.STONE_SWORD.getMaxDamage()*2));
    public static final CustomToolItem WOODEN_ULTIMATE_TOOL = new CustomToolItem(ToolMaterials.WOOD, 1f, -2.4f, new Item.Settings().group(ItemGroup.TOOLS).maxDamage(Items.WOODEN_SWORD.getMaxDamage()*2));

    public static void RegisterItems() {
        //items
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "diamond_ring"), DIAMOND_RING);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "golden_ring"), GOLDEN_RING);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "iron_ring"), IRON_RING);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "stone_ring"), STONE_RING);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "wooden_ring"), WOODEN_RING);

        //tools
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "netherite_ultimate_tool"), NETHERITE_ULTIMATE_TOOL);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "diamond_ultimate_tool"), DIAMOND_ULTIMATE_TOOL);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "golden_ultimate_tool"), GOLDEN_ULTIMATE_TOOL);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "iron_ultimate_tool"), IRON_ULTIMATE_TOOL);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "stone_ultimate_tool"), STONE_ULTIMATE_TOOL);
        Registry.register(Registry.ITEM, new Identifier(UltimateToolMain.MOD_ID, "wooden_ultimate_tool"), WOODEN_ULTIMATE_TOOL);
    }
}
