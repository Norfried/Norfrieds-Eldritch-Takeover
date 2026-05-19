package com.norfried.eldritch_takeover.util.handlers;

import com.norfried.eldritch_takeover.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTableHandler
{
    public static final ResourceLocation STRAULOKCRAWLER = LootTableList.register(new ResourceLocation(Reference.MODID, "straulok_crawler"));
    public static final ResourceLocation STRAULOKSCAVENGER = LootTableList.register(new ResourceLocation(Reference.MODID, "straulok_scavenger"));
}
