package com.norfried.eldritch_takeover.item.food;

import com.norfried.eldritch_takeover.Main;
import com.norfried.eldritch_takeover.init.ItemInit;
import com.norfried.eldritch_takeover.util.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.init.SoundEvents.AMBIENT_CAVE;


public class ItemFoodProtoplasm extends ItemFood implements IHasModel
{
    public ItemFoodProtoplasm(String name, int amount, float saturation, boolean isWolfFood)
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
        player.playSound(AMBIENT_CAVE, 1.0F, 1.0F);
        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
        player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 40, 2));
        player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 0));
        super.onFoodEaten(stack, worldIn, player);
    }

    @Override
    public void registerModels()
    {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
