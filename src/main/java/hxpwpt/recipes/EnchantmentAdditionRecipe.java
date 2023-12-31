package hxpwpt.recipes;

import java.util.List;
import java.util.Map;

import hxpwpt.Enchanters;
import hxpwpt.ingredients.EnchantedIngredient;
import hxpwpt.utils.Suppliers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentAdditionRecipe implements CraftingRecipe {
    private final int total;
    private final List <Ingredient> ingredients;
    private final List <Map <Enchantment, Integer>> books;
    private final Map <Enchantment, Integer> original;
    private final Map <Enchantment, Integer> addon;
    private final ResourceLocation icon;
    private final boolean correct;
    private final Item icon_item;
    private final ItemStack icon_stack;
    public List <Ingredient> ingredients () {
        return this.ingredients;
    }
    public List <Map <Enchantment, Integer>> books () {
        return this.books;
    }
    public Map <Enchantment, Integer> original () {
        return this.original;
    }
    public Map <Enchantment, Integer> addon () {
        return this.addon;
    }
    public ResourceLocation icon () {
        return this.icon;
    }
    public boolean correct () {
        return this.correct;
    }
    public boolean canCraftInDimensions (int x, int y) {
        return x * y >= total;
    }
    public RecipeType <CraftingRecipe> getType () {
        return RecipeType.CRAFTING;
    }
    public CraftingBookCategory category () {
        return CraftingBookCategory.EQUIPMENT;
    }
    public ItemStack getResultItem (RegistryAccess access) {
        return this.icon_stack.copy ();
    }
    public EnchantmentAdditionRecipe (
        List <Ingredient> ingredients, List <Map <Enchantment, Integer>> books,
        Map <Enchantment, Integer> original, Map <Enchantment, Integer> addon,
        ResourceLocation icon, Boolean correct
    ) {
        this.total = ingredients.size () + books.size () + 1;
        this.ingredients = ingredients;
        this.books = books;
        this.original = original;
        this.addon = addon;
        this.icon = icon;
        this.correct = correct;
        Item item = ForgeRegistries.ITEMS.getValue (icon);
        if (item == null) item = Items.DIAMOND_SWORD;
        this.icon_item = item;
        this.icon_stack = Enchanters.enchant (Enchanters.enchant (Suppliers.from_item (item), this.original), this.addon) .get ();
    }
    public boolean matches (CraftingContainer container, Level level) {
        boolean[] ing_flag = new boolean [this.ingredients.size ()];
        boolean[] book_flg = new boolean [this.books.size ()];
        boolean orig_flg = false;
        int i, j;
        for (i=0; i<ing_flag.length; i++) ing_flag [i] = false;
        for (i=0; i<book_flg.length; i++) book_flg [i] = false;
        CTN: for (i=0; i<container.getContainerSize (); i++) {
            ItemStack s = container.getItem (i);
            if (s.isEmpty ()) continue;
            for (j=0; j<ing_flag.length; j++) {
                if (!ing_flag [j] && this.ingredients.get (j) .test (s)) {
                    ing_flag [j] = true;
                    continue CTN;
                }
            }
            for (j=0; j<book_flg.length; j++) {
                if (!book_flg [j] && Enchanters.test_ench (this.books.get (j), Enchanters.book (s))) {
                    book_flg [j] = true;
                    continue CTN;
                }
            }
            if (!orig_flg && Enchanters.test_ench (this.original, s.getAllEnchantments ()) && (!this.correct || s.is (this.icon_item))) {
                orig_flg = true;
            } else return false;
        }
        for (i=0; i<ing_flag.length; i++) orig_flg = orig_flg && ing_flag [i];
        for (i=0; i<book_flg.length; i++) orig_flg = orig_flg && book_flg [i];
        return orig_flg;
    }
    public ItemStack assemble (CraftingContainer container, RegistryAccess access) {
        boolean[] ing_flag = new boolean [this.ingredients.size ()];
        boolean[] book_flg = new boolean [this.books.size ()];
        boolean orig_flg = false;
        int i, j;
        ItemStack stack = ItemStack.EMPTY;
        for (i=0; i<ing_flag.length; i++) ing_flag [i] = false;
        for (i=0; i<book_flg.length; i++) book_flg [i] = false;
        CTN: for (i=0; i<container.getContainerSize (); i++) {
            ItemStack s = container.getItem (i);
            if (s.isEmpty ()) continue;
            for (j=0; j<ing_flag.length; j++) {
                if (!ing_flag [j] && this.ingredients.get (j) .test (s)) {
                    ing_flag [j] = true;
                    continue CTN;
                }
            }
            for (j=0; j<book_flg.length; j++) {
                if (!book_flg [j] && Enchanters.test_ench (this.books.get (j), Enchanters.book (s))) {
                    book_flg [j] = true;
                    continue CTN;
                }
            }
            if (!orig_flg && Enchanters.test_ench (this.original, s.getAllEnchantments ()) && (!this.correct || s.is (this.icon_item))) {
                orig_flg = true;
                stack = s.copy ();
            }
        }
        Map <Enchantment, Integer> enchs = stack.getAllEnchantments ();
        EnchantmentHelper.setEnchantments (Enchanters.mix (enchs, this.addon), stack);
        return stack;
    }
    public RecipeSerializer <EnchantmentAdditionRecipe> getSerializer () {
        return SerializerRegistry.enchantment_addition.get ();
    }
    @Override
    public NonNullList <Ingredient> getIngredients () {
        NonNullList <Ingredient> list = NonNullList.create ();
        list.add (new EnchantedIngredient (this.icon, false, "Enchantments", this.original));
        for (var e : this.books) {
            list.add (new EnchantedIngredient (new ResourceLocation ("enchanted_book"), true, "StoredEnchantments", e));
        }
        list.addAll (this.ingredients);
        return list;
    }
}
