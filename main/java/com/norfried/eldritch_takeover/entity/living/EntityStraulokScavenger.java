package com.norfried.eldritch_takeover.entity.living;


import com.google.common.base.Predicate;
import com.norfried.eldritch_takeover.util.handlers.LootTableHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


import javax.annotation.Nullable;


public class EntityStraulokScavenger extends EntityMob
{
    public EntityStraulokScavenger(World worldIn)
    {
        super(worldIn);
        this.experienceValue = 9;
        this.setSize(0.5F, 0.4F);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, 10, true, false, new Predicate<EntityIronGolem>()
        {
            public boolean apply(@Nullable EntityIronGolem p_apply_1_)
            {
                return p_apply_1_.isPlayerCreated();
            }
        }));
        this.targetTasks.addTask(4, new EntityStraulokScavenger.AIStraulokScavengerHunt<>(this, EntityAnimal.class));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityEnderCrystal.class, 6.0F, 1.0D, 1.1D));
    }

    public float getEyeHeight()
    {
        return 0.125F;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_ENDERMITE_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.5F, 1.0F);
    }

    //Code for sound edits
    protected float getSoundPitch()
    {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.13F + 0.5F;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableHandler.STRAULOKSCAVENGER;
    }

    public static void registerFixesEndermite(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityStraulokScavenger.class);
    }

    public static void registerFixesEnderman(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityStraulokScavenger.class);
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
    }

    public void onUpdate()
    {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    public void setRenderYawOffset(float offset)
    {
        this.rotationYaw = offset;
        super.setRenderYawOffset(offset);
    }

    public double getYOffset()
    {
        return 0.1D;
    }

    //Immunity to the wither & poison effects.
    public boolean isPotionApplicable(PotionEffect potioneffectIn)
    {
        if(potioneffectIn.getPotion() == MobEffects.WITHER || potioneffectIn.getPotion() == MobEffects.POISON)
        {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, potioneffectIn);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW;
        }
        return super.isPotionApplicable(potioneffectIn);
    }

    //Code for slow falling over the void
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
        super.onLivingUpdate();

        if (this.world.isRemote)
        {
            for (int i = 0; i < 2; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;

            //Prevents the scavenger from entering the void
            while (isOverVoid(this.world, new BlockPos(this.posX, this.posY, this.posZ)))
            {
                this.teleportRandomly();
            }
        }
    }

    //Code for no fall damage.
    public void fall(float distance, float damageMultiplier)
    {

    }

    protected boolean teleportRandomly()
    {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        this.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 0.5F, 1.0F);
        return this.teleportTo(d0, d1, d2);
    }

    private boolean teleportTo(double x, double y, double z)
    {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag)
        {
            this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 0.5F, 1.0F);
        }

        return flag;
    }

    //Code for teleporting when claustrophobic
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else if (source == DamageSource.IN_WALL || source == DamageSource.CRAMMING || source == DamageSource.DROWN)
        {
            for (int i = 0; i < 64; ++i)
            {
                if (this.teleportRandomly())
                {
                    return true;
                }
            }

            return false;
        }
        else
        {
            boolean flag = super.attackEntityFrom(source, amount);

            if (source.isUnblockable() && this.rand.nextInt(10) != 0)
            {
                this.addPotionEffect(new PotionEffect(MobEffects.HASTE, 100, 0, false, false));
            }

            return flag;
        }
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
                //int weakness = 0;
                //int fatigue = 0;

                switch(this.world.getDifficulty())
                {
                    default:
                    case EASY:
                        time = 8;
                        break;
                    case NORMAL:
                        time = 16;
                        break;
                    case HARD:
                        time = 14;
                        amplifier = 1;
                        break;
                }

                if (time > 0)
                {
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, time * 20, amplifier));
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, time * 20, amplifier));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    //Code for the scavenger's "opportunist" AI
    static class AIStraulokScavengerHunt <T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
    {
        public AIStraulokScavengerHunt(EntityStraulokScavenger hunter, Class<T> classTarget)
        {
            super(hunter, classTarget, true);
        }

        public boolean shouldExecute()
        {
            if (super.shouldExecute())
            {
                float f = this.targetEntity.getHealth();
                return (f < this.targetEntity.getMaxHealth() && f <= this.taskOwner.getMaxHealth());
            }
            return false;
        }
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }
}
