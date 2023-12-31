package hxpwpt.ingredients;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import hxpwpt.Enchanters;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

public class EnchantedSerializer implements IIngredientSerializer <EnchantedIngredient> {
    private static final Codec <EnchantedIngredient> codec = RecordCodecBuilder.create (
        (instance) -> instance.group (
            ResourceLocation.CODEC.fieldOf ("item") .forGetter (EnchantedIngredient::item),
            Codec.BOOL.fieldOf ("variable") .orElse (Boolean.TRUE) .forGetter (EnchantedIngredient::determined_item),
            Codec.STRING.fieldOf ("tags_on") .orElse ("Enchantments") .forGetter (EnchantedIngredient::tags_on),
            Enchanters.map_codec.fieldOf ("enchantments") .forGetter (EnchantedIngredient::enchantments)
        ) .apply (instance, EnchantedIngredient::new)
    );
    public Codec <EnchantedIngredient> codec () {
        return codec;
    }
    public void write (FriendlyByteBuf buf, EnchantedIngredient ingredient) {
        buf.writeResourceLocation (ingredient.item ());
        buf.writeBoolean (ingredient.determined_item ());
        buf.writeUtf (ingredient.tags_on ());
        Enchanters.toNetwork (buf, ingredient.enchantments ());
    }
    public EnchantedIngredient read (FriendlyByteBuf buf) {
        ResourceLocation location = buf.readResourceLocation ();
        Boolean det = Boolean.valueOf (buf.readBoolean ());
        String tags = buf.readUtf ();
        var enchs = Enchanters.fromNetwork (buf);
        return new EnchantedIngredient (location, det, tags, enchs);
    }
}
