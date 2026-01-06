package com.norfried.eldritch_takeover.item.food;

import com.norfried.eldritch_takeover.Main;
import com.norfried.eldritch_takeover.init.ItemInit;
import com.norfried.eldritch_takeover.util.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import static net.minecraft.init.SoundEvents.ENTITY_SPIDER_DEATH;


public class ItemFoodCrawlerEye extends ItemFood implements IHasModel
{
    public ItemFoodCrawlerEye(String name, int amount, float saturation, boolean isWolfFood)
    {
        super(amount, saturation, isWolfFood);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(Main.etcreativetab);
        ItemInit.ITEMS.add(this);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return super.hasEffect(stack) || stack.getMetadata() > 0;
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        player.playSound(ENTITY_SPIDER_DEATH, 0.15F, 0.625F);
        player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 400, 1));
        player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 1));
        player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 300, 0));
        super.onFoodEaten(stack, worldIn, player);
    }

    @Override
    public void registerModels()
    {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
