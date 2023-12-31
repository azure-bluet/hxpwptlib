package hxpwpt.ingredients;

import static hxpwpt.HxPwPtLibInit.MOD_ID;

import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class IngredientRegistry {
    public static final DeferredRegister <IIngredientSerializer <?>> registry = DeferredRegister.create (ForgeRegistries.INGREDIENT_SERIALIZERS, MOD_ID);
    public static final RegistryObject <IIngredientSerializer <EnchantedIngredient>> enchanted = registry.register ("enchanted_ingredient", EnchantedSerializer::new);
}
