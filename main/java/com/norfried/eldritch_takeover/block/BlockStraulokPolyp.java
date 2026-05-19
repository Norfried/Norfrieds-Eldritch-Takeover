package com.norfried.eldritch_takeover.block;

import com.norfried.eldritch_takeover.Main;
import com.norfried.eldritch_takeover.entity.tile.TileEntityStraulokPolyp;
import com.norfried.eldritch_takeover.init.BlockInit;
import com.norfried.eldritch_takeover.init.ItemInit;
import com.norfried.eldritch_takeover.util.IHasModel;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.Random;

public class BlockStraulokPolyp extends BlockContainer implements IHasModel
{
    public BlockStraulokPolyp(String name, Material material)
    {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(Main.etcreativetab);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockStraulokPolyp(String name, Material material, float hardness, float resistance, SoundType soundType) {
        this(name, material);
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(soundType);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityMobSpawner();
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

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return 15 + RANDOM.nextInt(15) + RANDOM.nextInt(15);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return ItemStack.EMPTY;
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.WHITE_STAINED_HARDENED_CLAY;
    }

    public void registerModels()
    {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
