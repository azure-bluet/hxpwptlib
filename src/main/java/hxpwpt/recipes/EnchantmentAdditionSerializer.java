package hxpwpt.recipes;

import static hxpwpt.Enchanters.map_codec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import hxpwpt.Enchanters;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentAdditionSerializer implements RecipeSerializer <EnchantmentAdditionRecipe> {
    public void toNetwork (FriendlyByteBuf buf, EnchantmentAdditionRecipe recipe) {
        int i;
        buf.writeInt (recipe.ingredients () .size ());
        for (i=0; i<recipe.ingredients () .size (); i++) {
            recipe.ingredients () .get (i) .toNetwork (buf);
        }
        buf.writeInt (recipe.books () .size ());
        for (i=0; i<recipe.books () .size (); i++) {
            Enchanters.toNetwork (buf, recipe.books () .get (i));
        }
        Enchanters.toNetwork (buf, recipe.original ());
        Enchanters.toNetwork (buf, recipe.addon ());
        buf.writeResourceLocation (recipe.icon ());
        buf.writeBoolean (recipe.correct ());
    }
    public EnchantmentAdditionRecipe fromNetwork (FriendlyByteBuf buf) {
        int i, size;
        List <Ingredient> ing = new ArrayList <> ();
        List <Map <Enchantment, Integer>> books = new ArrayList <> ();
        size = buf.readInt ();
        for (i=0; i<size; i++) ing.add (Ingredient.fromNetwork (buf));
        size = buf.readInt ();
        for (i=0; i<size; i++) books.add (Enchanters.fromNetwork (buf));
        Map <Enchantment, Integer> original, addon;
        original = Enchanters.fromNetwork (buf);
        addon = Enchanters.fromNetwork (buf);
        ResourceLocation icon = buf.readResourceLocation ();
        return new EnchantmentAdditionRecipe (List.copyOf (ing), List.copyOf (books), original, addon, icon, buf.readBoolean ());
    }
    public static final Codec <EnchantmentAdditionRecipe> codec = RecordCodecBuilder.create (
        (instance) -> instance.group (
            Ingredient.CODEC_NONEMPTY.listOf () .fieldOf ("ingredients") .forGetter (EnchantmentAdditionRecipe::ingredients),
            map_codec.listOf () .fieldOf ("books") .forGetter (EnchantmentAdditionRecipe::books),
            map_codec.fieldOf ("original") .forGetter (EnchantmentAdditionRecipe::original),
            map_codec.fieldOf ("addon") .forGetter (EnchantmentAdditionRecipe::addon),
            ResourceLocation.CODEC.fieldOf ("original_item") .orElse (new ResourceLocation ("minecraft:diamond_sword")) .forGetter (EnchantmentAdditionRecipe::icon),
            Codec.BOOL.fieldOf ("require_correct_original_item") .orElse (Boolean.FALSE) .forGetter (EnchantmentAdditionRecipe::correct)
        ) .apply (instance, EnchantmentAdditionRecipe::new)
    );
    public Codec <EnchantmentAdditionRecipe> codec () {
        return codec;
    }
}
