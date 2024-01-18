package hxpwpt.ingredients;

import java.util.Map;

import javax.annotation.Nullable;

import hxpwpt.Enchanters;
import hxpwpt.utils.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedIngredient extends MoreAbstractIngredient {
    private final Item default_item;
    private final boolean determined_item;
    private final String tags_on; // Enchantments for normal items and StoredEnchantments for enchanted books
    private final Map <Enchantment, Integer> enchantments;
    // Getters
    public ResourceLocation item () {
        return ForgeRegistries.ITEMS.getKey (this.default_item);
    }
    public boolean determined_item () {
        return this.determined_item;
    }
    public String tags_on () {
        return this.tags_on;
    }
    public Map <Enchantment, Integer> enchantments () {
        return this.enchantments;
    }
    @Override
    public boolean isEmpty () {
        return false;
    }
    @Override
    public boolean isSimple () {
        return false;
    }
    @Override
    public boolean test (@Nullable ItemStack stack) {
        if (stack == null) return false;
        return Enchanters.test_ench (this.enchantments, Enchanters.by_str (stack, this.tags_on)) && (!determined_item || stack.is (this.default_item));
    }
    public EnchantedIngredient (ResourceLocation item, Boolean determined, String tag, Map <Enchantment, Integer> enchantments) {
        this.default_item = ForgeRegistries.ITEMS.getValue (item);
        this.determined_item = determined.booleanValue ();
        this.tags_on = tag;
        this.enchantments = enchantments;
    }
    @Override
    public ItemStack[] getItems () {
        var res = new ItemStack [1];
        res [0] = Enchanters.enchant (Suppliers.from_item (this.default_item), this.enchantments, this.tags_on) .get ();
        return res;
    }
    public IIngredientSerializer <EnchantedIngredient> serializer () {
        return IngredientRegistry.enchanted.get ();
    }
}
