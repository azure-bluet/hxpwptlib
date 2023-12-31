package hxpwpt.recipes;

import static hxpwpt.HxPwPtLibInit.MOD_ID;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class SerializerRegistry {
    public static final DeferredRegister <RecipeSerializer <?>> registry = DeferredRegister.create (ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final RegistryObject <RecipeSerializer <EnchantmentAdditionRecipe>> enchantment_addition = registry.register ("enchantment_addition", EnchantmentAdditionSerializer::new);
}
