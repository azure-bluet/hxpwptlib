package hxpwpt.plugins;

import static hxpwpt.HxPwPtLibInit.MOD_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hxpwpt.LevelUtils;
import hxpwpt.recipes.EnchantmentAdditionRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class HxPwPtJEIPlugin implements IModPlugin {
    public ResourceLocation getPluginUid () {
        return new ResourceLocation (MOD_ID, "jei_plugin");
    }
    public void registerRecipes (IRecipeRegistration registration) {
        ClientLevel level = LevelUtils.cli ();
        Objects.requireNonNull (level, "level");
        RecipeManager manager = level.getRecipeManager ();
        List <RecipeHolder <CraftingRecipe>> list = new ArrayList <> ();
        for (RecipeHolder <CraftingRecipe> recipe : manager.getAllRecipesFor (net.minecraft.world.item.crafting.RecipeType.CRAFTING)) {
            if (recipe.value () instanceof EnchantmentAdditionRecipe) list.add (recipe);
        }
        registration.addRecipes (
            RecipeType.createFromVanilla (net.minecraft.world.item.crafting.RecipeType.CRAFTING),
            list
        );
    }
}
