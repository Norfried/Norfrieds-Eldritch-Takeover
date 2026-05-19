//package com.norfried.eldritch_takeover.block;
/**
import com.norfried.eldritch_takeover.Main;
import com.norfried.eldritch_takeover.init.BlockInit;
import com.norfried.eldritch_takeover.init.ItemInit;
import com.norfried.eldritch_takeover.util.IHasModel;
import com.norfried.eldritch_takeover.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockRealmOre extends Block implements IHasModel, IMetaName
{
    private String name, dimension;

    public BlockRealmOre(String name, Material material, String dimension)
    {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(Main.etcreativetab);

        this.name = name;
        this.dimension = dimension;

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockRealmOre(String name, Material material, String dimension, float hardness, float resistance, SoundType soundType) {
        this(name, material, dimension);
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(soundType);
    }

    @Override
    public void registerModels()
    {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
**/