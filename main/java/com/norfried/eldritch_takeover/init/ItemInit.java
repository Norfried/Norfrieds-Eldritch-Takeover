package com.norfried.eldritch_takeover.init;

import java.util.List;
import java.util.ArrayList;

import com.norfried.eldritch_takeover.item.ItemBase;
import com.norfried.eldritch_takeover.item.food.*;
import com.norfried.eldritch_takeover.item.tools.*;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;

public class ItemInit {
    public static final List<Item> ITEMS = new ArrayList<Item>();

    //Special
    public static final Item MYSTERIOUS_ARTIFACT = new ItemBase("straulkite_tablet").setMaxStackSize(1);
    public static final Item STRAULOK_INVOCATOR = new ItemBase("straulok_tablet_dormant").setMaxStackSize(1);
    public static final Item STRAULOK_INVOCATOR_AWAKE = new ItemBase("straulok_tablet_awakened").setMaxStackSize(1);

    //Objects
    public static final Item STRAULOK_PROTOPLASM = new ItemBase("straulok_protoplasm");
    public static final Item STRAULOK_SCAVENGER_STALK = new ItemBase("straulok_scavenger_stalk");

    //Foods
    public static final Item BURNT_STRAULOK_PROTOPLASM = new ItemFoodProtoplasm("straulok_protoplasm_burnt", 1, 0.1F, false);
    public static final Item CURED_STRAULOK_PROTOPLASM = new ItemFoodBase("straulok_protoplasm_cured", 2, 0.4F, false).setPotionEffect(new PotionEffect(MobEffects.LEVITATION, 20, 0), 0.8F);
    public static final Item STRAULOK_CRAWLER_EYE = new ItemFoodCrawlerEye("straulok_crawler_eye", 3, 0.8F, false);

    //Tool Materials
    public static final Item.ToolMaterial EIDOLON_MATERIAL = EnumHelper.addToolMaterial("tool_eidolon", 2, 781, 4.0F, 2.0F, 5);

    //Tools
    //public static final Item EIDOLON_AXE = new ToolAxe("far_realm_axe", EIDOLON_MATERIAL);
    public static final Item EIDOLON_HOE = new ToolHoe("far_realm_hoe", EIDOLON_MATERIAL);
    public static final Item EIDOLON_PICKAXE = new ToolPickaxe("far_realm_pickaxe", EIDOLON_MATERIAL);
    public static final Item EIDOLON_SHOVEL = new ToolShovel("far_realm_shovel", EIDOLON_MATERIAL);
    public static final Item EIDOLON_SWORD = new ToolSword("far_realm_sword", EIDOLON_MATERIAL);
}
