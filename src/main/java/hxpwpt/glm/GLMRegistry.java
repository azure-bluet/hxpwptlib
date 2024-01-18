package hxpwpt.glm;

import static hxpwpt.HxPwPtLibInit.MOD_ID;

import com.mojang.serialization.Codec;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class GLMRegistry {
    public static final DeferredRegister <Codec <? extends IGlobalLootModifier>> registry = DeferredRegister.create (ForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);
    public static void load () {
        SimpleAddLM.load ();
    }
}
