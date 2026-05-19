package com.norfried.eldritch_takeover.block;

import java.util.Random;

import com.norfried.eldritch_takeover.Main;
import com.norfried.eldritch_takeover.init.BlockInit;
import com.norfried.eldritch_takeover.init.ItemInit;
import com.norfried.eldritch_takeover.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockStraulokBiomass extends Block implements IHasModel
{
    public BlockStraulokBiomass(String name, Material material)
    {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(Main.etcreativetab);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockStraulokBiomass(String name, Material material, float hardness, float resistance, SoundType soundType) {
        this(name, material);
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(soundType);
    }

    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return MathHelper.clamp(this.quantityDropped(random) + random.nextInt(fortune + 1), 1, 4);
    }

    public int quantityDropped(Random random)
    {
        return 2 + random.nextInt(3);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ItemInit.STRAULOK_PROTOPLASM;
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.WHITE_STAINED_HARDENED_CLAY;
    }

    @Override
    public void registerModels()
    {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
