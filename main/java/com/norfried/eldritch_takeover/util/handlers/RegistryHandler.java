package com.norfried.eldritch_takeover.util.handlers;

import com.norfried.eldritch_takeover.init.BlockInit;
import com.norfried.eldritch_takeover.init.ItemInit;
import com.norfried.eldritch_takeover.item.ItemBlockSlab;
import com.norfried.eldritch_takeover.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;


@Mod.EventBusSubscriber
public class RegistryHandler
{
    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
    }
    
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
        event.getRegistry().register(new ItemBlockSlab(BlockInit.STRAULKITE_SMOOTH_SLAB, BlockInit.STRAULKITE_SMOOTH_SLAB, BlockInit.STRAULKITE_SMOOTH_SLAB_DOUBLE).setRegistryName(BlockInit.STRAULKITE_SMOOTH_SLAB.getRegistryName()));
        event.getRegistry().register(new ItemBlockSlab(BlockInit.STRAULKITE_BRICK_SLAB, BlockInit.STRAULKITE_BRICK_SLAB, BlockInit.STRAULKITE_BRICK_SLAB_DOUBLE).setRegistryName(BlockInit.STRAULKITE_BRICK_SLAB.getRegistryName()));
        event.getRegistry().register(new ItemBlockSlab(BlockInit.STRAULKITE_TILE_SLAB, BlockInit.STRAULKITE_TILE_SLAB, BlockInit.STRAULKITE_TILE_SLAB_DOUBLE).setRegistryName(BlockInit.STRAULKITE_TILE_SLAB.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        for(Block block : BlockInit.BLOCKS)
        {
            if(block instanceof IHasModel)
            {
                ((IHasModel)block).registerModels();
            }
        }
        ModelLoader.setCustomStateMapper(BlockInit.STRAULKITE_SMOOTH_SLAB_DOUBLE, new StateMap.Builder().ignore(BlockSlab.HALF).build());
        ModelLoader.setCustomStateMapper(BlockInit.STRAULKITE_BRICK_SLAB_DOUBLE, new StateMap.Builder().ignore(BlockSlab.HALF).build());
        ModelLoader.setCustomStateMapper(BlockInit.STRAULKITE_TILE_SLAB_DOUBLE, new StateMap.Builder().ignore(BlockSlab.HALF).build());

        for(Item item : ItemInit.ITEMS)
        {
            if(item instanceof IHasModel)
            {
                ((IHasModel)item).registerModels();
            }
        }
    }
}
