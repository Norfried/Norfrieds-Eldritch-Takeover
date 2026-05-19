package com.norfried.eldritch_takeover.entity.living;


import com.google.common.base.Predicate;
import com.norfried.eldritch_takeover.entity.ai.EntityAIStraulokCrawlerLeap;
import com.norfried.eldritch_takeover.util.handlers.LootTableHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;


public class EntityStraulokCrawler extends EntitySpider
{
    private static final UUID MOVEMENT_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A1");
    private static final AttributeModifier MOVEMENT_SPEED_BOOST = (new AttributeModifier(MOVEMENT_SPEED_BOOST_ID, "Movement speed boost", 0.32D, 0)).setSaved(false);
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private int targetChangeTime;

    public EntityStraulokCrawler(World worldIn)
    {
        super(worldIn);
        this.experienceValue = 12;
        this.setSize(1.7F, 1.13F);
    }

    protected void initEntityAI()
    {
        super.initEntityAI();
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, 10, true, false, new Predicate<EntityIronGolem>()
        {
            public boolean apply(@Nullable EntityIronGolem p_apply_1_)
            {
                return p_apply_1_.isPlayerCreated();
            }
        }));
        this.targetTasks.addTask(3, new EntityStraulokCrawler.AIStraulokCrawlerHunt<>(this, EntityAnimal.class));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityEnderCrystal.class, 6.0F, 1.0D, 1.1D));
        this.tasks.addTask(2, new EntityAIStraulokCrawlerLeap(this, 2.0F));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(42.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(9.0D);
    }

    //Code for detecting if the crawler is over the void
    public boolean isOverVoid(World world, BlockPos blockpos) {
        if (world.getHeight(blockpos.getX(), blockpos.getZ()) == 0) return true;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(blockpos);
        while (world.isValid(pos)) {
            pos.setY(pos.getY() - 1);
            if (!world.isAirBlock(pos)) return false;
        }
        return true;
    }

    public void onLivingUpdate()
    {
        //Emits void particles while in the air.
        if (this.world.isRemote && !this.onGround)
        {
            for (int i = 0; i < 2; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        //Code for slow falling over the void
        super.onLivingUpdate();
        if (isOverVoid(this.world, new BlockPos(this.posX, this.posY, this.posZ)) && !this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;

            //Prevents the crawler from entering the void
            if (this.posY <= 0.0D)
            {
                this.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 20, 1, false, false));
            }
        }
    }

    //Code for "Void Hovering"
    protected void updateAITasks()
    {
        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0)
        {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase entitylivingbase = this.getAttackTarget();

        if (isOverVoid(this.world, new BlockPos(this.posX, this.posY, this.posZ)) && entitylivingbase != null && entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset)
        {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    //Code for increasing movement speed while over the void
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        super.setAttackTarget(entitylivingbaseIn);
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        //Hovering eldritch spider go brrrrrrrrr
        if (isOverVoid(this.world, new BlockPos(this.posX, this.posY, this.posZ)) && entitylivingbaseIn != null && !this.onGround)
        {
            this.targetChangeTime = this.ticksExisted;

            if (!iattributeinstance.hasModifier(MOVEMENT_SPEED_BOOST))
            {
                iattributeinstance.applyModifier(MOVEMENT_SPEED_BOOST);
            }
        }
        else
        {
            this.targetChangeTime = 0;
            iattributeinstance.removeModifier(MOVEMENT_SPEED_BOOST);
        }
    }

    //Code for no fall damage.
    public void fall(float distance, float damageMultiplier)
    {

    }


    //Code for sound edits
    protected float getSoundPitch()
    {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.13F + 0.5F;
    }

    //Code for gaining absorption whilst inside of cobwebs.
    public void setInWeb()
    {
        this.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 20, 0, false, false));
    }

    //Immunity to the wither effect.
    public boolean isPotionApplicable(PotionEffect potioneffectIn)
    {
        if(potioneffectIn.getPotion() == MobEffects.WITHER)
        {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, potioneffectIn);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW;
        }
        return super.isPotionApplicable(potioneffectIn);
    }

    //Code for setting eye height
    public float getEyeHeight()
    {
        return 0.815F;
    }

    //Code for adding potion effects to the mob's melee attack
    public boolean attackEntityAsMob(Entity entityIn)
    {
        if (super.attackEntityAsMob(entityIn))
        {
            if (entityIn instanceof EntityLivingBase)
            {
                int time = 0;
                int amplifier = 0;
                //int wither = 0;
                //int slowness = 0;

                switch(this.world.getDifficulty())
                {
                    default:
                    case EASY:
                        time = 6;
                        break;
                    case NORMAL:
                        time = 12;
                        break;
                    case HARD:
                        time = 10;
                        amplifier = 1;
                        break;
                }

                if (time > 0)
                {
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, time * 20, amplifier));
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, time * 20, amplifier));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    //Code for the crawler's "hunting" AI
    static class AIStraulokCrawlerHunt <T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
    {
        public AIStraulokCrawlerHunt(EntityStraulokCrawler hunter, Class<T> classTarget)
        {
            super(hunter, classTarget, true);
        }

        public boolean shouldExecute()
        {
            if (super.shouldExecute())
            {
                float f = this.targetEntity.getHealth();
                return (f < this.targetEntity.getMaxHealth());
            }
            return false;
        }
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {

        if (livingdata == null)
        {
            livingdata = new EntityStraulokCrawler.GroupData();

            if (this.world.getDifficulty() == EnumDifficulty.HARD && this.world.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
            {
                ((EntityStraulokCrawler.GroupData)livingdata).setRandomEffect(this.world.rand);
            }
        }

        if (livingdata instanceof EntityStraulokCrawler.GroupData)
        {
            Potion potion = ((EntityStraulokCrawler.GroupData)livingdata).effect;

            if (potion != null)
            {
                this.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE));
            }
        }

        return livingdata;
    }


    public static class GroupData implements IEntityLivingData
    {
        public Potion effect;

        public void setRandomEffect(Random rand)
        {
            int i = rand.nextInt(5);

            if (i <= 1)
            {
                this.effect = MobEffects.SPEED;
            }
            else if (i <= 2)
            {
                this.effect = MobEffects.STRENGTH;
            }
            else if (i <= 3)
            {
                this.effect = MobEffects.RESISTANCE;
            }
            else if (i <= 4)
            {
                this.effect = MobEffects.JUMP_BOOST;
            }
        }
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        return LootTableHandler.STRAULOKCRAWLER;
    }
}