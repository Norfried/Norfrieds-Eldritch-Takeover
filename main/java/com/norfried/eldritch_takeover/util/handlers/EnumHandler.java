//package com.norfried.eldritch_takeover.util.handlers;
/**
import com.norfried.eldritch_takeover.init.BlockInit;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;

public class EnumHandler
{
    public static enum EnumType implements IStringSerializable
    {
        STRAULKITE_STONE(0, "straulkite_stone");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        private final String name, unlocalizedName;

        private EnumType(int meta, String name, String unlocalizedName)
        {
            this(meta, name, name);
        }

        private EnumType(int meta, String name String unlocalizedName)
        {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return this.meta;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static EnumType byMetaData(int meta)
        {
            return META_LOOKUP[meta];
        }

        static
        {
            for (EnumType enumType : values()){
                META_LOOKUP[enumType.getMeta()] = enumType;
            }
        }
    }
}
**/