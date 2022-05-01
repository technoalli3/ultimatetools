package com.alli.ultimatetool;

import com.alli.ultimatetool.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UltimateToolMain implements ModInitializer {
    public static final TagKey<Block> ULTIMATE_MINEABLE = registerBlock("mineable/ultimate");
    public static final TagKey<Block> SHEARABLE_PLANTS = registerBlock("shearable_plants");
    public static final TagKey<Item> ULTIMATE_TOOLS = registerItem("ultimate_tools");

    public static final String MOD_ID = "ultimatetool";

    private static TagKey<Item> registerItem(String id) {
        return TagKey.of(Registry.ITEM_KEY, new Identifier(id));
    }
    private static TagKey<Block> registerBlock(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(id));
    }

    @Override
    public void onInitialize() {
        ModItems.RegisterItems();
    }
}
