package hxpwpt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class SimpleCodecs {
    public static final MapCodec <ItemStack> RESULT_ITEMSTACK = RecordCodecBuilder.mapCodec (
        (instance) -> instance.group (
            ForgeRegistries.ITEMS.getCodec () .fieldOf ("result") .forGetter (ItemStack::getItem),
            Codec.INT.fieldOf ("count") .forGetter (ItemStack::getCount)
        ) .apply (instance, (item, cnt) -> new ItemStack (item, cnt.intValue ()))
    ); 
}
